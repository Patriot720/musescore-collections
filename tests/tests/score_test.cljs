(ns tests.score-test
  (:require [cljs.spec.alpha :as s]
            [cljs.test :refer-macros [deftest is]]
            [collections-musescore.db :as db]
            [collections-musescore.score.events :as score]
            [tests.fixtures :as fixtures]))

(def dummy-collections-with-score {1 {:id 1
                                      :title "nice"
                                      :scores {}}})

(def expected-response (update fixtures/expected-response :id int))

(deftest add-to-collection-test
  (is (= (#'score/add-score-to-collection
          (get dummy-collections-with-score 1)
          expected-response)
         {:id 1
          :title "nice"
          :scores {(:id expected-response) expected-response}})))

(deftest add-test
  (is (= (score/add-score
          dummy-collections-with-score
          [nil 1 expected-response])
         {1 {:id 1
             :title "nice"
             :scores {(:id expected-response) expected-response}}})))

(deftest spec-test
  (is (true? (s/valid? ::db/collections {1 {:id 1
                                            :title "nice"
                                            :scores {(:id expected-response)
                                                     expected-response}}}))))

(defn count-equals [item expected-count]
  (= (count item)
     expected-count))

;; SCORE REMOVAL

(deftest remove-from-collection
  (let [dummy-collections {1 {:id 1 :title "nice"
                              :scores {1 {:id 1
                                          :title "some_title"
                                          :url "nice"}
                                       2 {:id 2 :title ":url"
                                          :url "nice"}
                                       3 {:id 3 :title ":url"
                                          :url "nice"}}}}]

    (is (= (-> (score/remove-score-from-collections dummy-collections [nil 1 1])
               (get 1) :scores count) 2))))
