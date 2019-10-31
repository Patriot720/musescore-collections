(ns tests.core
  (:require [cljs.test :refer-macros [run-tests]]
            [tests.db-spec-test]
            [tests.db-test]
            [tests.api-test]
            [tests.score-test]
            [tests.search-test]
            [tests.collection-test]
            :reload))

(enable-console-print!)

(defn run-all-tests
  []
  (run-tests
   'tests.db-test
   'tests.api-test
   'tests.score-test
   'tests.collection-test
   'tests.search-test
   'tests.db-spec-test))

(defn rat [] (run-all-tests))
