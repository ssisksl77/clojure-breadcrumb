(ns persistence.mongo
  (:require [monger.core :as mg]
            [monger.collection :as mc]))
;; ref http://clojuremongodb.info/articles/getting_started.html

(def uri "mongodb://test:test@127.0.0.1:27017")
(def conn (mg/connect))

(defn save-user [user]
  (let [db (mg/get-db conn "local")]
    (mc/insert-and-return db "documents" user)))

(defn find-user [id]
  (let [db (mg/get-db conn "local")]
    (mc/find-one-as-map db "documents" {:id id})))


(comment 
  (let [uri "mongodb://test:test@127.0.0.1:27017"
               {:keys [conn db]} (mg/connect-via-uri uri)])
  
  (mg/disconnect conn))
