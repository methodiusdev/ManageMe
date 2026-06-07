(ns manageme.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]))

(defonce react-root
  (rdom/create-root (.getElementById js/document "app")))

(defn app []
  [:div
   [:h1 "ManageMe - powered by Clojure"]])

(defn ^:dev/after-load reload []
  (rdom/render react-root [app]))

(defn init []
  (reload))
