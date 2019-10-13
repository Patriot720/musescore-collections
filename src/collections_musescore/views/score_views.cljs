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

  [:> mui/Typography {:component "div" :variant "body2"}
   [score-info-item "AR" (get red "500") favorite-count]
   [:> mui-icons/Accessible] playback-count
    ; [score-info-item [:> mui-icons/Accessible] tags]
   ])

(defn grid-item [props & children]
  (into [:> mui/Grid (into {:item true} props)] children))

(defn grid-container [props & children]
  (into [:> mui/Grid (into {:container true} props)] children))

(defn info-count [icon text]
  [:span {:style {:display "inline-flex"
                  :align-items "center"}}
   [:> icon {:font-size "inherit"
             :style {:padding-right 8}}]
   text])

(defn score-view [collection-id {:keys [id title url
                                        creator
                                        favorite-count
                                        playback-count
                                        without-buttons
                                        comment-count
                                        tags]}]
  [:> mui/Card {:style {:display "inline-block"}}
   [:> mui/CardContent {:style {:max-width 200}}
    [:> mui/Typography {:variant "h4" :gutterBottom true}
     title]
    [:> mui/Typography {:component "div" :variant "body1"}
     [grid-container {:spacing 1}
      [grid-item {:xs 6} [:strong creator]]
      [grid-item {:xs 6} [info-count mui-icons/FavoriteBorder favorite-count]]
      [grid-item {:xs 6} [info-count mui-icons/VisibilityOutlined playback-count]]
      [grid-item {:xs 6} [info-count mui-icons/QuestionAnswer comment-count]]]]]
   [:> mui/CardActions {:disable-spacing false}
    [:a {:href url :target "_blank"}
     [:> mui/Button
      {:color "primary"}
      "Go to score"]]
    [:> mui/Button {:color "secondary"
                    :style {:margin-left "auto"}
                    :on-click
                    #(dispatch [:remove-score collection-id id])} "DELETE"]]])