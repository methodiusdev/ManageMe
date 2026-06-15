(ns manageme.storage
  (:require [cljs.reader :refer [read-string]]))

(def storage-key "manageme-projects")

(defn serialize [data]
  "Returns EDN string from Clojure `data`"
  (pr-str data))

(defn deserialize [edn-string]
  "Returns Clojure data structures from `edn-string`"
  (when edn-string
    (read-string edn-string)))

(defn get-projects []
  (or (deserialize (.getItem js/localStorage storage-key)) []))

(defn save-projects [projects]
  (.setItem js/localStorage storage-key (serialize projects)))
