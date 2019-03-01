(ns holon.extra
  (:require [hoplon.core :as h]))

(defn- stylesheet [url]
  (h/link :rel "stylesheet" :href url))

(defn- javascript [url]
  (h/script :type "text/javascript" :src url))
