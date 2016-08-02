(ns hoplon.bidi
  (:require [bidi.schema :refer [RoutePair]]
            [schema.core :as schema]))

(defmacro route-tpl
  "Provides templating based on bidi routes.
   Validates routes agains bidi schema, uses case-tpl signature.
   Cases are matched against route handler."
  [routes & clauses]
  (assert (even? (count clauses)))
    `(let []
      (schema/validate RoutePair ~routes)
      (hoplon.core/case-tpl (wrap-route ~routes *route*) ~@clauses)))
