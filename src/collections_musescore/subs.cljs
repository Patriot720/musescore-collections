(ns collections-musescore.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :collections
 (fn [db _]
   (:collections db)))
