(ns hoplon.indexed-db
  (:require [javelin.core :as j]))

(def indexedDB (js/window -indexedDB))

(defprotocol IDBFactory
  (open [this name & [version]] "Open the connection to indexedDB database.")
  (deleteDatabase [this name] "Delete an indexedDB database.")
  (databases [this] "List all available databases, including name and version."))

(defprotocol IDBOpenDBRequest
  (onerror [this callback] "Handle errors on the open database request.")
  (onsuccess [this callback] "Handle success on the open database request.")
  (onupgradeneeded [this callback] "Handle database upgrade needed on the open database request."))

(defprotocol IDBDatabase
  (close [this] "Async closes the connection to a database.")
  (createObjectStore [this name & [opts]] "Creates a new object store.")
  (deleteObjectStore [this name] "Delete an object store.")
  (transaction [this stores & [mode]] "Returns the transaction object."))

(extend-type Object
  IDBFactory
  (open [this name & [version]]
    (.open this name version))
  (deleteDatabase [this name]
    (.deleteDatabase this name))
  (databases [this]
    (.deleteDatabase this))
  IDBOpenDBRequest
  (onerror [this callback]
    (.onerror this callback))
  (onsuccess [this callback]
    (.onsuccess this callback))
  (onupgradeneeded [this callback]
    (.onupgradeneeded this callback))
  IDBDatabase
  (close [this]
    (.close this))
  (createObjectStore [this name & [opts]]
    (.createObjectStore this name opts))
  (deleteObjectStore [this name]
    (.deleteObjectStore this name))
  (transaction [this stores & [mode]]
    (.transaction this stores mode)))
