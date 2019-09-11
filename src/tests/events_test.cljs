(ns tests.events-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.events :as events]))
(def dummy-collections [])
(deftest test-add-collection
  (is (= (events/add-collection dummy-collections [nil "Nice"])
         [(events/collection "Nice" [])])))

(def dummy-collections-with-score [{:title "nice"
                                    :scores []}])

(deftest add-score-to-collection-test
  (is (= (#'events/add-score-to-collection
          (first dummy-collections-with-score)
          "some-score" "url")
         {:title "nice"
          :scores [{:title "some-score"
                    :url "url"}]})))

; TODO forgot to tdd
(deftest add-score-test
  (is (= (events/add-score
          dummy-collections-with-score
          [nil "nice" "some_score" "url"])
         [{:title "nice"
           :scores [{:title "some_score"
                     :url "url"}]}])))
