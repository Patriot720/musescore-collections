(ns tests.api.api-test
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [collections-musescore.macros :refer [slurp]])
  (:require
   [cljs.test :refer-macros [async are is deftest testing]]
   [collections-musescore.api.musescore :as api]
   [cljs.core.async :refer [<!]]))

(def dummy-info {:title "Game Of Thrones - Main Theme - Piano Arrangement"
                 :likes 623
                 :views 25603})
(def url "https://musescore.com/user/24625996/scores/4801654")
(deftest parse-url
  (is (= "4801654" (api/parse-url url))))

(deftest ^:async get_information_by_url
  (async done
         (go (let [result (<! (api/get-info-by-url url))]
               (is (= dummy-info result)))
             (done))))