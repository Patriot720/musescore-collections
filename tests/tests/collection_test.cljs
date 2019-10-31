(ns tests.collection-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.collection.events :as collection]))


(def dummy-collections (sorted-map))

(deftest add-collection-test
  (is (= (collection/add-collection dummy-collections [nil "nice"])
         {1 {:id 1
             :title "nice"
             :scores {}}})))
;; TODO collection remove test
