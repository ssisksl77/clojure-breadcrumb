(ns persistence.db
  (:require [persistence.mongo :as mongo]))

#_(mongo/save-user {:id "001" :name "younghwan"})
#_(mongo/find-user "001")

(defn save-user [id name]
  (mongo/save-user {:id id :name name}))

(defn find-user [id]
  (mongo/find-user id))
