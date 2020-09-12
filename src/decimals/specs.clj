(ns decimals.specs
  (:require [clojure.spec.alpha :as s]
            [decimals.transactions :as tx]
            [decimals.balances :as b]))

(s/def ::tx/id (s/and string? #(> (count %) 1)))
(s/def ::tx/date (s/and string? #(> (count %) 1)))
(s/def ::tx/from (s/and string? #(> (count %) 1)))
(s/def ::tx/to (s/and string? #(> (count %) 1)))
(s/def ::tx/amount (s/and integer? #(>= % 0)))
(s/def ::tx/balance (s/and integer? #(>= % 0)))
(s/def ::tx/currency (s/and string? #(> (count %) 1)))
(s/def ::tx/public-key (s/and string? #(> (count %) 1)))

(s/def ::tx/account (s/keys :req-un [(or ::tx/from ::tx/to)]))

(s/def ::tx/transaction (s/keys :req-un [::tx/from
                                         ::tx/to
                                         ::tx/amount
                                         ::tx/currency]
                                :opt-un [::tx/metadata]))

(s/def ::tx/pub-transaction (s/keys :req-un [::tx/id
                                             ::tx/from
                                             ::tx/to
                                             ::tx/amount
                                             ::tx/balance
                                             ::tx/currency
                                             ::tx/metadata
                                             ::tx/date]))

(s/def ::b/account (s/and string? #(> (count %) 1)))

(s/def ::b/balance-tx (s/keys :req-un [::tx/from
                                       ::tx/to
                                       ::tx/amount
                                       ::tx/currency
                                       ::tx/balance
                                       ::tx/public-key
                                       ::tx/date]))

(s/def ::b/pub-balance (s/keys :req-un [::tx/balance ::tx/currency ::b/account]))
