(ns manageme.storage
  (:require [cljs.reader :refer [read-string]]))

(def storage-key "manageme-projects")

(defn- serialize [data]
  "Returns EDN string from Clojure `data`"
  (pr-str data))

(defn- deserialize [edn-string]
  "Returns Clojure data structures from `edn-string`"
  (when edn-string (read-string edn-string)))

(defn get-local-storage []
  (or (deserialize (.getItem js/localStorage storage-key)) []))

(defn set-local-storage [projects]
  (.setItem js/localStorage storage-key (serialize projects)))

(defn with-delay [thunk]
  (js/Promise.
   (fn [resolve _reject]
     (js/setTimeout (fn [] (resolve (thunk))) 200))))
