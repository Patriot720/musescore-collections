(ns collections-musescore.search.events
  (:require clojure.string
            [re-frame.core :refer [reg-event-db]]))

(defn- score-title-contains? [query score]
  (when (:title score) (clojure.string/includes? (:title score) query)))

(defn search-score [collections-vals test-function query]
    (flatten (map
              (fn [{:keys [scores]}]
                (let [scores (vals scores)] (filter (partial test-function query) scores)))
              collections-vals)))

(defn search-local-scores [{collections :collections :as db} [_ query]]
    (assoc db :search-results (search-score (vals collections) score-title-contains? query)))

(reg-event-db
 :search-local-scores
 search-local-scores)
