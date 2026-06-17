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

(defn project-card [{:keys [id title description]}]
  (let [expanded? (r/atom false)]
    (fn [{:keys [id title description]}]
      [:div {:class "bg-white rounded-xl border border-gray-200 shadow-sm
                     hover:shadow-md hover:border-emerald-200 transition-all p-6
                     flex flex-col justify-between"}
       [:div
        [:div {:class "flex justify-between items-start mb-2"}
         [:h3 {:class "text-base font-bold text-gray-900 tracking-tight"} title]
         [:span {:class "px-2 py-0.5 text-xs font-semibold rounded-md
                         bg-emerald-50 text-emerald-700 border border-emerald-100"}
          "Active"]]
        [:p {:class (str "text-gray-600 text-sm mt-2 leading-relaxed "
                         (when-not @expanded? "line-clamp-3"))}
         description]
        [:button {:class "text-xs font-bold text-emerald-600
                          hover:text-emerald-700 mt-2 transition-colors block"
                  :on-click #(swap! expanded? not)}
         (if @expanded? "Show Less ↑" "Show More ↓")]]
       [:div {:class "mt-6 pt-3 border-t border-gray-50
                      flex justify-end space-x-4"}
        [:button {:class "text-xs text-gray-400 hover:text-emerald-600
                          font-semibold transition-colors"}
         "Edit"]
        [:button {:class "text-xs text-red-400 hover:text-red-600
                          font-semibold transition-colors"
                  :on-click #(state/delete-project! id)}
         "Delete"]]])))

(defn main-view []
  (let [{:keys [projects loading? error]} @state/app-state]
    [:div {:class "min-h-screen bg-gray-50/50 flex flex-col font-sans antialiased"}
     [header-component]
     [:main {:class "p-6 max-w-5xl w-full mx-auto flex-1 mt-4"}
      [:div {:class "flex flex-col sm:flex-row sm:justify-between
                           sm:items-center gap-4 mb-8 pb-4 border-b border-gray-100"}
       [:h2 {:class "text-lg font-bold text-gray-900 tracking-tight"} "All Projects"]
       [:button {:class "bg-emerald-600 hover:bg-emerald-700 text-white text-sm
                         font-semibold px-4 py-2 rounded-xl shadow-xs
                         transition-colors self-start sm:self-auto"
                 :on-click #(state/add-project! {:title "New Project Blueprint" :description "Spawned directly from your minimalistic, emerald-accented workspace dashboard."})}
        "+ New Project"]]
      [:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"}
       (for [project projects]
         ^{:key (:id project)} [project-card project])]]]))
