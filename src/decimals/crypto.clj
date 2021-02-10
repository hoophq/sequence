(ns decimals.crypto
  (:import java.math.BigInteger
           java.security.MessageDigest)
  (:require
   [clojure.data.json :as json]
   ))

(defn md5
  [^String s]
  (->> s
       .getBytes
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%032x")))

(defn map->tuple [data]
  (str
   (:id data)
   (:from data)
   (:to data)
   (:amount data)
   (:currency data)))

(defn map->md5
  [data]
  (->> data
       map->tuple
       .getBytes
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%032x")))
