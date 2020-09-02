(ns decimals.balances
  (:require [decimals.dynamodb :as db]
            [clojure.tools.logging :as log]
            [decimals.crypto :as crypto]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [java-time :as t]))

(defn ctx->id
  ([context]
   (conj {:public-key (get-in context [:customer :public-key])}
         {:currency (get-in context [:tx :currency])}
         {:account (get-in context [:request :query-params :account])}))
  ([context party]
   (conj {:public-key (get-in context [:customer :public-key])}
         {:currency (get-in context [:tx :currency])}
         {:account (get-in context [:tx party])})))

(defn id->pk [id]
  (str (:public-key id) "#" (:account id)))

(defn list-balances [query]
  (when-let [balances (db/list-balances query)]
    balances))

(defn balance [query]
  (log/debug "querying " query)
  (when-let [balance (db/query-balance (id->pk query) (:currency query))]
    (if (s/valid? ::balance-tx balance)
      balance
      (log/warn "invalid balance in database: " balance (s/explain-data ::balance-tx balance)))))

(defn account->genesis [account party]
  (let [acc-id (party account)
        currency (:currency account)]
    {:id (str acc-id "#" currency)
     :public-key (:public-key account)
     :party party
     :from acc-id
     :to acc-id
     :amount 0
     :currency currency
     :date (str (t/instant))
     :timestamp (str (- (t/to-millis-from-epoch (t/instant)) 1))
     :type :genesis
     :balance 0}))

(defn context->account [context party]
  (let [tx (:tx context)
        cust (:customer context)
        party (select-keys tx [party])
        currency (select-keys tx [:currency]) 
        pub-key (select-keys cust [:public-key])
        account (conj party pub-key currency)]
    account))

(defn is-genesis [account]
  (= (:public-key account) (:from account)))

(defn genesis [context]
  (let [tx (:tx context)
        account (context->account context :from)]

    (when (is-genesis account)
      (if (and (contains? context :from) (contains? (:from context) :balance))

        (let [genesis-balance (get-in context [:from :balance])
              genesis-funds (assoc genesis-balance :balance (+ (:balance genesis-balance)
                                                            (* 2 (:amount tx))))]
          genesis-funds)

        (let [genesis-tx (account->genesis account :from)
              genesis-funds (assoc genesis-tx :balance (* 2 (:amount tx)))]
          genesis-funds)))))
