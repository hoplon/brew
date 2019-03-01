(ns hoplon.feathers
  (:require [javelin.core :as j]
            [feathers.client.services :as fs]))

(defn find-cell [app service & params]
  (let [fcell   (j/cell nil)
        fcell!  #(reset! fcell %)
        error!  #(.error js/console %)
        service (fs/service app service)]
    (j/with-let [_ (j/cell= fcell fcell!)]
      (-> service
        (fs/find (clj->js params))
        (.then fcell!)
        (.catch error!))
      (fs/created service #(prn "created" %))
      (fs/updated service #(prn "updated" %))
      (fs/patched service #(prn "patched" %))
      (fs/removed service #(prn "removed" %)))))
