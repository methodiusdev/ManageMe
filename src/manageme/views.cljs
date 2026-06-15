(ns manageme.views
  (:require [manageme.state :as state]))

(defn project-form []
  [:div
   [:h3 "Add new project: "]
   [:input {:type "text" :placeholder "Name..."}]
   [:textarea {:placeholder "Description..."}]
   [:button "Save"]])

(defn project-list []
  (let [projects (:projects @state/app-state)]
    [:div
     [:h3 "Projects"]
     (if (empty? projects)
       [:p "There are no projects avaliable. Create one!"]
       [:ul
        (for [project projects]
          [:li {:key (:id project)}
           [:strong (:title project)] " - " (:description project)])])]))

(defn main-view []
  [:div {:syle {:max-width "800px" :margin "0 auto" :padding "20px"}}
   [:h1 "ManageMe - powered by Clojure"]
   [:hr]
   [project-form]
   [:hr]
   [project-list]])
