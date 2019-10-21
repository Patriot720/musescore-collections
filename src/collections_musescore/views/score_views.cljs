(ns collections-musescore.views.score-views
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/core/colors" :refer [red-js]]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.views.util :refer [tab-panel text-field]]
            [collections-musescore.views.inputs :as inputs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

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
      :className "pull-right"
      :on-click
      #(dispatch [:remove-score collection-id id])}
     "DELETE"]]])

(defn- get-suggestion-value [suggestion]
  (.-permalink suggestion))

(defn score-search-view []
  [:div {:className "autosuggest"}
   [:div [inputs/auto-suggest-view {:placeholder "Type  stuff"
                                    :get-suggestion-value get-suggestion-value
                                    :suggestions @(subscribe [:suggestions])
                                    :update-suggestions
                                    #(dispatch [:get-suggestions (.-value %)])
                                    :on-suggestion-selected
                                    #(dispatch   [:get-url-info (.-suggestionValue %2)])
                                    :clear-suggestions #(dispatch [:clear-suggestions])}]]
   [:div {:style {:padding :20px}} [score-view 1 @(subscribe [:url-info])]]
   [:> mui/Button {:variant "contained"
                   :color "primary"
                   :on-click #()} "ADD"]])

(defn add-score-modal [collection-id]
  (let [open? (reagent/atom false)]
    (fn [collection-id]
      [:div
       [:> mui/Button {:variant "contained"
                       :on-click #(reset! open? true)} "Open modal add score"]
       [:> mui/Modal {:on-close #(reset! open? false)
                      :open @open?}
        [:> mui/Paper {:className "add-score-modal"}
         [score-search-view]]]])))
