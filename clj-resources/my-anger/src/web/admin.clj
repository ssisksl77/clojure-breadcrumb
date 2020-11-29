(ns web.admin)

(defn- default-handler [{:keys [acc]}]
  {:status 200
   :body {:data "Hello admin"}})

(def route-data
  ["/admin" {:roles #{:admin}}
   ["/db" {:delete {:handler default-handler}}]])


