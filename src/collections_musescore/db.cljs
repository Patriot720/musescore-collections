(ns collections-musescore.db
  (:require
   [re-frame.core :refer [dispatch dispatch-sync reg-cofx]]
   [cljs.reader]
   [cljs.spec.alpha :as s]))

(s/def ::title string?)
(s/def ::url string?)

(s/def ::score (s/or :map (s/keys :req-un [::title ::url]) :nil nil?))
(s/def ::scores (s/or  :empty empty? :map-of (s/map-of number? ::score)))
(s/def ::collection (s/or  :empty empty? :coll-of (s/keys :req-un [::title ::scores])))
(s/def ::collections
  (s/map-of number? ::collection))
(s/def ::db (s/keys :req-un [::collections]))
(def localstore-key "collections-musescore")                         ;; localstore key

(def default-db           ;; what gets put into app-db by default.
  {:collections   {}  ;; an empty list of todos. Use the (int) :id as the key
   :temp-url-info {}
   :suggestions []})

(defn ->local-store
  "Puts collections into localStorage"
  [collections]
  (.setItem js/localStorage localstore-key (str collections)))

(defn get-from-local-store []
  (into {} (some->> (.getItem js/localStorage localstore-key)
                    (cljs.reader/read-string)    ;; EDN map -> map
                    )))

(defn local-store-collections-cofx  [cofx _]
      ;; put the localstore todos into the coeffect under :local-store-todos
  (assoc cofx :local-store-collections
             ;; read in todos from localstore, and process into a sorted map
         (get-from-local-store)))

(reg-cofx
 :local-store-collections
 local-store-collections-cofx)

(defn initialize-db [{:keys [_ local-store-collections]}]
  {:db (assoc default-db :collections  local-store-collections)})
