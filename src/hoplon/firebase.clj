(ns hoplon.firebase
  (:require [hoplon.core :as hl]))

(defmacro with-auth!
  "Attaches callbacks to the `auth` state.
  Requires login callback, accepts optional logout callback.

  `login` must accept one argument which will be the auth data.
  `logout` must not accept arguments.

  When specifying `auth` both `login` and `logout` must be provided."
  ([auth login logout]
    `(hl/with-init!
      (when-auth ~auth
        #(if % (~login %) ~(when logout `(~logout))))))
  ([login logout]
    `(with-auth! *auth* ~login ~logout))
  ([login]
    `(with-auth! *auth* ~login nil)))
