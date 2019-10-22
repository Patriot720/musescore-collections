(ns collections-musescore.events.score
  (:require
   [collections-musescore.events.util :refer [allocate-next-id]]))


(defn- add-to-collection [collection score-info]
  (assoc-in collection [:scores (int (:id score-info))]  score-info))

(defn add [collections [_ collection-id score-info]]
  (update collections collection-id add-to-collection score-info))

(defn remove-from-collections [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc score-id))

(defn update-url-info [temp-url-info [_ result]]
  (js->clj result))
