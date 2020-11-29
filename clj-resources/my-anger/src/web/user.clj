(ns web.user
  (:require [persistence.db :as db]))

(defn- get-user-handler [{{{:keys [id]} :query} :parameters}]
  
  {:status 200
   :body {:data (str "Hello " id)}})

(defn- save-user-handler [{{{:keys [id name]} :body} :parameters}]
  (db/save-user id name)
  {:status 200
   :body {:data (str "save id=" id)}})

(defn- get-user-by-id-handler [{{{:keys [id]} :path} :parameters}]
  {:status 200
   :body {:data (str "save id=" id ", name=" (db/find-user id))}})

(def route-data
  ["/user"
   ["" {:post {:parameters {:body {:id string?
                                   :name string?}}
               :handler save-user-handler}}]
   ["/no/:no" {:get {:summary "query user"
                     :parameters {:path {:no int?}}
                     :handler get-user-handler}}]
   ["/id/:id" {:get {:summary "find by id"
                     :parameters {:path {:id string?}}
                     :handler get-user-by-id-handler}}]
   ])

