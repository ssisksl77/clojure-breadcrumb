(ns nam-blog.note)

(defn notes [n]
  (str "<ol>"
       (clojure.string/join "" (for [x (range n)]
                                 (note n)))
       "</ol>"))

(defn note [n]
  (str  "<li>note" n "</li>"))
