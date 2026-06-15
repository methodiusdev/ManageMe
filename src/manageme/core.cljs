(ns manageme.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [manageme.views :as v]))

(defonce react-root
  (rdom/create-root (.getElementById js/document "app")))

(defn app []
  [v/main-view])

(defn ^:dev/after-load reload []
  (rdom/render react-root [app]))

(defn init []
  (reload))
