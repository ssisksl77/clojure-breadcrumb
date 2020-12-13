(ns test.core
  (:require [hikari-cp.core :as hikari]
            ))


(def datasource-options {:auto-commit        true
                         :read-only          false
                         :connection-timeout 30000
                         :validation-timeout 5000
                         :idle-timeout       600000
                         :max-lifetime       1800000
                         :minimum-idle       10
                         :maximum-pool-size  10
                         :pool-name          "db-pool"
                         :adapter            "postgresql"
                         :username           "yhnam"
                         :password           "yhnam"
                         :database-name      "firstdb"
                         :server-name        "localhost"
                         :port-number        5432
                         :register-mbeans    false})


(defonce datasource
  (delay (make-da)))


(require '[clojure.java.jdbc :as jdbc])
(sql/db-do-commands "postgresql://localhost:5432/firstdb"
                    )
