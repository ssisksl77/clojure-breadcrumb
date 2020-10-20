(ns deep-walking-macro.core2)

(defmulti parse-item (fn [form ctx]
                      (cond
                        (seq? form) :seq
                        (integer? form) :int
                        (symbol? form) :symbol
                        (nil? form) :nil)))

(defmulti parse-sexpr (fn [[sym & rest] ctx]
                        sym))

;; 1차
(defmethod parse-sexpr 'if
  [[_ test then else] ctx]
  {:type :if
   :test (parse-item test ctx)
   :then (parse-item then ctx)
   :else (parse-item else ctx)})

#_(defmethod parse-item :seq
  [form ctx]
  (parse-sexpr form ctx))

; 2차macroexpand, when 접목해보기
(defmethod parse-item :seq
  [form ctx]
  (let [form (macroexpand form)]
    (parse-sexpr form ctx)))

(defmethod parse-item :int
  [form ctx]
  {:type :int
   :value form})

(defmethod parse-item :symbol
  [form ctx]
  {:type :symbol
   :value form})


#_(parse-item 42 nil)
#_(parse-item 's nil)
#_(parse-item '(if x 42 41) nil)

;; 현재 when은 없는 것 같다. 왜지 when은 if 로 변경되는데... 이제 parse-item을 변경해보자.
#_(parse-item '(when x 42 41) nil) ;; No method in multimethod 'parse-sexpr' for dispatch value: when
;; 2차 parse-item을 이용하지만 No method in multimethod 'parse-sexpr' for dispatch value: do 가 뜬다.
;; 아 do를 추가해야할 것 같다.

(defmethod parse-sexpr 'do
  [[_ & body] ctx]
  {:type :do
   :body (map (fn [x] (parse-item x ctx))
              body)})
;; 그런데 when은 nil을 else문제 리턴한다. 그러므로 nil 도 파싱해야함.
(defmethod parse-item :nil
  [form ctx]
  {:type :nil})
;; 다시 해보자
#_(parse-item '(when x 42 41) nil) ;; No method in multimethod 'parse-item' for dispatch value: null
;; 이러면 defmethod를 수정하면 repl을 다시 실행해야함.

(parse-item '(when x 42 41) nil)
#_{:type :if, :test {:type :symbol, :value x}, :then {:type :do, :body ({:type :int, :value 42} {:type :int, :value 41\
})}, :else {:type :nil}}
;; 좋아...
(parse-item '(when t 42 41) nil)
#_{:type :if, :test {:type :symbol, :value t}, :then {:type :do, :body ({:type :int, :value 42} {:type :int, :value 41\
})}, :else {:type :nil}}

;; 좀 더 재밌는 것을 해보자.
;; 매크로 안에 숫자가 얼마나 많이 있는지??
;; 그 내용은 core4.clj에서 하도록 하자.
;; core3.clj는 코드 정리.
