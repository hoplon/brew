(ns hoplon.indexed-db)

(defmacro with-result [name & body]
  `(fn [event#]
    (let [~name (get-result event#)]
      ~@body)))

(defmacro with-objectstore [[storesym store] & body]
  `(with-result db#
    (let [tx# (get-transaction db# ~store "readwrite")
          ~storesym (object-store tx# ~store)]
      ~@body)))
