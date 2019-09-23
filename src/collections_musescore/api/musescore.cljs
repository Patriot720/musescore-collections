(ns collections-musescore.api.musescore
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [collections-musescore.macros :refer [slurp]])
  (:require [cljs-http.client :as http]
            [clojure.string]
            [ajax.core :refer [GET]]
            [cljs.core.async :refer [<!]]))

(def query-base "https://cors-anywhere.herokuapp.com/http://api.musescore.com/services/rest/")
(defn parse-url [url] (last (clojure.string/split url #"/")))
(defn get-request-url-from [url]
  (str query-base "score/"
       (parse-url url) ".json?oauth_consumer_key=" (slurp "api_key")))

(defn get-info-by-url
  ([url callback]
   (get-info-by-url url callback js/console.log))
  ([url callback fail-callback]
   (GET
     (get-request-url-from url)
     {:with-credentials false
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
     :params {"text" text
              "oauth_consumer_key" (slurp "api_key")}}))