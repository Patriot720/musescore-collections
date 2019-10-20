(ns collections-musescore.events.score
  (:require
   [collections-musescore.events.util :refer [allocate-next-id]]))

(defrecord Score [id title url favorite-count
                  comment-count
                  views-count creator])

(defn score
  ([{:keys [title permalink id favoriting_count comment_count view_count metadata]}]
   (into {} (->Score id title permalink favoriting_count comment_count view_count (:poet metadata))))
  ([id title url]
   {:id id :title title :url url}))

(defn- add-to-collection [collection title url]
  (let [id (allocate-next-id (:scores collection))]
    (assoc-in collection [:scores id]  (score id title url))))

(defn add [collections [_ id score-title url]]
  (println id)
  (update collections id add-to-collection score-title url))

(defn remove-from-collections [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc score-id))

(defn update-url-info [temp-url-info [_ result]]
  (score  (js->clj result)))
