(ns hoplon.skel
  (:require [clojure.string :as s]
            [javelin.core :as j]
            [cljsjs.skel]
            [goog.object :as obj])
  (:require-macros [hoplon.skel :refer [with-breakpoint]]))

;; Skel Defaults
(j/defc skel-loading true)
(j/defc skel-mobile (obj/get js/skel "vars" "mobile"))

;; Init Skel Breakpoints
(defn breakpoints!
  ([]
   (breakpoints! :xlarge "1680px" :large  "1280px" :medium "980px" :small  "736px" :xsmall "480px"))
  ([& breakpoints]
    (->> breakpoints
      (into {})
      (map #(identity [(key %) (s/format "(max-width:%s)" (val %))]))
      (into {})
      (clj->js)
      (.breakpoints js/skel))))

;; Skel Event Methods
(defn on [events f]
  (.on js/skel events f))

;; Skel Window Load Complete
(.addEventListener js/window "load" #(reset! skel-loading false))

;; Skel Toggle Mobile Cell
(with-breakpoint -medium #(reset! skel-mobile false))
(with-breakpoint +medium #(reset! skel-mobile true))
