(ns deep-walking-macro.core
  (:gen-class))

(defmacro only-ints [& args]
  (assert (every? integer? args))
  (vec args))

#_(only-ints 1 2 3 4)
#_(only-ints 1 " s" 3 4)

;; 이건 컴파일 에러남.
#_(defn test-fn [x]
  (only-ints x))

(defn only-ints-fn [& args]
  (assert (every? integer? args))
  (vec args))

;; runtime failure만 가능함.
(defn test-fn [x]
  (only-ints-fn x))

;; 왜 이렇게 하면 나은가?
#_(defn test-fn2 [x]
  (only-ints x))


(defmacro my-when [test & body]
  (println &env)
  `(if ~test
     (do ~@body)
     nil))

#_(my-when true (println "hello") 42)
#_(fn [x] (my-when true (println "hello") 42))
#_(meta #'my-when) ;; :macro true 인점만 function하고 다른 점.
;; that is only different between macro and function
;; so the compiler is executing and it looks for a function invocation
;; like here in the #'my-when it looks at it and says "what is my-when"
;; and if it set to {:macro true}.
;; 이걸 계속 안으로 들어가서 확인한다.


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

