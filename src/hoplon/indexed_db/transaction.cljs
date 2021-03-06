(ns hoplon.indexed-db.transaction)

(defprotocol IDBTransaction
  (db [this] "The database connection with which this transaction is associated.")
  (error [this] "Returns an exception indicating the type of error that occured when there is an unsuccessful transaction.")
  (mode [this] "The mode for isolating access to data in the object stores that are in the scope of the transaction.")
  (objectStoreNames [this] "Returns a list of the names of object stores associated with the transaction.")
  (abort [this] "Rolls back all the changes to objects in the database associated with this transaction.")
  (objectStore [this name] "Returns an object store that has already been added to the scope of this transaction.")
  (commit [this] "Commits the transaction if it is called on an active transaction."))

(extend-type js/IDBTransaction
  IDBTransaction
  (db [this]
    (.-db this))
  (error [this]
    (.-error this))
  (mode [this]
    (.-mode this))
  (objectStoreNames [this]
    (.-objectStoreNames this))
  (abort [this]
    (.abort this))
  (objectStore [this name]
    (.objectStore this name))
  (commit [this]
    (.commit this)))
