(ns collections-musescore.score.events
  (:require [collections-musescore.api :as api]
            [collections-musescore.events-util :as util]
            [re-frame.core :refer [dispatch enrich path reg-event-db reg-event-fx]]))

(def add-score-loading-status (enrich #(assoc % :score-loading true)))
(def remove-score-loading-status (enrich #(dissoc % :score-loading)))

(defn- add-score-to-collection [collection score-info]
  (assoc-in collection [:scores (int (:id score-info))]  score-info))

(defn add-score [collections [_ collection-id score-info]]
  (update collections collection-id add-score-to-collection score-info))

(defn remove-score-from-collections [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc (int score-id)))

(defn update-temp-score [temp-url-info [_ result]]
  (js->clj result))

(defn get-score-by-url [status [_ url]]
  (api/get-info-by-url url (fn [result]
                             (dispatch [:update-temp-score result])))
  status)

(reg-event-fx
 :get-score-by-url
 [add-score-loading-status]
 get-score-by-url)

(reg-event-db
 :add-score
 [util/check-spec (path :collections) util/->local-store]
 add-score)

(reg-event-db
 :remove-score
 [util/check-spec (path :collections) util/->local-store]
 remove-score-from-collections)

(reg-event-db
 :update-temp-score
 [remove-score-loading-status
  (path :temp-url-info)]
 update-temp-score)

(reg-event-fx
 :get-suggestions
 (fn [status [_ title]]
   (api/search-score title (fn [result]
                             (dispatch [:update-score-suggestions result])))
   status)) ;; TODO loading db status

(reg-event-db
 :update-score-suggestions
 [(path :suggestions)]
 (fn [suggestions [_ result]] result))

(reg-event-db
 :clear-score-suggestions
 [(path :suggestions)]
 [])
