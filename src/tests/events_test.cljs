(ns tests.events-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.events :as events]))
(def dummy-collections (sorted-map))

(deftest test-add-collection
  (is (= (events/add-collection dummy-collections [nil "nice"])
         {1 {:id 1
             :title "nice"
             :scores {}}})))
; TODO test add multiple collections

(def dummy-collections-with-score {1 {:id 1
                                      :title "nice"
                                      :scores {}}})

(deftest add-score-to-collection-test
  (is (= (#'events/add-score-to-collection
          (get dummy-collections-with-score 1)
          "some-score" "url")
         {:id 1
          :title "nice"
          :scores {1 {:id 1
                      :title "some-score"
                      :url "url"}}})))

;forgot to tdd
(deftest add-score-test
  (is (= (events/add-score
          dummy-collections-with-score
          [nil 1 "some_score" "url"])
         {1 {:id 1
             :title "nice"
             :scores {1 {:id 1
                         :title "some_score"
                         :url "url"}}}})))


(defn count-equals [item expected-count]
  (= (count item)
     expected-count))

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

(deftest remove-score-from-collection-test
  (is (= (count (events/remove-score (:scores dummy-collection) 1)) 2)))

(deftest remove-score-from-collection
  (let [dummy-collections {1 {:id 1 :title "nice"
                              :scores {1 {:id 1
                                          :title "some_title"
                                          :url "nice"}
                                       2 {:id 2 :title ":url"
                                          :url "nice"}
                                       3 {:id 3 :title ":url"
                                          :url "nice"}}}}]

    (is (= (-> (events/remove-score-from-collections dummy-collections [nil 1 1]) (get 1) :scores count) 2))))