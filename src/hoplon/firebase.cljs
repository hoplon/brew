(ns hoplon.firebase
  (:require [javelin.core :as j]
            [cuerdas.core :as str]
            [firebase-cljs.database :as fbdb]
            [firebase-cljs.database.datasnapshot :as fbsnap]))

(defn fb->clj
  "Converts a Firebase DataSnapshot into a clj data structure."
  [fb]
  (-> fb fbsnap/val (js->clj :keywordize-keys true)))

(defn fb-cell
  "Returns a formula cell bound to the Firebase Reference.
  Takes an optional Firebase `event` to limit which event updates the cell.
  Attempting to call `reset!` on the cell updates the Firebase Database."
  [ref & [event]]
  (let [fbc   (cell nil)
        event (str/underscored (or event "value"))]
    (fbdb/listen ref event #(reset! fbc (fb->clj %)))
    (j/cell= fbc #(fbdb/reset! ref %))))
