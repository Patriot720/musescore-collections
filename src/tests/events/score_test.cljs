(ns tests.events.score-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.events.score :as score]))




(def dummy-collections-with-score {1 {:id 1
                                      :title "nice"
                                      :scores {}}})

(deftest add-to-collection-test
  (is (= (#'score/add-to-collection
          (get dummy-collections-with-score 1)
          "some-score" "url")
         {:id 1
          :title "nice"
          :scores {1 {:id 1
                      :title "some-score"
                      :url "url"}}})))

;forgot to tdd
(deftest add-test
  (is (= (score/add
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