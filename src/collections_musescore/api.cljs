(ns collections-musescore.api
  (:require-macros
   [collections-musescore.macros :refer [slurp]])
  (:require
   [clojure.string]
   [ajax.core :refer [GET]]))

(def query-base "https://cors-anywhere.herokuapp.com/http://api.musescore.com/services/rest/")
(defn parse-url [url] (last (clojure.string/split url #"/")))

(defn query ([endpoint params callback]
             (query endpoint params callback js/console.log))
  ([endpoint params callback fail-callback]
   (GET
     (str query-base endpoint)
     {:params (into params {"oauth_consumer_key" (slurp "api_key")})
      :with-credentials false
      :handler callback
      :response-format :json
      :keywords? true
      :error-handler fail-callback})))

(defn get-info-by-url
  ([url callback]
   (query (str "score/" (parse-url url) ".json")
          {}
          callback))
  ([url callback fail-callback]
   (query (str "score/" (parse-url url) ".json")
          {}
          callback)))

(defn search-score ([text callback]
                    (query "score.json" {"text" text} callback)))