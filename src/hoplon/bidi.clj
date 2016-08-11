(ns hoplon.bidi
  (:require [hoplon.core]
            [bidi.schema]
            [schema.core]))

(defmacro route-tpl
  "Provides templating based on bidi routes.
   Validates routes agains bidi schema, uses case-tpl signature.
   Cases are matched against route handler."
  [routes & clauses]
  (assert (even? (count clauses)))
    `(let []
      (validate ~routes)
      (hoplon.core/case-tpl (wrap-route ~routes *route*) ~@clauses)))
