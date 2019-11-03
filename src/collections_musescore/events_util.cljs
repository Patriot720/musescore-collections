(ns collections-musescore.events-util
  (:require [cljs.spec.alpha :as s]
            [collections-musescore.db :as db]
            [re-frame.core :refer [after path]]))

(defn- check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))
    nil))

(def check-spec (partial check-and-throw :collections-musescore.db/db))

(def db-manipulation-interceptors [(after check-spec) (path :collections) (after db/->local-store)])
