(ns collections-musescore.views.animation-util
(:require
  [re-frame.core :refer [dispatch subscribe]]
  [reagent.core :as reagent])
  )

(def animation-length 300)

(defn delete-with-animation [dispatch-function item-exists?]
  (reset! item-exists? false)
  (js/setTimeout dispatch-function animation-length))
