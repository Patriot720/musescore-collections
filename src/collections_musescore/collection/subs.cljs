(ns collections-musescore.collection.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :collections
 (fn [db _]
   (:collections db)))
