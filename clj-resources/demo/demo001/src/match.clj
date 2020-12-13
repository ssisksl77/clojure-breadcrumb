(ns match
  (:refer-clojure :exclude [binding]))

(defmacro aif [test-form then-form & else-form]
  `(let [~'it ~test-form]
     (if ~'it
       ~then-form
       ~@else-form)))

;; aif test
(comment (aif '(1 2 3)
              (str "TRUE=" it)
              "FALSE")
         (aif nil
              (str "TRUE" it)
              "FALSE"))

(defmacro acond [& cls]
  (if (nil? cls)
    nil
    (let [cl (first cls)
          sym (gensym)]
      `(let [~sym ~(first cl)]
         (if ~sym
           (let [~'it ~sym]
             ~@(rest cl))
           (acond ~@(rest cls)))))))

(comment (acond
          (false "wrong")
          ((+ 2 1) (str "THIS IS =" it))
          ((/ 3 0) (str "NOT EVALUATE"))))

(defn varsym? [x]
  (and (symbol? x)
       (let [str (name x)]
         (= (get (name str) (dec (count str))) \?))))

(defn v-find [x col]
  (loop [c  col]
    (if (nil? c)
      nil
      (if (= (first (first c)) x)
        (first c)
        (recur (next c))))))

(v-find :y [[:x 1] [:y 2]])

;; again
(defn binding [x binds]
  (letfn [(recbind [x binds]
            (aif (v-find x binds)
                 (or (recbind (second it) binds)
                     it)))]    
    (if-let [b (recbind x binds)]
      b)))


(binding 'x? [['x? 'a?]
              ['a? 2]])

(defn match
  ([x y] (match x y nil))
  ([x y binds]
   (acond
    ((or (= x y) (= x '_) (= y '_)) (vec binds))
    ((binding x binds) (match it y binds))
    ((binding y binds) (match x it binds))
    ((varsym? x) (conj (vec binds) [x y]))
    ((varsym? y) (conj (vec binds) [y x]))
    ((and (seq? x)
          (seq? y)
          (match (first x)
                  (first y)
                  binds))
     (match (next x) (next y) it))
    (:default nil))))

(match '(p a b c) '(p x? y? c))
(match '(p a b c a) '(p x? y? v x?))
(match '(p a) '(x? c))
(match '(p a b c a) '(p x? y? c x?))
