(ns decimals.transport
  (:gen-class) ; for -main method in uberjar
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [clojure.spec.gen.alpha :as gen]
            [clojure.tools.logging :as log]
            [environ.core :refer [env]]
            [mount.core :as mount]
            [decimals.interceptors :as i]
            [decimals.analytics :as a]
            [decimals.specs]
            ))

(def service-map
  {::http/routes i/routes
   ::http/allowed-origins {:allowed-origins (when-let [origins (:allowed-origins env)]
                                              (read-string origins))
                           :methods "GET,POST"}
   ::http/type   :jetty
   ::http/host   "0.0.0.0"
   ::http/port   8910})

(defn start []
  (mount/start)
  (http/start (http/create-server service-map)))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "\nCreating your server...")
  (start))

;; For interactive development
(defonce server (atom nil))

(defn start-dev []
  (mount/start)
  (reset! server
          (http/start (http/create-server
                       (assoc service-map
                              ::http/join? false)))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))

;(restart)
