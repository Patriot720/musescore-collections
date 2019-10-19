(ns collections-musescore.views.score-views
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/core/colors" :refer [red-js]]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.autosuggest :as autosuggest]
            [collections-musescore.views.util :refer [tab-panel text-field]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(def red (js->clj red-js))

(defn- score-info-item [icon text]
  [:span {:className "score-info-item"}
   [:> icon]
   text])

(defn score-form [collection-id]
  (let [title (reagent/atom  "")
        url (reagent/atom  "")
        save #(dispatch [:add-score collection-id @title @url])
        stop #(reset! title "")]
    (fn [collection-title]
      [:> mui/Grid {:container true :spacing 3}
       [:> mui/Grid {:item true} [text-field
                                  {:value @title
                                   :label "Add score"
                                   :on-change   #(reset! title (-> % .-target .-value))}]]
       [:> mui/Grid {:item true} [text-field
                                  {:label "url"
                                   :value @url
                                   :on-change   #(reset! url (-> % .-target .-value))
                                   :on-key-down #(case (.-which %)
                                                   13 (save)
                                                   27 (stop)
                                                   nil)}]]
       [:> mui/Grid {:item true} [:> mui/Button {:variant "contained"
                                                 :color "primary"
                                                 :on-click #(save)} "ADD"]]])))

(defn add-score-by-url-form [collection-id]
  (let [url (reagent/atom "")] ; TODO move to re-frame standart library
    (fn [collection-id]
      [:div
       [:> mui/Typography {:component "h4"} @(subscribe [:url-info])]
       [text-field {:value @url
                    :label "URL"
                    :on-change (fn [event]
                                 (let [value (-> event .-target .-value)]
                                   (reset! url value)
                                   (dispatch [:get-url-info value])))}]])))

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
   [:div [autosuggest/auto-suggest-view {:placeholder "Type  stuff"
                                         :get-suggestion-value get-suggestion-value
                                         :suggestions @(subscribe [:suggestions])
                                         :update-suggestions  #(dispatch [:get-suggestions (.-value %)])
                                         :on-suggestion-selected #(dispatch   [:get-url-info (.-suggestionValue %2)])
                                         :clear-suggestions #(dispatch [:clear-suggestions])}]]
   [:div {:style {:padding :20px}} [score-view 1 @(subscribe [:url-info])]]
   [:> mui/Button {:variant "contained"
                   :color "primary"
                   :on-click #()} "ADD"]])

(defn add-score-modal [collection-id]
  (let [open? (reagent/atom false)
        tab-value (reagent/atom 0)]
    (fn [collection-id]
      [:div
       [:> mui/Button {:variant "contained"
                       :on-click #(reset! open? true)} "Open modal add score"]
       [:> mui/Modal {:on-close #(reset! open? false)
                      :open @open?}
        [:> mui/Paper {:className "add-score-modal"}
         [:div
          [:> mui/Tabs {:value @tab-value :on-change #(reset! tab-value %2)}
           [:> mui/Tab {:label "Manual"}]
           [:> mui/Tab {:label "Search or url"}]]
          [tab-panel @tab-value 0
           [score-form collection-id]]
          [tab-panel @tab-value 1
           [score-search-view]]]]]])))
