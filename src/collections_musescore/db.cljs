(ns collections-musescore.db
  (:require
   [re-frame.core :refer [dispatch dispatch-sync reg-cofx]]
   [cljs.reader]
   [cljs.spec.alpha :as s]))

(s/def ::title string?)
; (ss//def url-regex
; s/  #"https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)")
(s/def ::url string?)

(s/def ::score (s/or :map (s/keys :req-un [::title ::url]) :nil nil?))
(s/def ::scores (s/or  :empty empty? :coll-of (s/coll-of ::score)))
(s/def ::collection (s/or  :empty empty? :coll-of (s/keys :req-un [::title ::scores])))
(s/def ::collections
  (s/coll-of ::collection))
(s/def ::db (s/keys :req-un [::collections]))
(def ls-key "collections-musescore")                         ;; localstore key

(def default-db           ;; what gets put into app-db by default.
  {:collections   []  ;; an empty list of todos. Use the (int) :id as the key
   })

(defn ->local-store
  "Puts collections into localStorage"
  [collections]
  (.setItem js/localStorage ls-key (str collections)))     ;; sorted-map written as an EDN map

(defn get-from-local-store []
  (some->> (.getItem js/localStorage ls-key)
           (cljs.reader/read-string)    ;; EDN map -> map
           ))

(defn local-store-collections-cofx  [cofx _]
      ;; put the localstore todos into the coeffect under :local-store-todos
  (assoc cofx :local-store-collections
             ;; read in todos from localstore, and process into a sorted map
         (get-from-local-store)))
(reg-cofx
 :local-store-collections
 local-store-collections-cofx)