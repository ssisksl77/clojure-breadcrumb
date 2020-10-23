(ns lambda-calculus.lambda-calculusls
  (:require [lambda-calculus.lambda :refer :all]
            [lambda-calculus.booleans :refer :all]
            [lambda-calculus.numerals :refer :all])
  (:gen-class))

;; https://dzone.com/articles/lambda-calculus-in-clojure-part-1
;; https://dzone.com/articles/lambda-calculus-in-clojure-part-2

(toStr zero)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
