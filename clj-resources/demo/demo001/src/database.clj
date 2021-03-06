(ns database
  (:require [match :as m]
            [clojure.inspector :as ci]
            [clojure.set :as s]))
(defn mapcan [f & args]
  (filter identity (apply map f args)))

(defn indexed
  "Returns a lazy sequence of [index, item] pairs, where items come
  from 's' and indexes count up from zero.

  (indexed '(a b c d))  =>  ([0 a] [1 b] [2 c] [3 d])"
  [s]
  (map vector (iterate inc 0) s))

(defn positions
  "Returns a lazy sequence containing the positions at which pred
   is true for items in coll."
  [pred coll]
  (for [[idx elt] (indexed coll) :when (pred elt)] idx))

(defn vassoc-in
  [m [k & ks] v]
  (if ks
    (assoc m k (vassoc-in (get m k []) ks v))
    (assoc m k v)))

(defn make-db [] {})

(def ^:dynamic *default-db* (atom (make-db)))

(defn clear-db
  ([] (clear-db *default-db*))
  ([db] (reset! db (make-db))))

(defmacro db-query
  ([key] `(db-query ~key *default-db*))
  ([key db] `(~key @~db)))
(macroexpand '(db-query :position-1))
(vec (vals (db-query :position-1)))

(defn db-push [table vals]
  (let [db *default-db* ]
    (swap! db
           (fn [a vals]
             (m/aif (first (positions #{(first vals)} (@db table)))
                    (assoc-in a [table it] vals)
                    (assoc-in a
                              [table (first vals)]
                              vals)))
           vals)))



(defmacro fact [table & vals]
  #_(let [g (gensym)
        g table])
  `(db-push (keyword '~table) '~vals))

(macroexpand-1 '(fact paiter a q b c d))
(macroexpand '(fact paiter a q b c d))
(fact painter a q b c d)
(db-query :painter)


;; insert
(clear-db)
(fact painter hogarth william english)
(fact painter canale antonio venetian)
(fact painter reynolds joshua english)
(fact dates hogarth 1697 1772)
(fact dates canale 1697 1768)
(fact dates reynolds 1723 1792)

;;; db-query interpreter
;; The function vars-in returns all the pattern variables in an expression.
;; vars-in 함수 구현해야함.
(defn _var? [x]
  (and (symbol? x)
       (let [str (name x)]
         (= (get (name str) (dec (count str))) \?))))

(defn vars-in [expr]
  (if (ci/atom? expr)
    (if (_var? expr) (list expr))
    (s/union (vars-in (first expr))
             (vars-in (next expr)))))
(defn lookup
  ([table args] (lookup table args nil))
  ([table args binds]
   (let [binds (if (nil? (first binds))
                 nil
                 binds
                 #_(vec (apply concat binds)))]
     (filter identity (map (fn [x]
                             (m/aif (m/match x args binds)
                                    (vec it)))
                           (vals (db-query table)))))))


(defmacro with-anser [query & body]
  (let [binds (gensym)]
    `(doseq [~binds (interpret-query '~query)]
       (let [~(filter identity (map
                                (fn [v]
                                  `(~v (m/binding '~v ~binds)))
                                (vars-in query)))]
         ~@body))))

(defn interpret-query 
  ([expr] (interpret-query expr nil))
  ([expr binds]
   (case (first expr)
     and (interpret-and (reverse (next expr)) binds)
     or (interpret-or (next expr) binds)
     not (interpret-not (second expr) binds)
     (lookup (keyword (first expr)) (next expr) binds))))

(defn interpret-and [clauses binds]
  (if (nil? clauses)
    (list binds)
    (first (mapcan (fn [b]
                     (interpret-query (first clauses) b))
                   (interpret-and (next clauses) binds)))))

(defn interpret-or [clauses binds]
  (map (fn [c]
         (interpret-query c binds))
       clauses))

(defn interpret-not [clauses binds]
  (if (interpret-query clauses binds)
    nil
    binds))

;; interpret test
(interpret-query  '(and (painter x? y? z?)
                        (dates x? 1697 w?)))
(interpret-query '(painter x? y? english))
(interpret-query '(and (painter x? y? english)))

(seq (first (interpret-query '(dates x? 1697 w?))))
(lookup :painter '(x? y? z?) (interpret-query '(dates x? 1697 w?)))

(lookup :painter '(x? y? english))

;; lookup test
(comment
  (m/match '(x? y? english)
           (vals (db-query :painter)))
  (map (fn [x]
         x)
       (vals (db-query :painter)))
  (m/match '(x? y? english)
           '(reynolds joshua english))
  (vals (db-query :painter))
  (filter identity  (lookup :painter '(x? y? english))))

;; 다시 만들어야함. 테이블 개념을 추가하지 않음.
;; db를 해시맵으로 만들고, 테이블 안에 [[]] 형태가 되어야 할 듯 함.
(comment
  (clear-db)
  (swap! *default-db* merge [:d :c])
  (macroexpand '(db-query :a))
  (db-push :f "CCC")
  @*default-db*
  (db-query :a)
  (deref *default-db*)
  (swap! *default-db* assoc :b 3))
(comment (:position-1 (deref *default-db*))
         (clear-db)
         (db-push :position-1 1 2 3 4 5)
         (db-push :position-1 2 3 4 5 6)
         (db-query :position-1)
         (vec (vals (db-query :position-1)))
         (-> :position-1
             db-query
             vals
             vec)

         (db-push :hello "AAAF"))
(comment
  (interpret-query '(painter x? y? english))
  (interpret-query '(and (painter x? y? english)))
  (interpret-query '(and (painter x? y? z?)
                         (dates x? 1697 w?))))
