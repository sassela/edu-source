(ns ers.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [ers.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
