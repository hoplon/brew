(ns hoplon.feathers
  (:require [javelin.core :as j]
            [feathers.client.services :as fs]))

(defn feathers-created [fcell]
  (fn [data] (swap! fcell conj (js->clj data :keywordize-keys true))))

(defn- remap [coll index]
  (zipmap (map index coll) coll))

(defn feathers-updated [fcell]
  (fn [data]
    (let [data (js->clj data :keywordize-keys true)]
      (swap! fcell
        (fn [fdata]
          (let [fdata (remap fdata :_id)]
            (vals (assoc fdata (:_id data) data))))))))

(defn feathers-patched [fcell]
  (fn [data]
    (let [data (js->clj data :keywordize-keys true)]
      (swap! fcell
        (fn [fdata]
          (let [fdata (remap fdata :_id)]
            (vals (assoc fdata (:_id data) (merge (get fdata (:_id data)) data)))))))))

(defn feathers-removed [fcell]
  (fn [data]
    (let [data (js->clj data :keywordize-keys true)]
      (swap! fcell
        (fn [fdata]
          (remove #(= (:_id %) (:_id data)) fdata))))))


(defn feathers-cell [service & params]
  (let [fcell   (j/cell nil)
        fcell!  #(reset! fcell %)
        error!  #(.error js/console %)]
    (j/with-let [_ (j/cell= fcell fcell!)]
      (-> service
        (fs/find (clj->js params))
        (.then #(js->clj % :keywordize-keys true))
        (.then fcell!)
        (.catch error!))
      (fs/created service (feathers-created fcell))
      (fs/updated service (feathers-updated fcell))
      (fs/patched service #(prn "patched" %))
      (fs/removed service (feathers-removed fcell)))))
