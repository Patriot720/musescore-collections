(ns collections-musescore.score.events
  (:require [collections-musescore.api :as api]
            [collections-musescore.events-util :as util]
            [re-frame.core :refer [dispatch enrich path reg-event-db reg-event-fx]]))

(def ^:private add-score-loading-status (enrich #(assoc % :score-loading true)))
(def ^:private remove-score-loading-status (enrich #(dissoc % :score-loading)))

(defn get-search-suggestions [status [_ title]]
  (api/search-score title #(dispatch [:update-search-suggestions %]))
  {})

(defn update-search-suggestions [suggestions [_ result]] result)

(defn get-score-by-url [status [_ url]]
  (api/get-info-by-url url #(dispatch [:update-temp-score %]))
  {})

(defn update-temp-score [temp-url-info [_ result]]
  (js->clj result))

(defn- add-score-to-collection [collection score-info]
  (assoc-in collection [:scores (int (:id score-info))]  score-info))

(defn add-score [collections [_ collection-id score-info]]
  (update collections collection-id add-score-to-collection score-info))

(defn remove-score [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc (int score-id)))

(reg-event-fx
 :get-search-suggestions
 get-search-suggestions)

(reg-event-db
 :update-search-suggestions
 [(path :suggestions)]
 update-search-suggestions)

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
 remove-score)

(reg-event-db
 :update-temp-score
 [remove-score-loading-status
  (path :temp-url-info)]
 update-temp-score)

(reg-event-db
 :clear-score-suggestions
 [(path :suggestions)]
 [])
