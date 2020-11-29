(ns comment.system
  (:require [integrant.core :as ig]
            [comment.handler :as handler]
            [ring.adapter.jetty :as jetty]))

(def system-config
  {:comment/jetty {:handler (ig/ref :comment/handler)
                   :port 3000}
   :comment/handler {:db (ig/ref :comment/sqlite)}
   :comment/sqlite nil})

(defmethod ig/init-key :comment/jetty [_ {:keys [handler port]}]
  (println "server running on port 3000")
  (jetty/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key :comment/handler [_ {:keys [db]}]
  (handler/create-app db))

(defmethod ig/init-key :comment/sqlite [_ _]
  nil)

(defmethod ig/halt-key! :comment/jetty [_ jetty]
  (.stop jetty))

(defn -main []
  (ig/init system-config))

(comment
  (defn start [])
  (def system (ig/init system-config))
  (ig/halt! system))


