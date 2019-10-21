(ns collections-musescore.events.score
  (:require
   [collections-musescore.events.util :refer [allocate-next-id]]))

(defrecord Score [id title permalink favoriting_count
                  comment_count
                  view_count poet])

(defn score
  ([{:keys [title permalink id favoriting_count comment_count view_count] {poet :poet} :metadata}]
   (into {} (->Score (int id) title permalink favoriting_count comment_count view_count poet)))
  ([id title url]
   {:id id :title title :url url}))

(defn- add-to-collection [collection score-info]
  (assoc-in collection [:scores (int (:id score-info))]  (score score-info)))

(defn add [collections [_ collection-id score-info]]
  (update collections collection-id add-to-collection score-info))

(defn remove-from-collections [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc score-id))

(defn update-url-info [temp-url-info [_ result]]
  (score  (js->clj result)))
