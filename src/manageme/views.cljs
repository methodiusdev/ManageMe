(ns manageme.views
  (:require [reagent.core :as r]
            [manageme.state :as state]))

(defn header-component []
  [:header {:class "bg-white border-b border-gray-100 px-6 py-4 flex
                    justify-between items-center max-w-5xl mx-auto w-full"}
   [:h1 {:class "text-xl font-black text-emerald-800 tracking-tight
                 flex items-center space-x-2"}
    [:span "ManageMe -- Powered by Clojure"]]
   [:div {:class "flex items-center space-x-3"}
    [:span {:class "text-xs font-medium text-gray-400 bg-gray-50
                    border border-gray-100 px-2.5 py-1 rounded-md"} 
     "Kira's Workspace"]]])

(defn main-view []
  [:div {:class "min-h-screen bg-gray-50/50 flex flex-col font-sans antialiased"}
   [header-component]
   [:main {:class "p-6 max-w-5xl w-full mx-auto flex-1 mt-4"}
    [:div {:class "text-center py-12"}
     [:p {:class "text-sm text-gray-400 font-medium animate-pulse"} 
      "Workspace shell active. Awaiting grid wrapper..."]]]])
