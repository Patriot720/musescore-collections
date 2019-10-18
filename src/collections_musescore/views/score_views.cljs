(ns collections-musescore.views.score-views
  (:require ["@material-ui/core" :as mui]
            [re-frame.core :refer [subscribe dispatch]]
            ["@material-ui/icons" :as mui-icons]
            ["@material-ui/core/colors" :refer [red-js]]))

(def red (js->clj red-js))

(defn- score-info-item [icon text]
  [:span {:className "score-info-item"}
   [:> icon]
   text])

(defn score-view [collection-id {:keys [id title url
                                        creator
                                        favorite-count
                                        views-count
                                        comment-count]}]
  [:> mui/Card {:style {:display "inline-block"}}

   [:> mui/CardContent {:style {:max-width 200}}
    [:> mui/Typography {:variant "h4" :gutterBottom true}
     title]
    [:> mui/Typography {:component "div" :variant "body1"}
     [:> mui/Grid {:container true :spacing 1}
      [:> mui/Grid {:item true :xs 6} [:strong creator]]
      [:> mui/Grid {:item true :xs 6}
       [score-info-item mui-icons/FavoriteBorder favorite-count]]
      [:> mui/Grid {:item true :xs 6}
       [score-info-item mui-icons/VisibilityOutlined views-count]]
      [:> mui/Grid {:item true :xs 6}
       [score-info-item mui-icons/QuestionAnswer comment-count]]]]]

   [:> mui/CardActions {:disable-spacing false}
    [:a {:href url :target "_blank"}
     [:> mui/Button
      {:color "primary"}
      "Go to score"]]
    [:> mui/Button
     {:color "secondary"
      :pull-right true
      :on-click
      #(dispatch [:remove-score collection-id id])}
     "DELETE"]]])
