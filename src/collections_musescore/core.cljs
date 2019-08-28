(ns collections-musescore.core
  (:require [goog.events :as events]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [secretary.core :as secretary]
            [collections-musescore.events] ;; These two are only required to make the compiler
            [collections-musescore.subs]   ;; load them (see docs/App-Structure.md)
            [collections-musescore.views :as views])
  (:import [goog History]
           [goog.history EventType]))

(enable-console-print!)


;; define your app data so that it doesn't get over-written on reload

(defonce app-state (reagent/atom {:text "Hello world!"}))


(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Edit this and watch it change!"]])


; (defroute "/" [] (dispatch [:set-showing :all]))
; (defroute "/:filter" [filter] (dispatch [:set-showing (keyword filter)]))

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