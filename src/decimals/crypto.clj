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

(defn map->md5
  [data]
  (->> data
       sorted-map
       .getBytes
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%032x")))
