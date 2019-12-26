(ns hoplon.indexed-db.opendbrequest)

(defprotocol IDBOpenDBRequest
  (onerror [this callback] "Handle errors on the open database request.")
  (onsuccess [this callback] "Handle success on the open database request.")
  (onupgradeneeded [this callback] "Handle database upgrade needed on the open database request."))

(extend-type js/IDBOpenDBRequest
  IDBOpenDBRequest
  (onerror [this callback]
    (.addEventListener this "error" callback))
  (onsuccess [this callback]
    (.addEventListener this "success" callback))
  (onupgradeneeded [this callback]
    (.addEventListener this "upgradeneeded" callback)))
