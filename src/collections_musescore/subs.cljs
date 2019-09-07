(ns collections-musescore.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
 :collections
 (fn [db _]
   (:collections db)))


