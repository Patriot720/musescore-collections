(ns tests.events-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.events :as events]))
(def dummy-collections [])
(deftest test-add-collection
  (is (= (events/add-collection dummy-collections [nil "Nice"])
         [(events/collection "Nice" [])])))