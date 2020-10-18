(ns nam-blog.note)

(defn notes [n]
  (str "<ol>"
       (for [x (range n)]
           (note n))
       "</ol>"))

(defn note [n]
  (str  "<li>note" n "</li>"))
