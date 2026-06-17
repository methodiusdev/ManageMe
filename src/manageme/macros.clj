(ns manageme.macros)

(defmacro define-endpoint [name args & body]
  "Defines endpoint. Executes `body` after some delay using JS Promise."
  `(defn ~name ~args
     (manageme.storage/with-delay (fn [] ~@body))))

(defmacro define-schema [name schema]
  "Defines and validates schema."
  `(defn ~name [obj#]
     (if (malli.core/validate ~schema obj#)
       {:valid? true}
       {:valid? false
        :error (malli.error/humanize
                (malli.core/explain ~schema obj#))})))
