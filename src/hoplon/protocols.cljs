(ns hoplon.protocols)

(extend-type js/DOMStringList
  ISeqable
  (-seq [this]
    (.from js/Array this)))
