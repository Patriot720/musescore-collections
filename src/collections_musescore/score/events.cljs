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
(def ^:private add-search-score-loading-status (enrich #(assoc % :search-score-loading true)))
(def ^:private remove-search-score-loading-status (enrich #(dissoc % :search-score-loading)))

(defn get-search-suggestions [status [_ title]]
  (api/search-score title #(dispatch [:update-search-suggestions %]))
  {})

(defn update-search-suggestions [suggestions [_ result]] result)

(defn get-score-by-url [status [_ url]]
  (api/get-info-by-url url #(dispatch [:update-temp-score %]) #(dispatch [:fail-link-temp-score %]))
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

;; TODO preconditions postconditions
;; TODO check for undefined
;; TODO don't write into db if spec is failed

(defn move-score [collections [_  score-id old-collection-id new-collection-id]]
  {:pre [(int? score-id)]}
  (if (get collections new-collection-id)
    (if-let [score (get-in collections [old-collection-id :scores (int score-id)])]
      (-> collections
          (remove-score [nil old-collection-id score-id])
          (add-score [nil new-collection-id score]))
      (throw (js/Error. (str score-id " score Not found")))) collections))

(reg-event-db
 :move-score
 (conj util/db-manipulation-interceptors params->int)
 move-score)

(reg-event-fx
 :get-search-suggestions
 [add-search-score-loading-status]
 get-search-suggestions)

(reg-event-db
 :update-search-suggestions
 [remove-search-score-loading-status (path :suggestions)]
 update-search-suggestions)

(reg-event-fx
 :get-score-by-url
 [add-score-loading-status]
 get-score-by-url)

(reg-event-db
 :add-score
 util/db-manipulation-interceptors
 add-score)

(reg-event-db
 :remove-score
 util/db-manipulation-interceptors
 remove-score)

(reg-event-db
 :update-temp-score
 [remove-score-loading-status
  (path :temp-url-info)]
 update-temp-score)

(reg-event-db
 :fail-link-temp-score
 [remove-score-loading-status]
 (fn [db &args] db))

(reg-event-db
 :clear-score-suggestions
 [(path :suggestions)]
 (fn [& args] []))
