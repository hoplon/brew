(ns hoplon.sorting
  (:require [javelin.core :as j]))

(defn index-coll [data]
  (->> data (map-indexed vector) (into {})))

(defn indexed-cell
  "Returns a formula cell which sorts and indexes a collection."
  [data] (j/cell= (index-coll data)))

(defn sort-indexed [data]
  (-> data (sort) (index-coll)))

(defn sort-by-indexed [data sort]
  (-> data (sort-by sort) (index-coll)))

(defn sorted-cell
  "Returns a formula cell which sorts and indexes a collection."
  ([data] (j/cell= (sort-indexed data)))
  ([data sort] (j/cell= (sort-by-indexed data sort))))
