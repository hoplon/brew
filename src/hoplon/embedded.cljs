(ns hoplon.embedded
  (:require [hoplon.core :as h]
            [javelin.core :as j]))

(defmulti h/do! :mount [elem _ v]
  (let [mount (js/jQuery v)]
    (.append mount elem)))
