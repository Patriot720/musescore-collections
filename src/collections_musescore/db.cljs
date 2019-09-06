(ns collections-musescore.db
  (:require
   [re-frame.core :refer [dispatch dispatch-sync reg-cofx]]
   [cljs.reader]
   [cljs.spec.alpha :as s]))

(def ::title string?)
(def url-regex
  #"https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)")
(def ::url (s/and string? #(re-matches url-regex %)))

(def ::score (s/map-of ::title))
(def ::scores (s/coll-of ::score))
(def ::collection (s/map-of ::title ::scores))
(def ::collections
  (s/coll-of ::collection))

(def ls-key "collections-musescore")                         ;; localstore key

(def default-db           ;; what gets put into app-db by default.
  {:collections   []  ;; an empty list of todos. Use the (int) :id as the key
   })

(defn ->local-store
  "Puts todos into localStorage"
  [todos]
  (.setItem js/localStorage ls-key (str todos)))     ;; sorted-map written as an EDN map

(reg-cofx
 :local-store-collections
 (fn [cofx _]
      ;; put the localstore todos into the coeffect under :local-store-todos
   (assoc cofx :local-store-collections
             ;; read in todos from localstore, and process into a sorted map
          (into (sorted-map)
                (some->> (.getItem js/localStorage ls-key)
                         (cljs.reader/read-string)    ;; EDN map -> map
                         )))))