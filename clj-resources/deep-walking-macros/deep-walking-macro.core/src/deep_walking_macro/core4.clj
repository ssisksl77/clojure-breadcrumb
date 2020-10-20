(ns deep-walking-macro.core3)
;; int의 갯수를 카운트 할 것이다.

(defmulti parse-item (fn [form ctx]
                       (cond
                         (seq? form) :seq
                         (integer? form) :int
                         (symbol? form) :symbol
                         (nil? form) :nil)))

(defmulti parse-sexpr (fn [[sym & rest] ctx]
                        sym))

(defmethod parse-sexpr 'if
  [[_ test then else] ctx]
  {:type :if
   :test (parse-item test ctx)
   :then (parse-item then ctx)
   :else (parse-item else ctx)})

(defmethod parse-sexpr 'do
  [[_ & body] ctx]
  {:type :do
   :body (doall (map (fn [x] (parse-item x ctx))
                     body))})

(defmethod parse-sexpr :default
  [[f & body] ctx]
  {:type :call
   :fn (parse-item f ctx) ;; 함수를 파싱하고 
   :args (doall (map (fn [x] (parse-item x ctx)) ;; 함수의 매개변수를 파싱함.
                     body))})

(defmethod parse-item :seq
  [form ctx]
  (let [form (macroexpand form)]
    (parse-sexpr form ctx)))

(defmethod parse-item :int
  [form ctx]
  (swap! ctx inc)
  {:type :int
   :value form})

(defmethod parse-item :symbol
  [form ctx]
  {:type :symbol
   :value form})

(defmethod parse-item :nil
  [form ctx]
  {:type :nil})


#_(let [a (atom 0)] (parse-item '(when t 42) a) (println @a))
;; 0이 나오는 건 이상하다.

#_(def a (atom 0))
#_(parse-item '(when t 42) a)
#_@a
;; 이건 된다...

;; 안되는 이유는 parse-sexpr 에서 map을 사용하는 부분에서 문제가 있는 듯 하다.
;; map을 사용하면(함수니까) parse-item 이 확장된 녀석을 호출할 것이다.
;; 그런데 map을 print하는 순간에 확장이 될 것이지만, 출력을 하는 곳이 없으므로 확장도 되지 않는다.
;; we are using map here that doesn't actually get expanded until we end up printing out the output
;; of this parse item it's never being printed therefore it's never being expanded. there you go

;; so let's kind of force this here 'doall'
(let [a (atom 0)] (parse-item '(when t 42) a) (println @a))
;; 이제 된다.

;; 더 나아가보자.
(let [a (atom 0)] (parse-item '(when t (do (do 4 3 (do 5 32))) 43) a) (println @a))
;; 5가 나온다.

;; next
#_(resolve '+) ;; => #'clojure.core/+
;; 이 resolve로 우리가 핸들링하지 않은 것들을 사용할 수 있을 것 같다.
;; 함수를 파싱하자.
(parse-item '(+ 2 4) (atom 0)) ;; => {:type :call, :fn {:type :symbol, :value +}, :args ({:type :int, :value 2} {:type :int, :value 4})}

(parse-item '((comp pos? +) 2 3) (atom 0))
#_{:type :call, :fn {:type :call, :fn {:type :symbol, :value comp}, :args ({:type :symbol, :value pos?} {:type :symbol\
, :value +})}, :args ({:type :int, :value 2} {:type :int, :value 3})}

;; 와 이런거로... 코드를 파싱해서 맘대로 가지고 놀 수 있다.
;; 다른 것도 해보자.

#_(defmacro to-ast [form]
  (parse-item form (atom 0)))

#_(to-ast (+ 1 2))
#_{:type :call, :fn {:type :symbol, :value #function[clojure.core/+]}, :args nil}

#_(to-ast (when x (if (< y 100) y-less y-greater)))
;; Unable to resolve symbol: x in this context
;; we're returning a map and that map is used in place of the what was before.
;; it replaces it and then evaluates it.
;; so what we really want to do and
;; we can see this once again `macroexpand`
#_(macroexpand '(to-ast (when x (if (< y 100) y-less y-greater))))

#_{:type :if,
 :test {:type :symbol, :value x},
 :then {:type :do,
        :body ({:type :if,
                :test {:type :call,
                       :fn {:type :symbol, :value <},
                       :args ({:type :symbol, :value y}
                              {:type :int, :value 100})},
                :then {:type :symbol, :value y-less},
                :else {:type :symbol, :value y-greater}})},
   :else {:type :nil}}
;; 여기서 보면 x가 있는데 이건 map이라는 함수 때문에 평가가 되어서 리턴 되어야 할 녀석이다.
;; 그런데 x라는 존재는 없다.
;; 일단 우리는 이 파싱한 데이터를 문자열로 바꾸는 일을 해야 겠다.

(defmacro to-ast [form]
  (pr-str (parse-item form (atom 0))))

(macroexpand '(to-ast (when x (if (< y 100) y-less y-greater))))
#_"{:type :if, :test {:type :symbol, :value x}, :then {:type :do, :body ({:type :if, :test {:type :call, :fn {:type :s\
ymbol, :value <}, :args ({:type :symbol, :value y} {:type :int, :value 100})}, :then {:type :symbol, :value y-less}, :e\
lse {:type :symbol, :value y-greater}})}, :else {:type :nil}}"

;; what the `go` macro does and we'll look at this later in our next video.
;; where the `go` macro does is does something pretty close to this
;; it emits a weird ast instead of what you kind of see here. nested hash map.
;; what's called SSA format. it taks that does some transformations on it
;; and then turns it back into clojure code
;; you can see how we can actually take this hash-map here

;; we built a macro that reads in the form to the ast and spits it back out.
