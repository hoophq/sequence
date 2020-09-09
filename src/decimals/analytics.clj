(ns decimals.analytics
  (:require [environ.core :refer [env]]
            [circleci.analytics-clj.core :as a]
            [mount.core :refer [defstate]]
            [clojure.tools.logging :as log]))

(defstate analytics :start (try
                             (a/initialize (env :segment-key))
                             (catch Exception e
                               (log/warnf "failed to initialize analytics: %s"
                                          (.getMessage e)))))

(defn track [context event traits]
  (when-let [analytics analytics]
    (when-let [email (get-in context [:customer :email])]
      (a/track analytics email event traits))))

(defn identify [context]
  (when-let [analytics analytics]
    (when-let [email (get-in context [:customer :email])]
      (a/identify analytics email {:email email}))))
