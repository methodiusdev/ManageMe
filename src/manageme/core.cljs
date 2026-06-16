(ns manageme.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [manageme.state :as s]
            [manageme.views :as v]))

(defonce react-root
  (rdom/create-root (.getElementById js/document "app")))

(defn app []
  [v/main-view])

(defn ^:dev/after-load reload []
  (rdom/render react-root [app]))

(defn init []
  (s/fetch-projects!)
  (reload))
