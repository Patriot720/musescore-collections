(ns collections-musescore.events
  (:require [collections-musescore.db :as db]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path]]
            [cljs.spec.alpha :as s]))

(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :collections-musescore.db/db)))

(def ->local-store (after db/->local-store))
(def collections-interceptors [(path :collections)
                               ->local-store])

(defn allocate-collection-id [collections]
  (count collections))

(defrecord Collection [title scores])
(defrecord Score [title url])

(reg-event-db
 :add-collection
 collections-interceptors
 (fn [collections [_ title]]
   (conj collections (->Collection title []))))

(reg-event-fx                 ;; part of the re-frame API
 :initialise-db              ;; event id being handled

  ;; the interceptor chain (a vector of 2 interceptors in this case)
 [(inject-cofx :local-store-collections) ;; gets todos from localstore, and puts value into coeffects arg
  check-spec-interceptor]          ;; after event handler runs, check app-db for correctness. Does it still match Spec?

  ;; the event handler (function) being registered
 (fn [{:keys [db local-store-collections]} _]                  ;; take 2 values from coeffects. Ignore event vector itself.
   {:db (assoc db/default-db :collections local-store-collections)}))   ;; all hail the new state to be put in app-db
