(ns manageme.schemas
  (:require [malli.core :as m]
            [malli.error :as me])
  (:require-macros [manageme.macros :refer [define-schema]]))

(define-schema post-project
  [:map
   [:title [:string {:min 3 :max 50}]]
   [:description {:optional true} [:string {:min 0 :max 500}]]])

(define-schema patch-project
  [:map
   [:title {:optional true} [:string {:min 3 :max 50}]]
   [:description {:optional true} [:string {:min 0 :max 500}]]])


