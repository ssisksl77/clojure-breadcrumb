(ns lambda-calculus.pred
  (:require [lambda-calculus.numerals :refer :all]
            [lambda-calculus.lambda :refer :all]
            [lambda-calculus.booleans :refer :all]))

;; pred 는 n f x 3개의 매개변수를 받는다.
;; n은 1을 뺄 숫자.
;; 여기서 f

(defmacro L
  [args & body]
  `(fn [~args] ~@body))
;;         S      arg1          arg2   arg3
;; PRED := λnfx.n (λgh.h (g f)) (λu.x) (λu.u)
(def _pred
  (L n (L f (L x (((n (L g (L h (h (g f))))) (L u x)) (L u u))))))
;; 일단 람다이긴 한데 이미 함수가 적용이 되기 직전이다.
;; 일단 (L f (L x 는 pred 를 하면서 숫자를 형태를 다시 씌우기 위함인듯 하다.
;; 첫번째 매개변수를 이용하면 다 벗겨지는 것 같다.
;; 두번째 매개변수는 뭘 하는 거지?
;; 내 생각에 두번째 인자 (L u x)는
;; x가 세번째 인자를 받는 경우인 것을 보아서. identity이거나 한번더 실행되는 경우를
;; 나누기 위함이 아닌가 싶다. 이건 나중에 더 알아봐야 할듯.
;; 세번쨰는 일단 아이덴티티임.


;; n 은 숫자를 말함
;; 첫번째 인자는 S(SUCC)를 말하는 듯하다.
;; Lg.Lh.h (g f) 로 쓸 수 있다. 
;;- 이것은 가장 바깥 쪽 람다만 n번 적용되는 요소를 반영한다.
;; 이 함수가 하는 일은 g까지 누적된 결과를 가져와
;; 하나의 인수를 갖는 새로운 함수를 반환하는 것이다.
;; 이 인수는 이미 적용된 f를 g에 적용하는 듯 함.

;; 0이 아닌 숫자 Sn을 보자.
;; n은 g에 바인딩 된 인수에 해당한다.
;; 따라서 f와 x는 외부 범위에서 바인딩되어 있음을 기억하자.
;; 우린 다음처럼 셀 수 있다.
;; Lu.x
;; Lh.h ((Lu.x f)
;; Lh'.h' ((Lh.h ((Lu.x) f)) f) ...
;; Performing the obvious reductions, we get this
;; Lu.x
;; Lh.h x
;; Lh'.h' (f x)
;;
;; 여기서 패턴은 함수가 한 레이어 "안쪽"으로 전달된다는 것이다.
;; 어느시점에서 S는 그것을 적용하고 Z는 그것을 무시함. 여기서 Z는 zero임.
;; 그래서 우리는 가장 바깥 쪽을 제외한 각 S에 대해 f를 한 번 적용함.

;; 3번째 매개변수는 그냥 identity function임.
;; 이녀석은 성실하게 바깥에 있는 S에 의해 적용됨. 그렇게 최종 결과를 반환함.
;; f는 n에 해당하는 S 레이어의 수보다 1회 적게 적용됨.


;; 예시를 보자.

;; Let's take a concrete example for Pred 3 = 2:
;; Consider expression: n (λgh.h (g f)) (λu.x). Let K = (λgh.h (g f))
;; 0인 경우 0 = λfx.x따라서 (λfx.x)(λgh.h(gf)에 베타 리덕션을 하면
;; 위에 베타리덕션을 하는 형태는
;; pred에서 


;; 이제 대충 알겠음.
#_(def pred
  (L n (L f (L x (((n (L g (L h (h (g f))))) (L u x)) (L u u))))))
;; 일단 (L n ...) 은 숫자를 기다리는 거임.
;; 하지만 일단 n을 받기 시작하면 그 안에서 L.u x와 L.u u가 인자로  바로 실행됨.
;; 바로 실행되는 녀석은
;; (n (L g (L h (h (h (g f))))) (L u x)) (L u u)
;; 일단 n에 0을 넣어보자.
;; ((L f (L x x)) (L g (L h (h (g f))))
;; 일단 뭘 받던 (L x x)가 되는 거 같다.
;; (L x x)
;; 그러면 (L x x)는 0이다.

;; 1로 해보자. we apply K for 1 times:
(L f (L x (f x))) (L u x)
(L x ((L u x) x))
(L x x)


;; 뭐지 2로 해보자.
;; 2 = (L f (L x (f (f x))))
;; (2 (L g (L h (h (g f)))))
;; (L f (L x (f (f x)))) (L g (L h (h (g f)))))
(L x ((L g (L h (h (g f)))) (L g (L h (h (g f)))) x))
(L x ((L g (L h (h (g f)))) (L h (h (x f)))))
(L x (L h (h (L (h (h (x f)))) f)))
(L x (L h (h (L (h (h (x f)))) f)))
