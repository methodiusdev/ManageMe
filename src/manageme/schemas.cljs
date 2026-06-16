(ns manageme.schemas
  (:require [malli.core :as m]
            [malli.error :as me]))

(def PostProjectSchema
  [:map
   [:title [:string {:min 3 :max 50}]]
   [:description {:optional true} [:string {:min 0 :max 500}]]])

(defn validate-post-project [project]
  (if (m/validate PostProjectSchema project)
    {:valid? true}
    {:valid? false
     :error (me/humanize (m/explain PostProjectSchema project))}))
