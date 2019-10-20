(ns tests.core

  (:require [cljs.test :refer-macros [deftest is]]))

(deftest successful-test
  (is (= 1 1)))
