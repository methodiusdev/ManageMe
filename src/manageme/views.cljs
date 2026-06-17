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

;; NEW: An inline, minimalist card to create new projects without modals
(defn new-project-card []
  (let [is-creating? (r/atom false)
        title (r/atom "")
        desc (r/atom "")]
    (fn []
      [:div {:class (str "rounded-xl border p-6 flex flex-col
                          justify-between min-h-[220px] transition-all"
                         (if @is-creating?
                           "bg-white border-emerald-500 ring-1
                            ring-emerald-500/20 shadow-sm"
                           "bg-gray-50/30 border-dashed border-gray-200
                            hover:border-emerald-300 hover:bg-white
                            cursor-pointer shadow-2xs"))
             :on-click #(when-not @is-creating? (reset! is-creating? true))}
       (if @is-creating?
         [:div {:class "space-y-3 w-full"}
          [:h4 {:class "text-xs font-bold text-emerald-700 uppercase
                        tracking-wider"} "New Project"]
          [:div {:class "space-y-1"}
           [:label {:class "text-2xs font-bold text-gray-400 uppercase
                            tracking-wider block"} "Title"]
           [:input {:type "text"
                    :placeholder "Title..."
                    :class "w-full border border-gray-200 rounded-xl px-3 py-1.5
                            text-sm font-medium focus:outline-none
                            focus:border-emerald-500 transition-colors"
                    :value @title
                    :on-change #(reset! title (-> % .-target .-value))}]]
          [:div {:class "space-y-1"}
           [:label {:class "text-2xs font-bold text-gray-400 uppercase
                            tracking-wider block"} "Description"]
           [:textarea {:rows 2
                       :placeholder "Description..."
                       :class "w-full border border-gray-200 rounded-xl px-3
                               py-1.5 text-sm focus:outline-none
                               focus:border-emerald-500 transition-colors
                               resize-none leading-relaxed"
                       :value @desc
                       :on-change #(reset! desc (-> % .-target .-value))}]]]
         [:div {:class "flex flex-col items-center
                        justify-center flex-1 py-6 text-center"}
          [:span {:class "text-2xl mb-1 font-bold text-gray-700"} "+"]
          [:p {:class "text-sm font-bold text-gray-700"} "Add Project Card"]])
       (when @is-creating?
         [:div {:class "mt-4 pt-3 border-t border-gray-50 flex justify-end
                        space-x-3 text-xs font-semibold"}
          [:button {:class "text-gray-400 hover:text-gray-600
                            transition-colors px-2 py-1"
                    :on-click (fn [e]
                                ;; Prevents closing event bubble conflict
                                (.stopPropagation e)
                                (reset! is-creating? false))}
           "Cancel"]
          [:button {:class "bg-emerald-600 hover:bg-emerald-700 text-white px-3
                            py-1.5 rounded-xl transition-colors shadow-xs"
                    :on-click (fn [e]
                                (.stopPropagation e)
                                (when-not (empty? @title)
                                  (state/add-project! {:title @title
                                                       :description @desc})
                                  (reset! title "")
                                  (reset! desc "")
                                  (reset! is-creating? false)))}
           "Create"]])])))

(defn project-card [{:keys [id title description]}]
  (let [expanded? (r/atom false)
        is-editing? (r/atom false)
        local-title (r/atom title)
        local-desc (r/atom description)]
    (fn [{:keys [id title description]}]
      [:div {:class (str "bg-white rounded-xl border shadow-sm transition-all
                          p-6 flex flex-col justify-between"
                         (if @is-editing?
                           "border-emerald-500 ring-1 ring-emerald-500/20"
                           "border-gray-200 hover:border-emerald-200"))}
       (if @is-editing?
         [:div {:class "space-y-3"}
          [:h4 {:class "text-xs font-bold text-emerald-700
                        uppercase tracking-wider"} "Edit"]
          [:div {:class "space-y-1"}
           [:label {:class "text-2xs font-bold text-gray-400 uppercase
                            tracking-wider block"} "Title"]
           [:input {:type "text"
                    :class "w-full border border-gray-200 rounded-xl px-3 py-1.5
                            text-sm font-medium focus:outline-none
                            focus:border-emerald-500 transition-colors"
                    :value @local-title
                    :on-change #(reset! local-title (-> % .-target .-value))}]]
          [:div {:class "space-y-1"}
           [:label {:class "text-2xs font-bold text-gray-400 uppercase
                            tracking-wider block"} "Description"]
           [:textarea {:rows 3
                       :class "w-full border border-gray-200 rounded-xl px-3
                               py-1.5 text-sm focus:outline-none
                               focus:border-emerald-500 transition-colors
                               resize-none leading-relaxed"
                       :value @local-desc
                       :on-change #(reset! local-desc (-> % .-target .-value))}]]]
         [:div
          [:div {:class "flex justify-between items-start mb-2"}
           [:h3 {:class "text-base font-bold text-gray-900 tracking-tight"}
            title]
           [:span {:class "px-2 py-0.5 text-xs font-semibold rounded-md
                           bg-emerald-50 text-emerald-700
                           border border-emerald-100"}
            "Active"]]
          [:p {:class (str "text-gray-600 text-sm mt-2 leading-relaxed "
                           (when-not @expanded? "line-clamp-3"))}
           description]
          [:button {:class "text-xs font-bold text-emerald-600
                          hover:text-emerald-700 mt-2 transition-colors block"
                    :on-click #(swap! expanded? not)}
           (if @expanded? "Show Less ↑" "Show More ↓")]])
       [:div {:class "mt-6 pt-3 border-t border-gray-50
                      flex justify-end space-x-4"}
        (if @is-editing?
          [:<>
           [:button {:class "text-xs text-gray-400 hover:text-gray-600
                             font-semibold transition-colors"
                     :on-click (fn []
                                 (reset! local-title title)
                                 (reset! local-desc description)
                                 (reset! is-editing? false))}
            "Cancel"]
           [:button {:class "text-xs text-emerald-600 hover:text-emerald-700
                             font-bold transition-colors"
                     :on-click (fn []
                                 (state/update-project!
                                  id {:title @local-title
                                      :description @local-desc})
                                 (reset! is-editing? false))}
            "Save"]]
          [:<>
           [:button {:class "text-xs text-gray-400 hover:text-emerald-600
                             font-semibold transition-colors"
                     :on-click #(reset! is-editing? true)}
            "Edit"]
           [:button {:class "text-xs text-red-400 hover:text-red-600
                             font-semibold transition-colors"
                     :on-click #(state/delete-project! id)}
            "Delete"]])]])))

(defn main-view []
  (let [{:keys [projects loading? error]} @state/app-state]
    [:div {:class "min-h-screen bg-gray-50/50 flex flex-col font-sans antialiased"}
     [header-component]
     [:main {:class "p-6 max-w-5xl w-full mx-auto flex-1 mt-4"}
      [:div {:class "flex flex-col sm:flex-row sm:justify-between
                           sm:items-center gap-4 mb-8 pb-4 border-b border-gray-100"}
       [:h2 {:class "text-lg font-bold text-gray-900 tracking-tight"} "All Projects"]]
      [:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"}
       [new-project-card]
       (for [project projects]
         ^{:key (:id project)} [project-card project])]]]))
