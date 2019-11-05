(ns collections-musescore.score.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :suggestions
 (fn [db _]
   (:suggestions db)))

(reg-sub
 :url-info
 (fn [db _]
   (:temp-url-info db)))

(reg-sub
 :is-score-loading?
 (fn [db _]
   (:score-loading db)))
(reg-sub
 :is-search-score-loading?
 (fn [db _]
   (:search-score-loading db))
 )
