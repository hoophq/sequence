(ns decimals.transactions-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.data.json :as json]
            [mockfn.macros :as mfn]
            [clojure.spec.gen.alpha :as gen]
            [decimals.dynamodb :as db]
            [decimals.balances :as b]
            [decimals.specs]
            [decimals.transactions :as tx]))

(deftest hash-txs-test
  "assert that values add up for a transaction"
  (let [customer {:pulic-key "123"}
        tx {:from "Alice", :to "Bob", :amount 10, :currency "usd"} 
        from-balance {:from "Genesis",
                      :to "Alice",
                      :amount 100,
                      :balance 100,
                      :currency "usd",
                      :public-key "123"}
        context  {:tx tx
                  :from { :balance from-balance}
                  :customer customer}]

    ; new destination balance
    (mfn/providing [(b/balance {:public-key nil, :currency "usd", :account "Bob"}) nil]
                   (if-let [result (tx/hash-txs context)]
                     (let [tx-amount (get-in result [:tx :amount])
                           from-balance (get-in result [:from :balance :balance])
                           from-tx (get-in result [:from :tx :balance])
                           to-balance (get-in result [:to :balance :balance])
                           to-tx (get-in result [:to :tx :balance])]
                       (is (= from-tx (- from-balance tx-amount)))
                       (is (= to-tx (+ to-balance tx-amount))))
                     (is true false)))

    ; existing destination balance
    (mfn/providing [(b/balance {:public-key nil, :currency "usd", :account "Bob"})
                    {:from "Matt",
                     :to "Bob",
                     :amount 100,
                     :balance 100,
                     :currency "usd",
                     :date "123"
                     :public-key "123"}]
                   (if-let [result (tx/hash-txs context)]
                     (let [tx-amount (get-in result [:tx :amount])
                           from-balance (get-in result [:from :balance :balance])
                           from-tx (get-in result [:from :tx :balance])
                           to-balance (get-in result [:to :balance :balance])
                           to-tx (get-in result [:to :tx :balance])]
                       (is (= from-tx (- from-balance tx-amount)))
                       (is (= to-tx (+ to-balance tx-amount))))
                     (is true false)))))
