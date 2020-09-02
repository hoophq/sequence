(ns decimals.security
  (:import java.security.MessageDigest
           java.util.Base64)
  (:require [clojure.tools.logging :as log]
            [environ.core :refer [env]]
            [clojure.walk]
            [clojure.string :as str]
            [clojure.data.json :as json]))

(defn sha256
  [string]
  (let [digest (.digest (MessageDigest/getInstance "SHA-256") (.getBytes string "UTF-8"))]
    (apply str (map (partial format "%02x") digest))))

(defn match-keys [digest keys]
  (->> keys
       (filter #(= (:secret-key-hash %) digest))
       seq
       first))

(defn authenticate
  [k keys]
  (log/debug "Matching: " k keys)
  (when-let [key-digest (sha256 k)]
    (when-let [account (match-keys key-digest keys)]
      account)))

(defn decode [to-decode]
  (try
    (String. (.decode (Base64/getDecoder) to-decode))
    (catch Exception e)))

(defn remove-colon [s]
  (subs s 0 (- (count s) 1)))

(defn authz-header [context]
  (get-in context [:request :headers "authorization"]))

(defn header-key
  [context]
  (when-let [header (authz-header context)]
    (-> header
        (str/split #" ")
        second
        decode
        remove-colon)))

(defn str->map [str]
  (try
    (-> str json/read-str clojure.walk/keywordize-keys)
    (catch Exception e (log/errorf "failde to parse keys %s" (.getMessage e)))))


(defn apikey-auth
  [context]
  (if-let [key (header-key context)]
    (if-let [keys (str->map (env :keys))]
      (if-let [customer (authenticate key keys)]
        (let []
          (log/debug "Got customer: " customer)
          customer)
        (log/debug "Invalid credentials"))
      (log/warn "Error parsing keys from env"))
    (log/debug "Invalid Authz header: " (authz-header context))))
