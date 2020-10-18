(ns comment.handler
  (:require [reitit.core :as r]
            [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.coercion.spec]
            [reitit.ring.coercion :as coercion]
            [muuntaja.core :as m]
            [ring.adapter.jetty :as jetty]))

(def ok (constantly {:status 200 :body "ok"}))

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
   ["/comments"
    {:swagger {:tags ["comments"]}}
    [""
     {:get {:summary "Get all comments"
            :handler ok}
      :post {:summary "Create a new comment"
             :parameters {:body {:name string? ;; spec 인듯..
                                 :slug string?
                                 :text string?
                                 :parent-comment-id int?}}
             :responses {200 {:body string?}}
             :handler ok}}]
    ["/:slug"
     {:get {:summary "Get comments by slug"
            :parameters {:path {:slug string?}}
            :handler ok}}]]
   ["/id/:id"
    {:put {:summary "Update a comment by the moderator"
           :parameters {:path {:id int?}} ;; path-parameters
           :handler ok}
     :delete {:summary "Delete a comment by the moderator"
              :handler ok}}]]) 

;; m/instance를 쓰면 아래처럼 썼던 코드는 다시 줄어듬.
#_["/swagger.json"
 {:get {:handler (fn [req] (let [handler (swagger/create-swagger-handler)
                                 response (handler req)]
                             response
                             ))}}]
(def router
  (ring/router routes
               {:data {:coercion reitit.coercion.spec/coercion
                       :muuntaja m/instance ;; 이걸쓰면
                       ;; middleware order TOP to BOTTOM
                       :middleware [swagger/swagger-feature
                                    ;; muuntaja/format-middleware
                                    muuntaja/format-negotiate-middleware
                                    muuntaja/format-response-middleware
                                    exception/exception-middleware
                                    muuntaja/format-request-middleware
                                    coercion/coerce-request-middleware
                                    coercion/coerce-response-middleware
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


