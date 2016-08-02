(ns hoplon.firebase
  (:require [hoplon.core :as hl]))

(defmacro with-auth!
  "Attaches callbacks to the `auth` state.
  Requires login callback, accepts optional logout callback."
  [auth login & [logout]]
  `(hl/with-init!
    ('fbauth/auth-changed
      ~auth
      #(if % (~login %) ~(when logout `(~logout))))))
