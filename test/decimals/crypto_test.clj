(ns decimals.crypto-test
  (:require [clojure.test :refer :all]
            [decimals.crypto :as c]))

(deftest map-hash-test
  (let [m {:foo 300 :toto 600}
        mm {:foo m}
        t (assoc (assoc {} :toto 600) :foo 300)
        tt {:foo t}
        tt-hash (c/map->md5 tt) ;; {:foo {:foo 300, :toto 600}} 52a197cb51d65d93a047df1b7fa994aa)
        mm-hash (c/map->md5 mm)] ;; {:foo {:foo 300, :toto 600}} d8a50edd5aeb65be51deb87eefc48f93
    (is tt-hash mm-hash)
    ))

(deftest list-hash-test
  (let [list-hash (c/map->md5 {:foo '(1)})
        array-hash (c/map->md5 {:foo [1]})]
    (is list-hash array-hash)))
