(ns collections-musescore.collection.events
  (:require
            [collections-musescore.events-util :as util]
            [re-frame.core :refer [reg-event-db path]]))

(defrecord Collection [id title scores])
(defn collection [id title scores]
  (into {} (->Collection id title scores)))


(defn allocate-next-id
  "Returns the next todo id.
  Assumes todos are sorted.
  Returns one more than the current largest id."
  [todos]
  ((fnil inc 0) (last (keys todos))))

(defn add-collection [collections [_ title]]
  (let [id (allocate-next-id collections)]
    (assoc collections id (collection id title {}))))

(defn remove-collection [collections [_ id]]
  (dissoc collections id))

(reg-event-db
 :remove-collection
 [util/check-spec (path :collections) util/->local-store]
 remove-collection)

(reg-event-db
 :add-collection
 [util/check-spec (path :collections) util/->local-store]
 add-collection)
