(ns collections-musescore.events.interceptors
  (:require [cljs.spec.alpha :as s]
            [collections-musescore.db :as db]
            [collections-musescore.api :as api]
            [re-frame.core :refer [after path dispatch ->interceptor]]))

(defn- check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :collections-musescore.db/db)))

(defn- score-interceptor [{{[_ collection-id score-id _ url] :events} :coeffects}]
  (api/get-info-by-url url #(dispatch [:update-score collection-id score-id %])))

(def add-score-intereptor (->interceptor
                           :id :add-score-interceptor
                           :after))

(def ->local-store (after db/->local-store))
(def collections-interceptors [check-spec-interceptor
                               (path :collections)
                               ->local-store])
