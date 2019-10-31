(ns collections-musescore.search.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :search-results
 (fn [db _]
(:search-results db)
   ))
