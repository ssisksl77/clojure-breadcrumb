(ns lambda-calculus.booleans
  (:require [lambda-calculus.lambda :refer :all]))

;; church's boolean
(def _true
  (fn [a] (fn [b] a)))

(def _false
  (fn [a] (fn [b] b)))

;;
(def T
  (λ a (λ b a)))

(def F
  (λ a (λ b b)))

(def And
  (λ p (λ q ((p q) p))))

(def Or
  (λ p (λ q ((p p) q))))

(def Not
  (λ p ((p F) T)))

(def Xor
  (λ a (λ b ((a (Not b)) b))))

(def If
  (λ p (λ a (λ b ((p a) b)))))

(def toBoolean
  (λ f ((f true) false)))

