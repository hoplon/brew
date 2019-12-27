(ns hoplon.indexed-db
  (:require [javelin.core :as j]
            [hoplon.indexed-db.factory :as factory]
            [hoplon.indexed-db.opendbrequest :as req]
            [hoplon.indexed-db.database :as db]
            [hoplon.indexed-db.transaction :as tx])
  (:require-macros hoplon.indexed-db))

;; IndexedDB Helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def indexedDB (.-indexedDB js/window))

(def open-database (partial factory/open indexedDB))

(def upgrade-database req/onupgradeneeded)

(defn create-stores [db stores]
  (let [current (db/objectStoreNames db)]
    (doseq [store stores]
      (when-not (.contains current store)
        (db/createObjectStore db store)))))

(defn delete-stores [db stores]
  (let [current (db/objectStoreNames db)]
    (doseq [store current]
      (when-not (contains? (set stores) store)
        (db/deleteObjectStore db store)))))

(defn get-result [event]
  (.-result (.-target event)))

(defn get-version [event]
  [(.-oldVersion event) (.-newVersion event)])

(defn when-success [tx callback]
  (.addEventListener tx "success" callback))

(defn when-error [tx callback]
  (.addEventListener tx "error" callback))

(defn when-upgrading [tx callback]
  (.addEventListener tx "upgradeneeded" callback))

(defn object-store= [req store key]
  (let [idb (j/cell nil)
        ostore (j/cell nil)
        ostore! (partial reset! ostore)]
    (when-success req
      (fn [event]
        (reset! idb (get-result event))
        (let [store (-> @idb (db/transaction store) (tx/objectStore store))]
          (when-success (.get store key)
            (fn [event]
              (let [result (get-result event)]
                (prn result)
                (ostore! (js->clj result :keywordize-keys true))))))))
    (j/cell= ostore
      (fn [val]
        (let [store (-> @idb (db/transaction store "readwrite") (tx/objectStore store))]
          (when-success (.put store (clj->js val) key)
            (fn [event]
              (ostore! val))))))))

(defn upgrade-database! [db config]
  (when-upgrading db
    (fn [event]
      (let [[old new] (get-version event)
            db (get-result event)]
        (loop [old old new new]
          (when-not (> old new)
            (when-let [stores (get config old)]
              (create-stores db stores)
              (delete-stores db stores))
            (recur (inc old) new)))))))
