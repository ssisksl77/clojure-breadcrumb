(ns nam-blog.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [ring.util.response :refer [response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def http-status-codes
  {:OK 200
   :NOT-FOUND 404
   :MEH 444})

(defn scores
  "Returns the current scoreboard as JSON"
  [_]
  (println "Calling the scoreboard handler..."))


(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/notes" [] "Notes")
  (GET "/scores" [] scores)
  (route/not-found "<h1>Page Not Found</h1>"))

(defn hello-world
  "A simple hello world handler"
  [_]
  (response "Hello Clojure World. from ring response."))

(def app
  (wrap-defaults #'app-routes site-defaults))



(defn scores
  "Returns the current scoreboard as JSON"
  [_]
  (println "Calling the scoreboard handler...")
  {:headers {"Content-type" "application/json"}
   :status  (:OK http-status-codes)
   :body    (json/write-str {:players
                             [{:name "johnny-be-doomed" :high-score 1000001}
                              {:name "jenny-jetpack" :high-score 23452345}]})})
