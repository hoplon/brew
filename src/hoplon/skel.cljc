(ns hoplon.skel
  (:require [clojure.string :as s]
            #?@(:cljs [[cljsjs.skel]]))
  #?(:cljs (:require-macros [hoplon.skel :refer [with-breakpoint]])))

;; Skel Defaults
(defc skel-loading true)
(defc skel-mobile (aget js/skel "vars" "mobile"))

;; Init Skel Breakpoints
(defn breakpoints!
  ([]
   (breakpoints! :xlarge "1680px" :large  "1280px" :medium "980px" :small  "736px" :xsmall "480px"))
  ([& {:keys [xlarge large medium small xsmall]}]
   (.breakpoints js/skel
                 #js {:xlarge (str "(max-width:" xlarge ")")
                      :large  (str "(max-width:" large  ")")
                      :medium (str "(max-width:" medium ")")
                      :small  (str "(max-width:" small  ")")
                      :xsmall (str "(max-width:" xsmall ")")})))

;; Skel Event Methods
(defn on [events f]
  (.on js/skel events f))

;; Skel Window Load Complete
(.on (js/jQuery js/window) "load" #(reset! skel-loading false))

;; Skel Toggle Mobile Cell
(with-breakpoint -medium #(reset! skel-mobile false))
(with-breakpoint +medium #(reset! skel-mobile true))

;;Prioritize "important" elements on medium
(with-breakpoint +medium -medium
  #(.prioritize js/jQuery
    ".important\\28 medium\\29" (-> js/skel (.breakpoint "medium") .active)))

;; CLJS Macros
#?(:clj (defmacro with-breakpoint [& bp]
          (let [f (last bp)
                e (s/join " " (mapv str (butlast bp)))]
            `(on ~e ~f))))
