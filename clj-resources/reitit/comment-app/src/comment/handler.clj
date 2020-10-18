(ns comment.handler
  (:require [reitit.core :as r]
            [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [ring.adapter.jetty :as jetty]))

;; how to run server START
(def routes
  [["/ping"
    {:get {:swagger {:tags ["test"]}
           :handler (fn [req] {:status 200 :body "ok"})}}]
   ["/swagger.json"
    {:get {:no-doc true
           :swagger {:info {:title "Comment System API"
                            :description "Comment System API"}}
           :handler (swagger/create-swagger-handler)}}]
   ]) 

;; m/instance를 쓰면 아래처럼 썼던 코드는 다시 줄어듬.
#_["/swagger.json"
 {:get {:handler (fn [req] (let [handler (swagger/create-swagger-handler)
                                 response (handler req)]
                             response
                             ))}}]
(def router
  (ring/router routes
               {:data {:muuntaja m/instance ;; 이걸쓰면 
                       :middleware [muuntaja/format-middleware
                                    ;; muuntaja/format-negotiate-middleware
                                    ;; muuntaja/format-request-middleware
                                    ;; muuntaja/format-response-middleware
                                    ]}}))

(def app
  (ring/ring-handler router
                     (ring/routes
                      (swagger-ui/create-swagger-ui-handler
                       {:path "/"}))))

(defn start []
  (jetty/run-jetty #'app {:port 3000 :join? false})
  (println "server running on 3000"))

;; 이렇게 실행되는거랑 같다는 말임.
(comment
  (app {:request-method :get :uri "/ping"})
  (start))
;; how to run server END


