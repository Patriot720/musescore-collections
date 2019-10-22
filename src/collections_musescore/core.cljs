(ns collections-musescore.core
  (:require [goog.events :as events]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [secretary.core :as secretary]
            [collections-musescore.events] 
            [collections-musescore.subs]   
            [collections-musescore.api]
            ;;   [tests.test_runner]
            [collections-musescore.views.score]
            [collections-musescore.views.main :as views])

  (:import [goog History]
           [goog.history EventType]))

(enable-console-print!)


;; define your app data so that it doesn't get over-written on reload

(dispatch-sync [:initialise-db])

(def history
  (doto (History.)
    (events/listen EventType.NAVIGATE
                   (fn [event] (secretary/dispatch! (.-token event))))
    (.setEnabled true)))


(reagent/render-component [views/main]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
;; optionally touch your app-state to force rerendering depending on
;; your application
;; (swap! app-state update-in [:__figwheel_counter] inc)
)
