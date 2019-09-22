(ns collections-musescore.events.events
  (:require [collections-musescore.db :as db]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path dispatch]]
            [collections-musescore.api.musescore :refer [get-info-by-url]]
            [ajax.core :as ajax]
            [clojure.string]))



(defrecord Collection [id title scores])
(defn collection [id title scores]
  (into {} (->Collection id title scores)))



(defn add-collection [collections [_ title]]
  (let [id (allocate-next-id collections)]
    (assoc collections id (collection id title {}))))

; (defn- collection-title-equals? [collection title]
;   (= (:title collection) title))

(defn remove-collection [collections [_ id]]
  (dissoc collections id))

; TODO slow should really do ID map instead of array
(reg-event-db
 :remove-collection
 collections-interceptors
 remove-collection)
(reg-event-db
 :add-score
 collections-interceptors
 add-score)
(reg-event-db
 :add-collection
 collections-interceptors
 add-collection)

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

(defn remove-score [scores score-id]
  (dissoc scores score-id))

(defn remove-score-from-collections [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc score-id))

(reg-event-db
 :remove-score
 collections-interceptors
 remove-score-from-collections)