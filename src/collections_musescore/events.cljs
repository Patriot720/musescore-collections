(ns collections-musescore.events
  (:require [collections-musescore.db :as db]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path dispatch enrich]]
            [collections-musescore.api :as api]
            [collections-musescore.data.score :as score]
            [collections-musescore.data.collection :as collection]
            [cljs.spec.alpha :as s]))

(defn- check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec (after (partial check-and-throw :collections-musescore.db/db)))

(def add-score-loading-status (enrich #(assoc % :score-loading true)))
(def remove-score-loading-status (enrich #(dissoc % :score-loading)))

(def ->local-store (after db/->local-store))
(def collections-interceptors [check-spec
                               (path :collections)
                               ->local-store])

(reg-event-fx
 :get-suggestions
 (fn [status [_ title]]
   (api/search-score title (fn [result]
                             (dispatch [:update-suggestions result])))
   status)) ;; TODO loading db status

(reg-event-db
 :stop-loading
 #(assoc % :loading false))

(reg-event-db
 :update-suggestions
 [(path :suggestions)]
 (fn [suggestions [_ result]] result))

(reg-event-db
 :clear-suggestions
 [(path :suggestions)]
 [])

(reg-event-fx
 :get-url-info
 [add-score-loading-status]
 (fn [status [_ url]]
   (api/get-info-by-url url (fn [result]
                              (dispatch [:update-url-info result])))
   status))

(reg-event-db
 :update-url-info
 [remove-score-loading-status
  (path :temp-url-info)]
 score/update-url-info)

(reg-event-fx
 :initialise-db
 [(inject-cofx :local-store-collections)
  check-spec]
 db/initialize-db)

(reg-event-db
 :remove-collection
 collections-interceptors
 collection/remove-collection)

(reg-event-db
 :add-collection
 collections-interceptors
 collection/add)

(reg-event-db
 :add-score
 collections-interceptors
 score/add)

(reg-event-db
 :search-scores
 score/search-scores
 )
(reg-event-db
 :remove-score
 collections-interceptors
 score/remove-from-collections)
