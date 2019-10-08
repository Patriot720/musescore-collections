(ns collections-musescore.views.score-views
  (:require ["@material-ui/core" :as mui]
            [re-frame.core :refer [subscribe dispatch]]
            ["@material-ui/icons" :as mui-icons]))
(defn score-info-item [icon text]
  [:> mui/Grid {:item true
                :display "flex"
                :alignItems "center"}
   icon text])
(defn score-info [{:keys [favorite-count
                          playback-count
                          tags]}]

  [:> mui/Typography {:variant "body1"}
   [:> mui/Grid {:container true :spacing 2
                 :justify "center"}
    [score-info-item [:> mui-icons/Accessible] favorite-count]
    [score-info-item [:> mui-icons/Accessible] playback-count]
    [score-info-item [:> mui-icons/Accessible] tags]]])


(defn score-view [collection-id {:keys [id title url
                                        favorite-count
                                        playback-count
                                        tags]}]
  [:> mui/Grid {:container true
                :spacing 2
                :alignItems "center"
                :justify "space-evenly"}
   [:> mui/Grid {:container true :item true
                 :direction "column" :xs 4
                 :spacing 2}
    [:> mui/Typography {:align "center"}
     [:> mui/Grid {:item true}
      [:h1  title]]
     [:> mui/Grid {:item true}
      [score-info {:favorite-count favorite-count
                   :playback-count playback-count
                   :tags tags}]]]]
   [:> mui/Grid {:item true}

    [:a {:href url :target "_blank"}
     [:> mui/Button
      {:variant "contained"
       :color "primary"}
      "Go to score"]]]
   [:> mui/Grid {:item true}
    [:> mui/Button {:variant "contained"
                    :color "secondary"
                    :on-click
                    #(dispatch [:remove-score collection-id id])} "DELETE"]]])