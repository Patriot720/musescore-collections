(ns collections-musescore.search.events
  (:require clojure.string
            [re-frame.core :refer [reg-event-db]]))

(defn- score-title-contains? [query score]
  (when (:title score) (clojure.string/includes? (:title score) query)))

(defn search-local-scores [{collections :collections :as db} [_ query]]
  (let [collections-vals (vals collections)]
    (assoc db :search-results (flatten (map
                                        (fn [{:keys [scores]}]
                                          (let [scores (vals scores)] (filter (partial score-title-contains? query) scores)))
                                        collections-vals)))))


(reg-event-db
 :search-local-scores
 search-local-scores)
