(ns collections-musescore.events.collection
  (:require [collections-musescore.events.util :refer [allocate-next-id]]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path dispatch]]))

(defrecord Collection [id title scores])
(defn collection [id title scores]
  (into {} (->Collection id title scores)))



(defn add [collections [_ title]]
  (let [id (allocate-next-id collections)]
    (assoc collections id (collection id title {}))))

(defn remove [collections [_ id]]
  (dissoc collections id))

; TODO slow should really do ID map instead of array
(reg-event-db
 :remove-collection
 collections-interceptors
 remove)

(reg-event-db
 :add-collection
 collections-interceptors
 add)
