(ns degree9.bidi
  (:require [bidi.schema :refer [RoutePair]]
            [schema.core :as schema]
    #?@(:cljs [[cuerdas.core :as s]
               [bidi.bidi :as bidi]
               [hoplon.core :refer [route-cell]]]))
  #?(:cljs (:require-macros [degree9.bidi :refer [route-tpl]])))

#?(:cljs (def match-route bidi/match-route))

#?(:cljs (def ^:dynamic *route* (route-cell "")))

#?(:cljs (defn wrap-route [routes routec]
           (javelin.core/cell= (:handler (bidi/match-route routes (s/strip-prefix routec "#"))))))

#?(:cljs (defn active-route? [routes route]
           (javelin.core/cell= (= route (wrap-route routes *route*)))))

#?(:cljs (defn route!
           "Change URL hash, does not reload the page."
           [routes & routeopts]
           (aset js/window "location" "hash"
             (s/join ["#" (apply bidi/path-for routes routeopts)]))))

#?(:clj (defmacro route-tpl
          [routes & clauses]
          (assert (even? (count clauses)))
          `(let []
             (schema/validate RoutePair ~routes)
             (hoplon.core/case-tpl (wrap-route ~routes *route*) ~@clauses))))
