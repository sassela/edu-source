(ns ers.core-test
  (:require [clojure.test :refer :all]
            [ers.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))