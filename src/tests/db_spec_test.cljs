(ns tests.db-spec-test
  (:require [cljs.test :refer-macros [deftest is]]
            [collections-musescore.db]
            [cljs.spec.alpha :as s]))

(deftest this_spec_should_not_throw_errors
  (println  :collections-musescore.db/db)
  (is (true? (s/valid? true? true))))

;// TODO spec just title 
;// TODO spec just score 
;// TODO spec just collection 