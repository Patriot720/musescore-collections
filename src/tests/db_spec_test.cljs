(ns tests.db-spec-test
  (:require [cljs.test :refer-macros [testing deftest is]]
            [collections-musescore.db :as db]
            [collections-musescore.events :as events]
            [cljs.spec.alpha :as s]))

(deftest this_spec_should_not_throw_errors
  (is (true? (s/valid? ::db/db  {:collections {}}))))

(deftest wrong-collections-should-be-false
  (is (false? (s/valid? ::db/collections nil))))

(deftest valid-collection-test
  (is (true? (s/valid? ::db/collection {:title "nice" :scores []}))))

(deftest wrong-collection-test
  (is (false? (s/valid? ::db/collection {:title 1 :scores nil}))))

(deftest title_spec_should_approve_string
  (is (true? (s/valid?  ::db/title "nice"))))

(deftest empty-score-should-be-valid
  (is (true? (s/valid? ::db/score nil))))

(deftest record-collections-should-be-valid
  (is (true? (s/valid? ::db/collection (events/->Collection "Nice" []))))
  (is (true? (s/valid? ::db/collections [(events/->Collection "Nice" [])]))))


(deftest score-should-be-valid
  (is (true? (s/valid? ::db/score {:title "some-title" :url "url"})))
  (testing "invalid score should fail"
    (is (false? (s/valid? ::db/score {:title 2})))))
