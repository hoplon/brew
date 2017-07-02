(ns hoplon.bidi
  (:require [hoplon.core :as hl]
            [javelin.core :as j]
            [cuerdas.core :as s]
            [bidi.bidi :as bidi]
            [schema.core :as schema]
            [bidi.schema :as bschema]))

(def match-route bidi/match-route)

(def ^:dynamic *route*
  "Default route cell, without prefix."
  (hl/route-cell ""))

(defn- wrap-route
  "Takes `routes` and a (route-cell).
   Returns formula cell containing current matching handler."
  [routes routec]
  (j/cell=
    (:handler (bidi/match-route routes (s/strip-prefix routec "#")))))

(defn- wrap-route-params
  "Takes `routes` and a (route-cell).
   Returns formula cell containing current route params."
  [routes routec]
  (j/cell=
    (:route-params (bidi/match-route routes (s/strip-prefix routec "#")))))

(defn route
  "Takes `routes`, returns a formula cell with the current route handler."
  [routes]
  (wrap-route routes *route*))

(defn route-params
  "Takes `routes`, returns a formula cell with the current route params."
  [routes]
  (wrap-route-params routes *route*))

(defn route?
  "Takes `routes` and `handler`, returns formula cell.
   Formula cell is true if `route` is currently active."
  [routes handler]
  (let [route (route routes)]
    (j/cell= (= route handler))))

(defn mkroute
  "Generate a valid route. (includes '#' character)"
  [routes & routeopts]
  (s/join ["#" (apply bidi/path-for routes routeopts)]))

(defn route!
  "Change URL hash, does not reload the page."
  [routes & routeopts]
  (aset js/window "location" "hash"
    (apply mkroute routes routeopts)))

(defn validate
  "Validate `routes` against bidi schema."
  [routes]
  (schema/validate bschema/RoutePair routes))
