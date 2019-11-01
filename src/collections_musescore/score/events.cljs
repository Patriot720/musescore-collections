(ns collections-musescore.score.events
  (:require [collections-musescore.api :as api]
            [collections-musescore.events-util :as util]
            [collections-musescore.search.events :as search]
            [re-frame.core :refer [->interceptor dispatch enrich path reg-event-db reg-event-fx]]))

(def ^:private add-score-loading-status (enrich #(assoc % :score-loading true)))
(def ^:private remove-score-loading-status (enrich #(dissoc % :score-loading)))
(def params->int
  (->interceptor
   :id params->int
   :before (fn [context]
             (update-in context [:coeffects :event]
                        (fn [event]
                          (map
                           (fn [item] (if (js/parseInt item) (int item))) event))))))

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
  (if (get collections collection-id)
    (update collections collection-id add-score-to-collection score-info)
    collections))

(defn remove-score [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc (int score-id)))
;; TODO check for undefined
;; TODO don't write into db if spec is failed
;; TODO to-integer interceptors

(defn move-score [collections [_  score-id old-collection-id new-collection-id]]
    (if (get collections new-collection-id)
      (if-let [score (get-in collections [old-collection-id :scores (int score-id)])]
        (-> collections
            (remove-score [nil old-collection-id score-id])
            (add-score [nil new-collection-id score]))
        (throw (js/Error. (str score-id " score Not found")))) collections))

(def score-manipulation-interceptors [util/check-spec (path :collections) util/->local-store])

(reg-event-db
 :move-score
 (conj score-manipulation-interceptors params->int)
 move-score)

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
 score-manipulation-interceptors
 add-score)

(reg-event-db
 :remove-score
 score-manipulation-interceptors
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
