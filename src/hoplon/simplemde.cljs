(ns hoplon.simplemde
  (:require [hoplon.core :as hl]
            [javelin.core :as j]
            [cljsjs.simplemde]))

(defn editor!
  "Creates a SimpleMDE editor."
  [& [config]]
  (if config (js/SimpleMDE. (clj->js config))
    (js/SimpleMDE.)))

(defelem editor [attr kids]
  (let [content  (:content attr)
        change   (:change attr)
        blur     (:blur attr)
        attr     (dissoc attr :content :change :blur)
        editor   (j/cell nil)
        textarea (hl/textarea :css {:display "none"} kids)]
    (j/cell= (when (and content editor) (.value editor content)))
    (hl/with-dom textarea
      (let [sme (editor! (assoc attr :element textarea))
            cm  (.-codemirror sme)]
        (reset! editor sme)
        (when blur
          (.on cm "blur" #(blur (.getValue %1))))
        (when change
          (.on cm "change" #(change (.getValue %1))))))
    textarea))
