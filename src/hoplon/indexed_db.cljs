(ns hoplon.indexed-db
  (:require [javelin.core :as j])
  (:require-macros hoplon.indexed-db))

(def indexedDB (.-indexedDB js/window))

(defprotocol IDBFactory
  (open [this name] [this name version] "Open the connection to indexedDB database.")
  (deleteDatabase [this name] "Delete an indexedDB database.")
  (databases [this] "List all available databases, including name and version."))

(defprotocol IDBOpenDBRequest
  (onerror [this callback] "Handle errors on the open database request.")
  (onsuccess [this callback] "Handle success on the open database request.")
  (onupgradeneeded [this callback] "Handle database upgrade needed on the open database request."))

(defprotocol IDBDatabase
  (close [this] "Async closes the connection to a database.")
  (createObjectStore [this name] [this name  opts] "Creates a new object store.")
  (deleteObjectStore [this name] "Delete an object store.")
  (transaction [this stores] [this stores mode] "Returns the transaction object.")
  (objectStoreNames [this] "Returns a DOMStringList of all existing object stores."))

(extend-type js/IDBFactory
  IDBFactory
  (open
    ([this name]
     (.open this name))
    ([this name version]
     (.open this name version)))
  (deleteDatabase [this name]
    (.deleteDatabase this name))
  (databases [this]
    (.deleteDatabase this)))

(extend-type js/IDBOpenDBRequest
  IDBOpenDBRequest
  (onerror [this callback]
    (.addEventListener this "error" callback))
  (onsuccess [this callback]
    (.addEventListener this "success" callback))
  (onupgradeneeded [this callback]
    (.addEventListener this "upgradeneeded" callback)))

(extend-type js/IDBDatabase
  IDBDatabase
  (close [this]
    (.close this))
  (createObjectStore
    ([this name]
     (.createObjectStore this name))
    ([this name opts]
     (.createObjectStore this name opts)))
  (deleteObjectStore [this name]
    (.deleteObjectStore this name))
  (transaction
    ([this stores]
     (.transaction this stores))
    ([this stores mode]
     (.transaction this stores mode)))
  (objectStoreNames [this]
    (.-objectStoreNames this)))
