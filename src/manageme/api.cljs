(ns manageme.api
  (:require [manageme.storage :as s]
            [manageme.schemas :as sch])
  (:require-macros [manageme.macros :refer [define-endpoint]]))

;;; Projects

;; GET /projects
(define-endpoint http-get-projects []
  (s/get-local-storage))

;; POST /projects
(define-endpoint http-post-projects [project-fields]
  (let [validation (sch/validate-post-project project-fields)]
    (if (:valid? validation)
      (let [projects (s/get-local-storage)
            new-project (assoc project-fields :id (str (js/crypto.randomUUID)))
            new-projects (conj projects new-project)]
        (s/set-local-storage new-projects)
        new-project)
      (throw (js/Error. (str "Validation error: " (:error validation)))))))
