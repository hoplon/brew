(ns hoplon.indexed-db)

(defmacro with-db [name & body]
  `(fn [event#]
    (let [~name (.-result (.-target event#))]
      ~@body)))
