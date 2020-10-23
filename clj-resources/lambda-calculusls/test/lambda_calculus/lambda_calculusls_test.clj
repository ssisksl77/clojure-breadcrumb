(ns lambda-calculus.lambda-calculusls-test
  (:require [clojure.test :refer :all]
            [lambda-calculus.lambda-calculusls :refer :all]))

;; C-c C-t t
(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest zero-test
  (testing "is zero"
    (is (iszero? zero))))
