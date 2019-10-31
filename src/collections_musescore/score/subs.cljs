(ns collections-musescore.score.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
 :suggestions
 (fn [db _]
   (:suggestions db)))
