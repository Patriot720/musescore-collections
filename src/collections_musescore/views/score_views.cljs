(ns collections-musescore.views.score-views
  (:require ["@material-ui/core" :as mui]
            [re-frame.core :refer [subscribe dispatch]]
            ["@material-ui/icons" :as mui-icons]
            ["@material-ui/core/colors" :refer [red-js]]))

(def red (js->clj red-js))
(defn score-info-item [type color value]
  (js/console.log color)
  [:> mui/Paper {:style {:padding 5 ; todo move to styles
                         :display "inline-block"
                         :background color :color "white"}}
   [:span {:style {:padding-right 5}} type]
   value])
(defn score-info [{:keys [favorite-count
                          playback-count
                          tags]}]

  [:> mui/Typography {:variant "body2"}
   [score-info-item "AR" (get red "500") favorite-count]
   [:> mui-icons/Accessible] playback-count
    ; [score-info-item [:> mui-icons/Accessible] tags]
   ])


(defn score-view [collection-id {:keys [id title url
                                        favorite-count
                                        playback-count
                                        tags]}]
  [:> mui/Card {:style {:display "inline-block"
                        :spacing 2}}
   [:> mui/CardContent
    [:> mui/Typography
     [:h1  title]]
    [score-info {:favorite-count favorite-count
                 :playback-count playback-count
                 :tags tags}]]
   [:> mui/CardActions
    [:a {:href url :target "_blank"}
     [:> mui/Button
      {:variant "contained"
       :color "primary"}
      "Go to score"]]
    [:> mui/Button {:variant "contained"
                    :color "secondary"
                    :on-click
                    #(dispatch [:remove-score collection-id id])} "DELETE"]]])