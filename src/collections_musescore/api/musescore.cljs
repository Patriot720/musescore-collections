(ns collections-musescore.api.musescore
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [collections-musescore.macros :refer [slurp]])
  (:require [cljs-http.client :as http]
            [clojure.string]
            [ajax.core :refer [GET]]
            [cljs.core.async :refer [<!]]))

(def query-base "https://cors-anywhere.herokuapp.com/http://api.musescore.com/services/rest/")
(defn parse-url [url] (last (clojure.string/split url #"/")))

(defn query ([endpoint params callback]
             (query endpoint params callback js/console.log))
  ([endpoint params callback fail-callback]
   (GET
     (str query-base endpoint)
     {:params (into params {"oath_consumer_key" (slurp "api_key")})
      :with-credentials false
      :handler callback
      :response-format :json
      :keywords? true
      :error-handler fail-callback})))

(defn get-info-by-url
  ([url callback]
   (get-info-by-url url callback js/console.log))
  ([url callback fail-callback]
   (GET
     (str query-base "score/" (parse-url url) ".json") ; dif
     {:params {"oauth_consumer_key" (slurp "api_key")}
      :with-credentials false
      :handler callback
      :response-format :json
      :keywords? true
      :error-handler fail-callback})))

(defn search-score [text callback]
  (GET
    (str query-base "score.json")
    {:handler callback
     :response-format :json
     :keywords? true
     :error-handler js/console.log
     :params {"text" text ;dif
              "oauth_consumer_key" (slurp "api_key")}}))