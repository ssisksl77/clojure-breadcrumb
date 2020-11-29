(ns web.handler
  (:require [clojure.set :as set]
            [reitit.core :as r]
            [reitit.coercion.spec]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.exception :as exception]
            [reitit.middleware :as middleware]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.dev.pretty :as pretty]
            [web.main-page :as main-page]
            [web.admin :as admin]
            [web.user :as user]
            [muuntaja.core :as m]
            ))


(defn wrap [handler id]
  (fn [request]
    (handler (update request ::acc (fnil conj []) id))))

;; middleware 를 데이터로 가지는것이 좋다.
(def wrap3
  {:name ::wrap3
   :description "Middleware that does things."
   :wrap wrap})

(defn handler [{::keys [acc]}]
  {:status 200, :body (conj acc :handler)})

(def app
  (ring/ring-handler
   (ring/router
    ["/api"
     ;;main-page/route-data
     ;;admin/route-data
     user/route-data]
    {:exception pretty/exception
     :data {:coercion reitit.coercion.spec/coercion
            :muuntaja m/instance
            :middleware [parameters/parameters-middleware
                         ;; content-negotiation
                         muuntaja/format-middleware
                         ;; exception handling
                         exception/exception-middleware
                         ;; coercing request parameters
                         rrc/coerce-request-middleware
                         ;; coercing response bodys
                         rrc/coerce-response-middleware
                         ]}})
   (ring/routes
     (ring/redirect-trailing-slash-handler)
     (ring/create-default-handler
      {:not-found (constantly {:status 404 :body "Not Found"})
       :method-not-allowed (constantly {:status 405 :body "Method Not Allowed"})
       :not-accepted (constantly {:status 406 :body "Not Accepted"})}))))

;; test
(app {:request-method :get, :uri "/api/user/no/1"})
(app {:request-method :get, :uri "/api/user/NAM"})
(app {:request-method :get, :uri "/api/user/id/A"})
(slurp (:body (app {:request-method :get, :uri "/api/user/id/A"})))
(slurp (:body (app {:request-method :post, :uri "/api/user", :body-params {:id "A" :name "younghwan"}})))

(defn my-test []
     (app {:request-method :post, :uri "/api/user", :body-params {:y 2}}))

#_(-> (r/router
      ["http://localhost:8080/api/user/{id}" ::user-by-id]
      {:syntax :bracket})
    (r/match-by-path "http://localhost:8080/api/user/123"))
