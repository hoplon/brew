(ns hoplon.firebase
  (:require [javelin.core :as j]
            [cuerdas.core :as str]
            [cljsjs.firebase]
            [firebase-cljs.core :as fb]
            [firebase-cljs.auth :as fbauth]
            [firebase-cljs.database :as fbdb]
            [firebase-cljs.database.datasnapshot :as fbsnap])
  (:require-macros [adzerk.env :as env]))

;; Firebase Init ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(env/def
  FIREBASE_API_KEY        nil
  FIREBASE_AUTH_DOMAIN    nil
  FIREBASE_DATABASE_URL   nil
  FIREBASE_STORAGE_BUCKET nil)

(def FIREBASE_ENV
  (and FIREBASE_API_KEY
       FIREBASE_AUTH_DOMAIN
       FIREBASE_DATABASE_URL
       FIREBASE_STORAGE_BUCKET))

(when FIREBASE_ENV
  (fb/init
    {:apiKey FIREBASE_API_KEY
     :authDomain FIREBASE_AUTH_DOMAIN
     :databaseURL FIREBASE_DATABASE_URL
     :storageBucket FIREBASE_STORAGE_BUCKET}))

;; Firebase Services ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def ^:dynamic *auth* (when FIREBASE_ENV (fb/get-auth)))

(def ^:dynamic *app*  (when FIREBASE_ENV (fb/get-app)))

(def ^:dynamic *db*   (when FIREBASE_ENV (fb/get-db)))

;;todo: storage api

;; Internal Helper Functions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- when-auth
  ([callback]
   (when-auth *auth* callback))
  ([auth callback]
   (fbauth/auth-changed auth callback)))

;; Firebase Helper Fn's ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn fb->clj
  "Converts a Firebase DataSnapshot into a clj data structure."
  [fb]
  (-> fb fbsnap/val (js->clj :keywordize-keys true)))

(defn fb-ref
  "Returns a Firebase Reference to `korks`.

  `korks` is a vector of keywordized paths in the database.
  ie. [:path :from :root]

  Optionally accepts a Firebase Database as first argument."
  ([korks] (fb-ref *db* korks))
  ([db korks] (fbdb/get-in db korks)))

(defn fb-cell
  "Returns a formula cell bound to the Firebase Reference.
  Takes an optional Firebase `event` to limit which event updates the cell.
  Attempting to call `reset!` on the cell updates the Firebase Database.

  (This can only be used outside of auth state change when `ref` is public,
   ie. not read secured by Firebase Authentication. Listeners for secured ref's
   should be set from the auth login handler.)"
  [ref & [event]]
  (let [fbc   (j/cell nil)
        event (str/underscored (or event "value"))]
    (fbdb/listen ref event #(reset! fbc (fb->clj %)))
    (j/cell= fbc #(fbdb/reset! ref %))))

(defn fbonce-cell
  "Returns a formula cell unbound to the Firebase Reference.
  Takes an optional Firebase `event` to limit which event updates the cell."
  [ref & [event]]
  (let [fbc   (j/cell nil)
        event (str/underscored (or event "value"))]
    (fbdb/listen-once ref event #(reset! fbc (fb->clj %)))
    (j/cell= fbc)))

(defn fb-sync
  "Returns a formula cell unbound to the Firebase Reference.
  This variant will fetch changes and update only after persisting changes to
  the Firebase Database.
  Takes an optional Firebase `event` to limit which event updates the cell."
  [ref & [event]]
  (let [fbc   (j/cell nil)
        event (str/underscored (or event "value"))
        sync  #(fbdb/listen-once ref event
                (fn [fbdat] (reset! fbc (fb->clj fbdat))))]
    (sync)
    (j/cell= fbc #(dosync (fbdb/reset! ref %) (sync)))))

(defn fb-sync
  "Returns a formula cell unbound to the Firebase Reference.
  This variant will fetch changes and update only after persisting changes to
  the Firebase Database.
  Takes an optional Firebase `event` to limit which event updates the cell."
  [ref & [event]]
  (let [fbc   (cell nil)
        event (str/underscored (or event "value"))
        sync  #(fbdb/listen-once ref event
                (fn [fbdat] (reset! fbc (fb->clj fbdat))))]
    (sync)
    (cell= fbc #(dosync (fbdb/reset! ref %) (sync)))))

(defn fbwhen-cell
  "Returns a formula cell unbound to the Firebase Reference when `pred` is true.
  Takes an optional Firebase `event` to limit which event updates the cell."
  [pred ref & [event]]
  (let [fbc (j/cell nil)
        event (str/underscored (or event "value"))]
    (j/cell=
      (when pred
        (fbdb/listen-once ref event ~#(reset! fbc (fb->clj %)))))
    (j/cell= fbc)))

(defn fb-default
  "Returns a promise which will set the value of `ref` to `default` if it
  does not exist."
  [ref default]
  (fbdb/listen-promise ref "value"
    #(when-not (fbsnap/exists %) (fbdb/reset! ref default))))
