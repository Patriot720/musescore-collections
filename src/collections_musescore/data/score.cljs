(ns collections-musescore.data.score
  (:require clojure.string))

(defn- add-to-collection [collection score-info]
  (assoc-in collection [:scores (int (:id score-info))]  score-info))

(defn add [collections [_ collection-id score-info]]
  (update collections collection-id add-to-collection score-info))

(defn remove-from-collections [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc (int score-id)))

(defn- score-title-contains? [query score]
  (when (:title score) (clojure.string/includes? (:title score) query)))

(defn search-scores [{collections :collections :as db} [_ query]]
  (let [collections-vals (vals collections)]
    (assoc db :search-results (flatten (map
                                        (fn [{:keys [scores]}]
                                          (let [scores (vals scores)] (filter (partial score-title-contains? query) scores)))
                                        collections-vals)))))

(defn update-url-info [temp-url-info [_ result]]
  (js->clj result))
