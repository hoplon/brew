(ns hoplon.skel
  (:require [clojure.string :as s]))

(defmacro with-breakpoint [& bp]
  (let [f (last bp)
        e (s/join " " (mapv str (butlast bp)))]
    `(on ~e ~f)))
