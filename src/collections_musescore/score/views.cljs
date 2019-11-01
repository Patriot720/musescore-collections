(ns collections-musescore.score.views
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.views.animation-util :as animation-util]
            [reagent.core :as reagent]))

(defn- score-info-item [icon text]
  [:span {:className "score-info-item"}
   [:> icon]
   text])

(defn score-view []
  (let [score-exists? (reagent/atom true)]
    (fn [collection-id {:keys [id title permalink
                               favoriting_count
                               view_count
                               comment_count]
                        {username :username} :user}]
      [:> mui/Zoom {:in @score-exists? :timeout animation-util/animation-length}
       [:> mui/Card {:className "score-view" :id id}
        [:> mui/CardContent {:className "score-content"}
         [:> mui/Typography {:variant "h6" :gutterBottom true}
          title]
         [:> mui/Typography {:component "div" :variant "body1"}
          [:> mui/Grid {:container true :spacing 1}
           [:> mui/Grid {:item true :xs 6} [:strong {:className "username"} username]]
           [:> mui/Grid {:item true :xs 6}
            [score-info-item mui-icons/FavoriteBorder favoriting_count]]
           [:> mui/Grid {:item true :xs 6}
            [score-info-item mui-icons/VisibilityOutlined view_count]]
           [:> mui/Grid {:item true :xs 6}
            [score-info-item mui-icons/QuestionAnswer comment_count]]]]]

        [:> mui/CardActions {:disable-spacing false}
         [:a {:href permalink :target "_blank"}
          [:> mui/Button
           {:color "primary"}
           "Go to score"]]
         [:> mui/Button
          {:color "secondary"
           :className "pull-right"
           :on-click (partial animation-util/delete-with-animation #(dispatch [:remove-score collection-id id]) score-exists?)}
          "DELETE"]]]])))


