(ns tests.data.collection-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.data.collection :as collection]))


(def dummy-collections (sorted-map))

(deftest add-test
  (is (= (collection/add dummy-collections [nil "nice"])
         {1 {:id 1
             :title "nice"
             :scores {}}})))
;; TODO collection remove test
