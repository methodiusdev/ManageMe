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

;; (defn add-project! [project]
;;   (let [new-project (assoc project :id (str (random-uuid)))]
;;     (swap! app-state update :projects conj new-project)))

;; (defn delete-project! [project-id]
;;   (swap! app-state update :projects
;;          (fn [projects] (vec (remove #(= (:id %) project-id) projects)))))

;; (defn update-project! [project-id updated-fields]
;;   (swap! app-state update :projects
;;          (fn [projects]
;;            (mapv #(if (= (:id %) project-id) (merge % updated-fields) %)
;;                  projects))))
