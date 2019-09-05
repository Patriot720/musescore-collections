(ns tests.motion-test-test
  (:require
   [cljs.test :refer-macros [deftest is]]
   [reagent.core :as reagent]
   [collections-musescore.views.motion-test :as m]))


(deftest swap-test
  (let [dummy-stuff (reagent/atom [{:id 2
                                    :title "nice"}
                                   {:id 3
                                    :title "cool"}])]
    (is (= (count @dummy-stuff) 2))
    (m/remove-item "2" dummy-stuff)
    (is (= (count @dummy-stuff) 1))))