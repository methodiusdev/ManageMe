(ns manageme.state
  (:require [reagent.core :as r]
            [manageme.api :as api]))

(defonce app-state
  (r/atom {:projects []
           :loading? false
           :error nil}))

(defn- handle-request! [promise-t success-fn error-msg]
  (swap! app-state assoc :loading? true :error nil)
  (-> promise-t
      (.then (fn [data]
               (success-fn data)
               (swap! app-state assoc :loading? false)))
      (.catch (fn [error]
                (.error js/console error)
                (swap! app-state assoc :error error-msg :loading? false)))))

(defn fetch-projects! []
  (handle-request!
   (api/http-get-projects)
   (fn [data] (swap! app-state assoc :projects data))
   "Error fetching data."))

(defn add-project! [project-fields]
  (handle-request!
   (api/http-post-projects project-fields)
   (fn [new-project] (swap! app-state update :projects conj new-project))
   "Error creating project."))

(defn update-project! [id updated-fields]
  (handle-request!
   (api/http-patch-project id updated-fields)
   (fn [updated-project]
     (swap! app-state update :projects
            (fn [projects]
              (mapv #(if (= (:id %) id) updated-project %) projects))))
   "Error updating project."))

(defn delete-project! [id]
  (handle-request!
   (api/http-delete-project id)
   (fn [_success-payload]
     (swap! app-state update :projects
            (fn [projects]
              (vec (remove #(= (:id %) id) projects)))))
   "Error deleting project."))

