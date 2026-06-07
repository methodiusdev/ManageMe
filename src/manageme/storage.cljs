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

(defn add-project [project]
  (let [current-projects (get-projects)
        new-project (assoc project :id (str (random-uuid)))
        new-projects (conj current-projects new-project)]
    (save-projects new-projects)))

(defn update-project [id updated-fields]
  (let [current-projects (get-projects)
        updated-projects (mapv (fn [project] (if (= (:id project) id)
                                               (merge project updated-fields)
                                               project))
                               current-projects)]
    (save-projects updated-projects)))

(defn delete-project [id]
  (let [current-projects (get-projects)
        updated-projects (vec (remove (fn [project] (= (:id project) id))
                                      current-projects))]
    (save-projects updated-projects)))
