(defproject decimals "0.0.1"
  :description "An immutable, scalable, minimalist ledger API"
  :url "https://github.com/decimals/sequence"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.8"]
                 [io.pedestal/pedestal.jetty "0.5.8"]

                 ;; custom
                 [org.clojure/data.json "1.0.0"]
                 [com.taoensso/faraday "1.11.0-alpha1"]
                 [clojure.java-time "0.3.2"]
                 [clj-http "3.10.1"]
                 [environ "1.2.0"]
                 [mount "0.1.16"]
                 [org.clojure/core.async "1.2.603"]
                 [metosin/spec-tools "0.10.3"]
                 [org.clojure/tools.logging "1.1.0"]
                 [circleci/analytics-clj "0.8.0"]

                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.26"]
                 [org.slf4j/jcl-over-slf4j "1.7.26"]
                 [org.slf4j/log4j-over-slf4j "1.7.26"]]
  :plugins [[lein-environ "1.2.0"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :profiles {:dev [:dev-common :dev-overrides]
             :dev-common {:dependencies [[io.pedestal/pedestal.service-tools "0.5.8"]
                                           [org.clojure/test.check "0.9.0"]
                                           [nubank/mockfn "0.6.1"]]
                          :aliases {"run-dev" ["trampoline" "run" "-m" "decimals.transport/run-dev"]}}
             :dev-overrides {}
  :uberjar {:aot [decimals.transport]}}
  :main ^{:skip-aot true} decimals.transport)
