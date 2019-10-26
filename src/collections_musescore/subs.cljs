(ns collections-musescore.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))
(reg-sub
 :collections
 (fn [db _]
   (:collections db)))

(reg-sub
 :url-info
 (fn [db _]
   (:temp-url-info db)))
(reg-sub
 :is-score-loading?
 (fn [db _]
   (:score-loading db)))
(reg-sub
 :suggestions
 (fn [db _]
   (:suggestions db)))
