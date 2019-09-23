(ns collections-musescore.events.events
  (:require [collections-musescore.db :as db]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path dispatch]]
            [collections-musescore.api.musescore :refer [get-info-by-url]]
            [collections-musescore.events.interceptors :refer [collections-interceptors check-spec-interceptor]]))


(reg-event-fx
 :get-url-info
 (fn [db [_ url]]
   (get-info-by-url url #(dispatch [:get-url-info-success %]))
   db))

(reg-event-db
 :get-url-info-success
 [(path :temp-url-info)]
 (fn [temp-url-info [_ result]]
   result))

(reg-event-fx                 ;; part of the re-frame API
 :initialise-db              ;; event id being handled

  ;; the interceptor chain (a vector of 2 interceptors in this case)
 [(inject-cofx :local-store-collections) ;; gets todos from localstore, and puts value into coeffects arg
  check-spec-interceptor]          ;; after event handler runs, check app-db for correctness. Does it still match Spec?

  ;; the event handler (function) being registered
 (fn [{:keys [db local-store-collections]} _]                  ;; take 2 values from coeffects. Ignore event vector itself.
   {:db (assoc db/default-db :collections  local-store-collections)}))   ;; all hail the new state to be put in app-db