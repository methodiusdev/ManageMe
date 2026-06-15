(ns manageme.api
  (:require [manageme.storage :as s])
  (:require-macros [manageme.macros :refer [define-endpoint]]))

;;; Projects

;; GET /projects
(define-endpoint http-get-projects []
  (s/get-local-storage))
