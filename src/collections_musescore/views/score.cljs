(ns collections-musescore.views.score
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/core/colors" :refer [red-js]]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.views.inputs :as inputs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(defn- score-info-item [icon text]
  [:span {:className "score-info-item"}
   [:> icon]
   text])

(defn trunc
  [s n]
  (if (> (count s) n)
    (str (apply str (take n s)) "...")
    s))

(defn score-view [collection-id {:keys [id title permalink
                                        favoriting_count
                                        view_count
                                        comment_count]
                                 {username :username} :user}]

  [:> mui/Card {:className "score-view"}
   [:> mui/CardContent {:className "score-content"}
    [:> mui/Typography {:variant "h6" :gutterBottom true}
     title]
    [:> mui/Typography {:component "div" :variant "body1"}
     [:> mui/Grid {:container true :spacing 1}
      [:> mui/Grid {:item true :xs 6 } [:strong {:className "username"} username]]
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
      :on-click #(dispatch [:remove-score collection-id id])}
     "DELETE"]]])

(defn- get-suggestion-value [suggestion]
  (.-permalink suggestion))

(defn score-search-form []
  (let [url-info (subscribe [:url-info])]
    (fn [collection-id]
      [:div {:className "autosuggest"}
       [:div [inputs/auto-suggest-view {:placeholder "Type  stuff"
                                        :get-suggestion-value get-suggestion-value
                                        :suggestions @(subscribe [:suggestions])
                                        :update-suggestions
                                        #(dispatch [:get-suggestions (.-value %)])
                                        :on-suggestion-selected
                                        #(dispatch   [:get-url-info (.-suggestionValue %2)])
                                        :clear-suggestions #(dispatch [:clear-suggestions])}]]
       [:div {:style {:padding :20px}}
        [score-view 1 @url-info]]
       [:> mui/Button {:variant "contained"
                       :color "primary"
                       :full-width true
                       :on-click #(dispatch [:add-score collection-id @url-info])} "ADD"]])))

(defn add-score-modal [collection-id]
  (let [open? (reagent/atom false)]
    (fn [collection-id]
      [:div
       [:> mui/Button {:variant "contained"
                       :color "primary"
                       :className "float-right"
                       :on-click #(reset! open? true)} "Add score"]
       [:> mui/Modal {:on-close #(reset! open? false)
                      :open @open?}

        [:> mui/Paper {:className "add-score-modal"}
         (when @(subscribe [:is-score-loading?])
           [:div {:className "loading-score"}  [:> mui/CircularProgress]])
         [:> mui/AppBar  [:> mui/Toolbar {:elevation 0} [:> mui/Typography {:varinat "h4"} "Add score"]]]
         [score-search-form collection-id]]]])))
