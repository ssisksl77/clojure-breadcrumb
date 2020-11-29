(ns persistence.db)

(def db (atom {}))

(defn save-user [id name]
  (swap! db assoc (keyword id) name))

(defn find-user [id]
  ((keyword id) @db))
