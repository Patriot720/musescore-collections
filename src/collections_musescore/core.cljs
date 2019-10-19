(ns collections-musescore.core
  (:require [goog.events :as events]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [secretary.core :as secretary]
            [collections-musescore.events.events] ;; These two are only required to make the compiler
            [collections-musescore.subs]   ;; load them (see docs/App-Structure.md)
            [collections-musescore.api]
            [tests.test-runner] ;; TEST ONLY
            [collections-musescore.views.score-views]
            [collections-musescore.views.views :as views])

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

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

(reagent/render-component [views/main]
                          (. js/document (getElementById "app")))
