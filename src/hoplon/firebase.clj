(ns hoplon.firebase
  (:require [hoplon.core :as hl]))

(defmacro with-auth!
  "Attaches callbacks to the `auth` state.
  Requires login callback, accepts optional logout callback.

  Login callback must accept one argument which will be the auth data.
  Logout callback does not accept arguments."
  [auth login & [logout]]
  `(hl/with-init!
    (when-auth ~auth
      #(if % (~login %) ~(when logout `(~logout))))))
