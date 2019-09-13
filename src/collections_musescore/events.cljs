(ns collections-musescore.events
  (:require [collections-musescore.db :as db]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path]]
            [cljs.spec.alpha :as s]))

(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :collections-musescore.db/db)))

(def ->local-store (after db/->local-store))
(def collections-interceptors [(path :collections)
                               ->local-store])

(defn allocate-collection-id [collections]
  (count collections))

(defrecord Collection [title scores])
(defn collection [title scores]
  (into {} (->Collection title scores)))
(defrecord Score [title url])
(defn score [title url]
  (into {} (->Score title url)))

(defn add-collection [collections [_ title]]
  (conj collections (collection title [])))

(defn- add-score-to-collection [collection title url]
  (update collection :scores #(conj % (score title url))))

(defn- collection-title-equals? [collection title]
  (= (:title collection) title))

(defn add-score [collections [_ title score-title url]]
  (map (fn [item]
         (if (collection-title-equals? item title)
           (add-score-to-collection item score-title url)
           item)) collections))
; TODO slow should really do ID map instead of array
(reg-event-db
 :add-score
 collections-interceptors
 add-score)
(reg-event-db
 :add-collection
 collections-interceptors
 add-collection)

(reg-event-fx                 ;; part of the re-frame API
 :initialise-db              ;; event id being handled

  ;; the interceptor chain (a vector of 2 interceptors in this case)
 [(inject-cofx :local-store-collections) ;; gets todos from localstore, and puts value into coeffects arg
  check-spec-interceptor]          ;; after event handler runs, check app-db for correctness. Does it still match Spec?

  ;; the event handler (function) being registered
 (fn [{:keys [db local-store-collections]} _]                  ;; take 2 values from coeffects. Ignore event vector itself.
   {:db (assoc db/default-db :collections (if (empty?  local-store-collections) [] local-store-collections))}))   ;; all hail the new state to be put in app-db

(defn remove-score [scores score-title]
  (println score-title)
  (remove (fn [item]
            (= (:title item) score-title)) scores))

(defn remove-score-from-collections [collections [_ collection-title score-title]]
  (map (fn [collection]
         (if (= (:title collection) collection-title)
           (update collection :scores remove-score score-title) collection)) collections))

(reg-event-db
 :remove-score
 collections-interceptors
 remove-score-from-collections)