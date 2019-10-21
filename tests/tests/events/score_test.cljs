(ns tests.events.score-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.events.score :as score]
            [tests.api.fixtures :as fixtures]))

(def dummy-collections-with-score {1 {:id 1
                                      :title "nice"
                                      :scores {}}})

(def expected-score {:title
                              "Game Of Thrones - Main Theme - Piano Arrangement"
                              :favoriting_count 625
                              :id 4801654
                              :permalink (:permalink fixtures/expected-response)
                              :view_count "25605"
                              :comment_count 29
                              :poet "Arranged by AmiAll"})

(deftest add-to-collection-test
  (is (= (#'score/add-to-collection
          (get dummy-collections-with-score 1)
          fixtures/expected-response)
         {:id 1
          :title "nice"
          :scores {(:id expected-score) expected-score}})))

#_(deftest add-test
  (is (= (score/add
          dummy-collections-with-score
          [nil fixtures/expected-response])
         {1 {:id 1
             :title "nice"
             :scores {(:id expected-score) expected-score}}})))

(defn count-equals [item expected-count]
  (= (count item)
     expected-count))

;; SCORE REMOVAL
(def dummy-collection {:id 1
                       :title "nice"
                       :scores {1 {:id 1
                                   :title "url"
                                   :url "nice"}
                                2 {:id 2
                                   :title ":url"
                                   :url "nice"}
                                3 {:id 3
                                   :title ":url"
                                   :url "nice"}}})

(deftest remove-from-collection
  (let [dummy-collections {1 {:id 1 :title "nice"
                              :scores {1 {:id 1
                                          :title "some_title"
                                          :url "nice"}
                                       2 {:id 2 :title ":url"
                                          :url "nice"}
                                       3 {:id 3 :title ":url"
                                          :url "nice"}}}}]

    (is (= (-> (score/remove-from-collections dummy-collections [nil 1 1]) (get 1) :scores count) 2))))
