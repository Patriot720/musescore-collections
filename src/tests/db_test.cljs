(ns tests.db-test
  (:require [cljs.test :refer-macros [testing deftest is]]
            [collections-musescore.db :as db]
            ; [collections-musescore.events :as events]
            ))

(deftest shitty-test
  (db/->local-store [{:title "nice" :scores []} {:title "nice" :scores []}])
  (is (= (db/get-from-local-store) [{:title "nice" :scores []}
                                    {:title "nice" :scores []}])))

; (deftest localstore-test
;   (is (= (db/get-from-local-store) [{:title "nice" :scores []}])))