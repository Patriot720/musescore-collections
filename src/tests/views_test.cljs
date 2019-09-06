(ns tests.views-test
  (:require
   [reagent.core :as reagent]
   [cljs.test :refer-macros [deftest is]]
   [collections-musescore.views.views :as views]))

(defn count-equals [item expected-count]
  (= (count item)
     expected-count))

(deftest remove-score-from-collection-test
  (is (count-equals (views/remove-score (:scores views/dummy-collection) "Roses of castemire") 3)))

(deftest remove-score-from-collection
  (let [dummy-collections (reagent/atom [views/dummy-collection])]
    (is (count-equals (-> @dummy-collections first :scores) 4))
    (views/remove-score-from-collections dummy-collections "SightRead" "Roses of castemire")
    (is (count-equals (-> @dummy-collections first :scores) 3))))
