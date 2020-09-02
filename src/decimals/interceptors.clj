(ns decimals.interceptors
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.interceptor.chain :as chain]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [clojure.walk]
            [clojure.core.async :as async]
            [spec-tools.core :as st]
            [clojure.spec.alpha :as s]
            [decimals.security :as sec]
            [decimals.crypto :as crypto]
            [decimals.transactions :as tx]
            [decimals.braintree :as braintree]
            [decimals.analytics :as analytics]
            [decimals.balances :as b]
            [decimals.cache :as c]))

(def msg
  {:generic { :message "Sorry, we had a problem. Please, try again or reach out."}
   :funds { :message "Insufficient funds, check origin account balance."}
   :creds { :message "Invalid credentials."}})

(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok       (partial response 200))
(def created  (partial response 201))
(def accepted (partial response 202))
(def badrequest (partial response 400))
(def forbidden (partial response 401))
(def not-found (partial response 404))
(def server-error (partial response 500))

(defn req-meta [context]
  (select-keys (:request context) [:path-info
                                   :remote-addr
                                   :request-method
                                   :server-name
                                   :transit-params
                                   :uri
                                   ]))

(defn respond [context response]
  (let [m (req-meta context)]
    (case (:status response)
      200 (analytics/track context "Success" m)
      201 (analytics/track context "Created" m)
      400 (analytics/track context "Bad request" m)
      401 (analytics/track context "Unauthorized" m)
      500 (analytics/track context "Server Error" m)))
  (chain/terminate (assoc context :response response)))

(def genesis-balance
  {:name :genesis-balance
   :enter
   (fn [context]
     (if-let [genesis (b/genesis context)]
       (assoc-in context [:from :balance] genesis)
       context))})

(def check-balance
  {:name :check-balance
   :enter
   (fn [context]
     (log/debug "checking origin funds " (get-in context [:from :balance :balance]) (get-in context [:tx :amount]))
     (if-let [balance (get-in context [:from :balance :balance])]
       (when-let [amount (get-in context [:tx :amount])]
         (if-not (>= balance amount)
           (respond context (badrequest (:funds msg)))
           context))
       (respond context (badrequest (:funds msg)))))})

(def hash-txs
  {:name :hash-txs
   :enter
   (fn [context]
     (if-let [ctx (tx/hash-txs context)]
       ctx
       (respond context (server-error {:message (:generic msg)}))))})

(def chain-txs
  {:name :chain-tx
   :enter
   (fn [context]
     (let [r (tx/chain context)]
       (if-let [reply (:success r)]
         (respond context (created (map #(st/select-spec ::tx/pub-transaction %) reply)))
         (respond context (server-error {:message (:generic msg)})))))})

(def spec-tx
  {:name :spec-tx
   :enter
   (fn [context]
     (if-not (s/valid? ::tx/transaction (:tx context))
       (respond context (badrequest {:error (s/explain-data ::tx/transaction (:tx context))}))
       context))})

(defn str->map [str]
  (try
    (-> str json/read-str clojure.walk/keywordize-keys)
    (catch Exception e)))

(def parse-tx
  {:name :parse-tx
   :enter
   (fn [context]
     (if-let [tx (str->map (slurp (:body (:request context))))]
       (assoc context :tx tx)
       (respond context (badrequest {:error "Malformed JSON."}))))})

(def account-queryparam
  {:name :account-queryparam
   :enter
   (fn [context]
     (if-let [acc-id (get-in context [:request :query-params :account])]
       (let [id (b/ctx->id context)]
         (log/debug "Querying account: " id)
         (assoc context :account id))
       (let [pk (get-in context [:customer :public-key])]
         (log/debug "Querying public-key " pk)
         (assoc-in context [:account :public-key] pk))))})

(def transaction-queryparam
  {:name :transaction-queryparam
   :enter
   (fn [context]
     (if-let [tx-id (select-keys context [:request :path-params :transaction-id])]
       (let [id (conj (select-keys context [:customer :public-key])
                      tx-id)]
         (log/debug "Querying transaction: " id)
         (assoc context :tx-id id))
       (respond context (badrequest {:error "Missing transaction path parameter."}))))})

(def pagination
  {:name :pagination
   :enter
   (fn [context]
     (if-let [after (get-in context [:request :query-params :starting_after])]
       (assoc context :starting-after after)
       context))})

(def list-transactions
  {:name :list-transactions
   :enter
   (fn [context]
     (if-let [transactions (tx/list-transactions
                            (conj (:account context)
                                  (select-keys context [:starting-after])))]
       (respond context (ok (map #(st/select-spec ::tx/pub-transaction %) transactions)))
       (respond context (not-found {:error "Account not found."}))))})

(def list-balances
  {:name :list-balances
   :enter
   (fn [context]
     (if-let [balances (b/list-balances (:account context))]
       (respond context (ok (map #(st/select-spec ::b/pub-balance %) balances)));
       (respond context (not-found {:error "Account not found."}))))})

(def auth
  {:name :auth
   :enter
   (fn [context]
     (if-let [customer (sec/apikey-auth context)]
       (let [ctx (assoc context :customer customer)]
         (analytics/identify ctx)
         ctx)
       (respond context (forbidden (:creds msg)))))})

(def from-balance
  {:name :from-balance
   :enter
   (fn [context]
     (if-let [from (b/balance (b/ctx->id context :from))]
       (assoc-in context [:from :balance] from)
       context))})

(def routes
  (route/expand-routes
   #{["/v1/transactions"            :post
      [http/json-body auth parse-tx spec-tx from-balance genesis-balance check-balance hash-txs chain-txs]
      ;auth
      :route-name :transactions-post]
     ["/v1/transactions/:transaction-id"            :get
      [http/json-body auth transaction-queryparam]
      :route-name :transactions-get]
     ["/v1/transactions"            :get
      [http/json-body auth account-queryparam pagination list-transactions]
      :route-name :transactions-list]
     ["/v1/balances"            :get
      [http/json-body auth account-queryparam list-balances]
      :route-name :balances-get]}))
