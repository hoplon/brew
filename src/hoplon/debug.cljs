(ns hoplon.debug)

(defn debug [module]
  (fn [& args]
    (apply js/console.debug (str "[" module "]") args)))
