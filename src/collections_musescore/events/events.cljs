(ns collections-musescore.events.events
  (:require [collections-musescore.db :as db]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path dispatch]]
            [collections-musescore.api :as api]
            ["@material-ui/core" :as mui]
            [clojure.string :as str]
            [collections-musescore.events.score :as score]
            [collections-musescore.events.collection :as collection]
            [collections-musescore.events.interceptors :refer [collections-interceptors check-spec-interceptor]]))


(reg-event-fx
 :get-url-info
 (fn [db [_ url]]
   (api/get-info-by-url url #(dispatch [:update-url-info %]))
   db))

(reg-event-db
 :update-url-info
 [(path :temp-url-info)]
 (fn [temp-url-info [_ result]]
   result))

(reg-event-fx
 :initialise-db
 [(inject-cofx :local-store-collections)
  check-spec-interceptor]
 (fn [{:keys [db local-store-collections]}]
   {:db (assoc db/default-db :collections  local-store-collections)}))

(reg-event-fx
 :get-suggestions
 (fn [db [_ title]]
   (api/search-score title #(dispatch [:update-suggestions %]))
   db)) ;; TODO loading db status

(reg-event-db
 :update-suggestions
 [(path :suggestions)]
 (fn [suggestions [_ result]]
   (map #(:title %) result)))

(reg-event-db
 :clear-suggestions
 [(path :suggestions)]
 [])


; TODO slow should really do ID map instead of array
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
 :remove-score
 collections-interceptors
 score/remove-from-collections)