(ns hoplon.skel
  (:require [javelin.core :as j]
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
  ([& {:keys [xlarge large medium small xsmall]}]
    (->> {:xlarge xlarge :large large :medium medium :small small :xsmall xsmall}
      (reduce-kv #(assoc %1 %2 (str "(max-width:" %3 ")")) {})
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
