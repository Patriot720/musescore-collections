(ns tests.db-test
  (:require [cljs.test :refer-macros [testing deftest is]]
            [collections-musescore.db :as db]
            ; [collections-musescore.events:as events]
            ))

(deftest shitty-test
  (db/->local-store {1 {:title "nice" :scores {}} 2 {:title "nice" :scores {}}})
  (is (= (db/get-from-local-store) {1 {:title "nice" :scores {}}
                                    2 {:title "nice" :scores {}}})))
(deftest empty-local-store-test
  (db/->local-store nil)
  (is (= (db/get-from-local-store) {})))
