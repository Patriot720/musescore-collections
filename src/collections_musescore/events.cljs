(ns collections-musescore.events
  (:require-macros [collections-musescore.macros :refer [slurp]])
  (:require [collections-musescore.db :as db]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [clojure.string]
            [cljs.spec.alpha :as s]))

(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :collections-musescore.db/db)))

(def ->local-store (after db/->local-store))
(def collections-interceptors [check-spec-interceptor
                               (path :collections)
                               ->local-store])


(defrecord Collection [id title scores])
(defn collection [id title scores]
  (into {} (->Collection id title scores)))
(defrecord Score [id title url])
(defn score [id title url]
  (into {} (->Score id title url)))
(defn- get-next-id [array]
  (count array))

(defn allocate-next-id
  "Returns the next todo id.
  Assumes todos are sorted.
  Returns one more than the current largest id."
  [todos]
  ((fnil inc 0) (last (keys todos))))

(defn add-collection [collections [_ title]]
  (let [id (allocate-next-id collections)]
    (assoc collections id (collection id title {}))))

(defn- add-score-to-collection [collection title url]
  (let [id (allocate-next-id (:scores collection))]
    (assoc-in collection [:scores id]  (score id title url))))

; (defn- collection-title-equals? [collection title]
;   (= (:title collection) title))

(defn add-score [collections [_ id score-title url]]
  (println id)
  (update collections id add-score-to-collection score-title url))

(defn remove-collection [collections [_ id]]
  (dissoc collections id))

; TODO slow should really do ID map instead of array
(reg-event-db
 :remove-collection
 collections-interceptors
 remove-collection)
(reg-event-db
 :add-score
 collections-interceptors
 add-score)
(reg-event-db
 :add-collection
 collections-interceptors
 add-collection)

(defn parse-url [url] (last (clojure.string/split url #"/")))

; (reg-event-fx
;  :get-url-info
;  (fn [{:keys [db]} _ url]
;    {:http-xhrio {:method :get
;                  :url (str "https://cors-anywhere.herokuapp.com/http://api.musescore.com/services/rest/score/"
;                            (parse-url url) ".json?oauth_consumer_key=" (slurp "api_key"))
;                  :response-format (ajax/json-response-format {:keywords? true})
;                  :on-success [:update-temp-url-info]}}))

; (reg-event-db
;  :update-temp-url-info
;  [(path :temp-url-info)]
;  (fn [temp-url-info [_ result]]
;    (:body result)))

(reg-event-fx                 ;; part of the re-frame API
 :initialise-db              ;; event id being handled

  ;; the interceptor chain (a vector of 2 interceptors in this case)
 [(inject-cofx :local-store-collections) ;; gets todos from localstore, and puts value into coeffects arg
  check-spec-interceptor]          ;; after event handler runs, check app-db for correctness. Does it still match Spec?

  ;; the event handler (function) being registered
 (fn [{:keys [db local-store-collections]} _]                  ;; take 2 values from coeffects. Ignore event vector itself.
   {:db (assoc db/default-db :collections  local-store-collections)}))   ;; all hail the new state to be put in app-db

(defn remove-score [scores score-id]
  (dissoc scores score-id))

(defn remove-score-from-collections [collections [_ collection-id score-id]]
  (update-in collections [collection-id :scores] dissoc score-id))

(reg-event-db
 :remove-score
 collections-interceptors
 remove-score-from-collections)