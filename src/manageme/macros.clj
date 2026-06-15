(ns manageme.macros)

(defmacro define-endpoint [name args & body]
  "Defines endpoint. Executes `body` after some delay using JS Promise."
  `(defn ~name ~args
     (manageme.storage/with-delay (fn [] ~@body))))
