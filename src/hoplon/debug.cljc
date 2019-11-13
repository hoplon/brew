(ns hoplon.debug
  (:require
    #?@(:browser [["debug" :as dbg]])))

(defn debug [namespace]
  #?(:browser (dbg namespace)
     :cljs (fn [& args] (apply js/console.debug (str "[" namespace "]") args))))
