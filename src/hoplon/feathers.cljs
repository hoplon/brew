(ns hoplon.feathers
  (:require [javelin.core :as j]
            [feathers.client.services :as fs]))

(defn feathers-created [fcell]
  (fn [data] (swap! fcell conj (js->clj data :keywordize-keys true))))

(defn remap [coll index]
  (zipmap (map index coll) coll))

(defn compare-index [index coll1 coll2]
  (= (get coll1 index)
     (get coll2 index)))

;(defn feathers-updated [fcell]
;  (fn [data]
;    (let [data (js->clj data :keywordize-keys true)]
;      (swap! fcell
;        (fn [fdata]
;          (conj (remove (partial compare-index :_id data) fdata) data))))

(defn feathers-cell [app service & params]
  (let [fcell   (j/cell nil)
        fcell!  #(reset! fcell %)
        error!  #(.error js/console %)
        service (fs/service app service)]
    (j/with-let [_ (j/cell= fcell fcell!)]
      (-> service
        (fs/find (clj->js params))
        (.then fcell!)
        (.catch error!))
      (fs/created service (feathers-created fcell))
      (fs/updated service #(prn "updated" %))
      (fs/patched service #(prn "patched" %))
      (fs/removed service #(prn "removed" %)))))
