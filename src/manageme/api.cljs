(ns manageme.api
  (:require [manageme.storage :as s]
            [manageme.schemas :as schemas])
  (:require-macros [manageme.macros :refer [define-endpoint]]))

;;; Projects

;; GET /projects
(define-endpoint http-get-projects []
  (s/get-local-storage))

;; POST /projects
(define-endpoint http-post-projects [project-fields]
  (let [validation (schemas/post-project project-fields)]
    (if (:valid? validation)
      (let [projects (s/get-local-storage)
            new-project (assoc project-fields :id (str (js/crypto.randomUUID)))
            new-projects (conj projects new-project)]
        (s/set-local-storage new-projects)
        new-project)
      (throw (js/Error. (str "Validation error: " (:error validation)))))))

;; PATCH /projects/:id
(define-endpoint http-patch-project [id updated-fields]
  (let [validation (schemas/patch-project updated-fields)]
    (if (:valid? validation)
      (let [projects (s/get-local-storage)
            project-exists? (some #(= (:id %) id) projects)]
        (if project-exists?
          (let [updated-list
                (mapv #(if (= (:id %) id) (merge % updated-fields) %) projects)
                updated-project (some #(when (= (:id %) id) %) updated-list)]
            (s/set-local-storage updated-list)
            updated-project)
          (throw (js/Error. (str "Resource not found: Project with ID " id " does not exist.")))))
      (throw (js/Error. (str "Validation error: " (:error validation)))))))

;; DELETE /projects/:id
(define-endpoint http-delete-project [id]
  (let [projects (s/get-local-storage)
        project-exists? (some #(= (:id %) id) projects)]
    (if project-exists?
      (let [updated-list (vec (remove #(= (:id %) id) projects))]
        (s/set-local-storage updated-list)
        {:id id :status "deleted"})
      (throw (js/Error.
              (str "Resource not found: Cannot delete non-existent project with ID " id))))))
