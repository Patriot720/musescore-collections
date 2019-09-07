(ns collections-musescore.db
  (:require
   [re-frame.core :refer [dispatch dispatch-sync reg-cofx]]
   [cljs.reader]
   [cljs.spec.alpha :as s]))

(s/def ::title string?)
; (ss//def url-regex
; s/  #"https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)")
(s/def ::url (s/and string?))

(s/def ::score (s/map-of ::title ::url))
(s/def ::scores #(or  empty? (s/coll-of ::score)))
(s/def ::collection #(or  empty? (s/map-of ::title ::scores)))
(s/def ::collections
  (s/coll-of ::collection))
(s/def ::db (s/keys :req [::collections]))
(def ls-key "collections-musescore")                         ;; localstore key

(def default-db           ;; what gets put into app-db by default.
  {:collections   []  ;; an empty list of todos. Use the (int) :id as the key
   })

(defn ->local-store
  "Puts collections into localStorage"
  [collections]
  (.setItem js/localStorage ls-key (str collections)))     ;; sorted-map written as an EDN map

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