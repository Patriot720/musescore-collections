(ns collections-musescore.data.collection)

(defrecord Collection [id title scores])
(defn collection [id title scores]
  (into {} (->Collection id title scores)))


(defn allocate-next-id
  "Returns the next todo id.
  Assumes todos are sorted.
  Returns one more than the current largest id."
  [todos]
  ((fnil inc 0) (last (keys todos))))

(defn add [collections [_ title]]
  (let [id (allocate-next-id collections)]
    (assoc collections id (collection id title {}))))

(defn remove-collection [collections [_ id]]
  (dissoc collections id))
