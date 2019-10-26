(ns tests.data.score-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.data.score :as score]
            [cljs.spec.alpha :as s]
            [collections-musescore.db :as db]
            [tests.api.fixtures :as fixtures]))

(def dummy-collections-with-score {1 {:id 1
                                      :title "nice"
                                      :scores {}}})

(def expected-response (update fixtures/expected-response :id int))

(deftest add-to-collection-test
  (is (= (#'score/add-to-collection
          (get dummy-collections-with-score 1)
          expected-response)
         {:id 1
          :title "nice"
          :scores {(:id expected-response) expected-response}})))

(deftest add-test
  (is (= (score/add
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
(def dummy-db  {:collections {1 {:id 1
                                 :title "nice"
                                 :scores {1 {:id 1
                                             :title "Krappa"
                                             :url "nice"}
                                          2 {:id 2
                                             :title "nice score"
                                             :url "2nice"}
                                          3 {:id 3
                                             :title "nice Krappa"
                                             :url "3nice"}}}}})

(deftest remove-from-collection
  (let [dummy-collections {1 {:id 1 :title "nice"
                              :scores {1 {:id 1
                                          :title "some_title"
                                          :url "nice"}
                                       2 {:id 2 :title ":url"
                                          :url "nice"}
                                       3 {:id 3 :title ":url"
                                          :url "nice"}}}}]

    (is (= (-> (score/remove-from-collections dummy-collections [nil 1 1])
               (get 1) :scores count) 2))))

(deftest  score-title-contains?-test
  (is (true? (#'score/score-title-contains?  "krappa" {:title "krappa"}))))

(deftest search-collections
  (is (= (score/search-scores dummy-db [nil "Krappa"])
         (assoc dummy-db :search-results '({:id 1 :title "Krappa" :url "nice"}
                                           {:id 3 :title "nice Krappa" :url "3nice"}
                                           )))))
