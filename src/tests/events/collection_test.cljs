(ns tests.events.collection-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.events.collection :as collection]))


(def dummy-collections (sorted-map))

(deftest add-test
  (is (= (collection/add dummy-collections [nil "nice"])
         {1 {:id 1
             :title "nice"
             :scores {}}})))