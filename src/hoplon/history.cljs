(ns hoplon.history)

(defn history-cell
 "A cell analagous to hoplon.core/route-cell, using Google Closure History API."
 []
 (let [c (j/cell nil)
       history (History.)]
  (j/with-let [_ (j/cell= c #(.setToken history %))]
   (goog.events/listen history goog.History/EventType.NAVIGATE
    (fn [e] (reset! c (.-token e))))
   (.setEnabled history true))))
