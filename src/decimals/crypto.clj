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

(defn sort-map [data]
  (into (sorted-map) (sort-by first (seq data))))

(defn map->md5
  [data]
  (->> data
       sort-map
       prn-str
       .getBytes
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%032x")))
