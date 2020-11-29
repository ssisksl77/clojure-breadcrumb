(ns web.main-page)

(defn- default-handler [{::keys [acc]}]
  {:status 200
   :body (conj acc :default-handler)})

(def route-data
  ["/main" {:get default-handler}])
