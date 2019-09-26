(ns collections-musescore.events.events
  (:require [collections-musescore.db :as db]
            [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx after path dispatch]]
            [collections-musescore.api :refer [get-info-by-url]]
            [clojure.string :as str]
            [collections-musescore.events.score :as score]
            [collections-musescore.events.collection :as collection]
            [collections-musescore.events.interceptors :refer [collections-interceptors check-spec-interceptor]]))


(reg-event-fx
 :get-url-info
 (fn [db [_ url]]
   (get-info-by-url url #(dispatch [:get-url-info-success %]))
   db))

(reg-event-db
 :get-url-info-success
 [(path :temp-url-info)]
 (fn [temp-url-info [_ result]]
   result))

(reg-event-fx                 ;; part of the re-frame API
 :initialise-db              ;; event id being handled

  ;; the interceptor chain (a vector of 2 interceptors in this case)
 [(inject-cofx :local-store-collections) ;; gets todos from localstore, and puts value into coeffects arg
  check-spec-interceptor]          ;; after event handler runs, check app-db for correctness. Does it still match Spec?

  ;; the event handler (function) being registered
 (fn [{:keys [db local-store-collections]} _]                  ;; take 2 values from coeffects. Ignore event vector itself.
   {:db (assoc db/default-db :collections  local-store-collections)}))   ;; all hail the new state to be put in app-db
(def languages [{:name "C"   :year 1972}
                {:name "C#" :year 2000}
                {:name "C++" :year 1983}
                {:name "Clojure" :year 2007}
                {:name "Elm" :year 2012}
                {:name "Go" :year 2009}
                {:name "Haskell" :year 1990}
                {:name "Java" :year 1995}
                {:name "Javascript" :year 1995}
                {:name "Perl" :year 1987}
                {:name "PHP" :year 1995}
                {:name "Python" :year 1991}
                {:name "Ruby" :year 1995}
                {:name "Scala" :year 2003}])


(defn str->regex [a-str]
  (let [escaped (str/replace a-str #"[\+\.\?\[\]\(\)\^\$]" (partial str "\\"))]
    (re-pattern (str "(?i)^" escaped ".*"))))

(reg-event-db
 :get-suggestions
 [(path :suggestions)]
 (fn [suggestions [_ val]]
   (js/console.log val)
   (let [trimmed-val (if (string? val) (str/trim val) "")]
     (if (empty? trimmed-val)
       []
       (into [] (filter (comp #(re-matches  (str->regex trimmed-val) %) :name) languages))))))

(reg-event-db
 :clear-suggestions
 [(path :suggestions)]
 [])


; TODO slow should really do ID map instead of array
(reg-event-db
 :remove-collection
 collections-interceptors
 collection/remove-collection)

(reg-event-db
 :add-collection
 collections-interceptors
 collection/add)

(reg-event-db
 :add-score
 collections-interceptors
 score/add)

(reg-event-db
 :remove-score
 collections-interceptors
 score/remove-from-collections)