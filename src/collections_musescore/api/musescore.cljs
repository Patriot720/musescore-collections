(ns collections-musescore.api.musescore
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [collections-musescore.macros :refer [slurp]])
  (:require [cljs-http.client :as http]
            [clojure.string]
            [ajax.core :refer [GET]]
            [cljs.core.async :refer [<!]]))


(defn parse-url [url] (last (clojure.string/split url #"/")))
(defn get-request-url-from [url]
  (str "https://cors-anywhere.herokuapp.com/http://api.musescore.com/services/rest/score/"
       (parse-url url) ".json?oauth_consumer_key=" (slurp "api_key")))
(defn get-stuff [url]
  (http/get (get-request-url-from url)
            {:with-credentials? false
             :headers {}}))

(defn get-info-by-url [url]
  (go (:body (<! (get-stuff url)))))

(defn new-get-info-by-url [url callback]
  (GET
    (get-request-url-from url)
    {:with-credentials false
     :handler callback
     :response-format :json
     :keywords? true
     :error-handler js/console.log}))