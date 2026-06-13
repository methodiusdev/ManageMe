(ns manageme.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [manageme.state :as state]))

(defonce react-root
  (rdom/create-root (.getElementById js/document "app")))

(defn app []
  [:div
   [:h1 "ManageMe - powered by Clojure"]
   [:p (str "Total projects: " (count (:projects @state/app-state)))]])

(defn ^:dev/after-load reload []
  (rdom/render react-root [app]))

(defn init []
  (reload))
