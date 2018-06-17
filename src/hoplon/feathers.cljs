(ns hoplon.feathers
  (:require [javelin.core :as j]
            [feathers.client.services :as fs]))

(defn feathers-cell [service]
  (let [fcell (j/cell nil)]
    (j/cell= (prn fcell))
    (-> (fs/find service) (.then #(reset! fcell %)))
    (fs/created service #(swap! fcell conj %))
    ;(fs/updated service #(swap! fcell conj %))
    ;(fs/patched service #(swap! fcell conj %))
    ;(fs/removed service #(swap! fcell conj %))
    (j/cell= fcell)))
