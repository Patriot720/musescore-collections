(ns collections-musescore.events.score
  (:require [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path dispatch]]
            [collections-musescore.events.interceptors :refer [collections-interceptors]]
            [collections-musescore.events.util :refer [allocate-next-id]]))


(defrecord Score [id title url])
(defn score [id title url]
  (into {} (->Score id title url)))


(defn- add-to-collection [collection title url]
  (let [id (allocate-next-id (:scores collection))]
    (assoc-in collection [:scores id]  (score id title url))))

(defn add [collections [_ id score-title url]]
  (println id)
  (update collections id add-to-collection score-title url))


(defn remove-from-collections [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc score-id))