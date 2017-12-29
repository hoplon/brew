(ns hoplon.observer)

;; Hoplon mutation! Multimethod ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmulti mutation! dispatcher :default ::default)

(defmethod mutation! ::default
  [elem key [config callback]]
  (let [observer (js/MutationObserver. callback)]
    (.observe observer elem config)))

(defmethod mutation! :mutation/childList
  [elem key callback]
  (mutation! elem [#js {:childList true} callback]))

(defmethod mutation! :mutation/attributes
  [elem key callback]
  (mutation! elem [#js {:attributes true} callback]))

(defmethod mutation! :mutation/characterData
  [elem key callback]
  (mutation! elem [#js {:characterData true} callback]))

(defmethod mutation! :mutation/subtree
  [elem key callback]
  (mutation! elem [#js {:subtree true} callback]))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Hoplon intersection! Multimethod ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmulti intersection! dispatcher :default ::default)

(defmethod intersection! ::default
  [elem key config callback]
  (let [observer (js/IntersectionObserver. callback config)]
    (.observe observer elem)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Hoplon performance! Multimethod ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmulti performance! dispatcher :default ::default)

(defmethod performance! ::default
  [key config callback]
  (let [observer (js/PerformanceObserver. callback)]
    (.observe observer config)))

(defmethod performance! :performance/mark
  [key callback]
  (performance! ::default #js {:eventTypes ["mark"]} callback))

(defmethod performance! :performance/measure
  [key callback]
  (performance! ::default #js {:eventTypes ["measure"]} callback))

(defmethod performance! :performance/frame
  [key callback]
  (performance! ::default #js {:eventTypes ["frame"]} callback))

(defmethod performance! :performance/navigation
  [key callback]
  (performance! ::default #js {:eventTypes ["navigation"]} callback))

(defmethod performance! :performance/resource
  [key callback]
  (performance! ::default #js {:eventTypes ["resource"]} callback))

(defmethod performance! :performance/paint
  [key callback]
  (performance! ::default #js {:eventTypes ["paint"]} callback))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
