(ns collections-musescore.events.collection
  (:require [collections-musescore.events.util :refer [allocate-next-id]]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path dispatch]]))

(defrecord Collection [id title scores])
(defn collection [id title scores]
  (into {} (->Collection id title scores)))



(defn add [collections [_ title]]
  (let [id (allocate-next-id collections)]
    (assoc collections id (collection id title {}))))

(defn remove-collection [collections [_ id]]
  (dissoc collections id))