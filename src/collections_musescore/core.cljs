(ns collections-musescore.core
  (:require [goog.events :as events]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [secretary.core :as secretary]
            [collections-musescore.events] 
            [oops.core :refer [oget oset! ocall oapply ocall! oapply!
                               oget+ oset!+ ocall+ oapply+ ocall!+ oapply!+]]
            [collections-musescore.score.events]
            [collections-musescore.collection.events]
            [collections-musescore.collection.subs]
            [collections-musescore.score.subs]
            [collections-musescore.search.subs]
            [collections-musescore.search.events]
            [collections-musescore.api]
            ;;   [tests.test_runner]
            [collections-musescore.score.views]
            [collections-musescore.views.main :as views])

  (:import [goog History]
           [goog.history EventType]))

(set! *warn-on-infer* true) ;; TODO fix infer errors
(enable-console-print!)


;; define your app data so that it doesn't get over-written on reload
(dispatch-sync [:initialise-db])

;; (def history
;;   (doto (History.)
;;     (events/listen EventType.NAVIGATE
;;                    (fn [event] (secretary/dispatch! (oget event "token"))))
;;     (.setEnabled true)))


(reagent/render-component [views/main]
                          (ocall js/document "getElementById" "app"))
;; wtuf

(defn on-js-reload []
;; optionally touch your app-state to force rerendering depending on
;; your application
;; (swap! app-state update-in [:__figwheel_counter] inc)
)
