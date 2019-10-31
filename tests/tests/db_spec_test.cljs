(ns tests.db-spec-test
  (:require
   [cljs.test :refer-macros [testing deftest is]]
   [collections-musescore.db :as db]
   [collections-musescore.collection.events :as collection]
   [cljs.spec.alpha :as s]))

(deftest this_spec_should_not_throw_errors
  (is (true? (s/valid? ::db/db  {:collections {}}))))

(deftest valid-collections-test
  (is (true? (s/valid? ::db/db {:collections {0 {:title "nice"
                                                 :scores []}}}))))
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
  (is (true? (s/valid? ::db/collection (collection/collection 1 "Nice" {}))))
  (is (true? (s/valid? ::db/collections {0 (collection/collection 1 "Nice" {})}))))

(deftest scores-spec-test
  (is (true? (s/valid? ::db/scores {1 {:title "some-title" :permalink "url"}}))))

(deftest score-should-be-valid
  (is (true? (s/valid? ::db/score {:title "some-title" :permalink "url"})))
  (testing "invalid score should fail"
    (is (false? (s/valid? ::db/score {:title 2})))))
