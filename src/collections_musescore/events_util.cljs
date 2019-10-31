(ns collections-musescore.events-util
  (:require [cljs.spec.alpha :as s]
            [collections-musescore.db :as db]
            [re-frame.core :refer [after path]]))

(defn- check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec (after (partial check-and-throw :collections-musescore.db/db)))


(def ->local-store (after db/->local-store))

(def check-spec->path:collections->local-store [check-spec
                               (path :collections)
                               ->local-store])
