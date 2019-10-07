(ns collections-musescore.views.score-views
  (:require ["@material-ui/core" :as mui]
            [re-frame.core :refer [subscribe dispatch]]
            ["@material-ui/icons" :as mui-icons]))

(defn score-info [{:keys [favorite-count
                          playback-count
                          tags]}]
  [:> mui/Grid {:container true}
   [:> mui/Grid {:item true}
    [mui-icons/Accessible] favorite-count]
   [:> mui/Grid {:item true}
    [mui-icons/Accessible] playback-count]
   [:> mui/Grid {:item true} ; TODO to a loop
    [mui-icons/Accessible] tags]])


(defn score-view [collection-id {:keys [id title url
                                        favorite-count
                                        playback-count
                                        tags]}]
  [:a {:href url :target "_blank"}
   [:> mui/Grid {:container true
                 :alignItems "center"
                 :justify "space-evenly"}
    [:> mui/Grid {:item true}
     [:h1 title]]
    [:> mui/Grid {:item true}
     [score-info {:favorite-count favorite-count
                  :playback-count playback-count
                  :tags tags}]]
    [:> mui/Grid {:item true}
     [:> mui/Button {:on-click
                     #(dispatch [:remove-score collection-id id])} "DELETE"]]]])