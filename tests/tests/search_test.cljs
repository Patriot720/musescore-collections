(ns tests.search-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.search.events :as score]
            [tests.fixtures :refer [dummy-db]]))

(deftest  score-title-contains?-test
  (is (true? (#'score/score-title-contains?  "krappa" {:title "krappa"}))))

(deftest search-local-scores-test
  (is (= (score/search-local-scores dummy-db [nil "Krappa"])
         (assoc dummy-db :search-results '({:id 1 :title "Krappa" :url "nice"}
                                           {:id 3 :title "nice Krappa" :url "3nice"}
                                           )))))

