(ns manageme.state
  (:require [reagent.core :as r]
            [manageme.storage :as storage]))

(defonce app-state
  (r/atom {:projects (storage/get-projects)}))

(add-watch app-state :storage-watcher
           (fn [_key _atom _old-state new-state]
             (storage/save-projects (:projects new-state))))

(defn add-project! [project]
  (let [new-project (assoc project :id (str (random-uuid)))]
    (swap! app-state update :projects conj new-project)))

(defn delete-project! [project-id]
  (swap! app-state update :projects
         (fn [projects] (vec (remove #(= (:id %) project-id) projects)))))

(defn update-project! [project-id updated-fields]
  (swap! app-state update :projects
         (fn [projects]
           (mapv #(if (= (:id %) project-id) (merge % updated-fields) %)
                 projects))))
