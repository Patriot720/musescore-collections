(ns collections-musescore.score.views
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.views.animation-util :as animation-util]
            [collections-musescore.views.inputs :as inputs]
            [oops.core :refer [oget oset! ocall oapply ocall! oapply!
                               oget+ oset!+ ocall+ oapply+ ocall!+ oapply!+]]
            [collections-musescore.db :as db]
            [cljs.spec.alpha :as s]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(set! *warn-on-infer* true) ;; TODO fix infer errors
(defn- score-info-item [icon text]
  [:span {:className "score-info-item"}
   [:> icon]
   text])

(defn move-to-collection []
  (let [new-collection-id (reagent/atom 0)]
    (fn [old-collection-id score-id]
      [:div
       [inputs/text-field {:value @new-collection-id
                           :on-change #(reset! new-collection-id (oget %2 "props.value"))
                           :select true}
        (for [{:keys [id title]} (vals @(subscribe [:collections]))]
          ^{:key (str "coll-modal-" id)}
          [:> mui/MenuItem {:value id} title])]
       [:> mui/Button {:on-click #(dispatch [:move-score score-id old-collection-id @new-collection-id])} "Move"]])))

(defn move-score-modal []
  (let [open? (reagent/atom false)]
    (fn [collection-id score-id]
      [:div
       [:> mui/Button {:on-click #(reset! open? true)} "Move score"]
       [:> mui/Modal {:on-close #(reset! open? false)
                      :open @open?}
        [:> mui/Paper {:className "score-modal"}
         [move-to-collection collection-id score-id]]]])))

(defn expand-score-modal []
    (fn [open? {:keys [title description] }]
      [:> mui/Modal {:on-close #(reset! open? false)
                     :open @open?}
       [:> mui/Fade {:in @open? :timeout animation-util/animation-length}
        [:> mui/Paper {:className "score-modal"}
         [:> mui/Typography {:variant "h3" :gutterBottom true}
          title]
         [:> mui/Typography {:variant "body1"}
          description]]]]))

(defn score-view []
  (let [score-exists? (reagent/atom true)
        expanded? (reagent/atom false)]
    (fn [collection-id {:keys [id title permalink
                               favoriting_count
                               view_count
                               comment_count]
                        {username :username} :user :as score}]
      [:> mui/Zoom {:in @score-exists? :timeout animation-util/animation-length}
       [:> mui/Card {:className "score-view" :id id}
        [expand-score-modal expanded? score]
        [:> mui/CardContent {:className "score-content"}
         [:> mui/Typography {:variant "h6"  :on-click #(reset! expanded? true) :gutterBottom true}
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
         [move-score-modal collection-id id]
         [:> mui/Button
          {:color "secondary"
           ;; :className "pull-right"
           :on-click (partial animation-util/delete-with-animation #(dispatch [:remove-score collection-id id]) score-exists?)}
          "DELETE"]]]])))

(defn- get-suggestion-value [suggestion]
  (oget suggestion "permalink"))

(defn score-search-form []
  (let [url-info (subscribe [:url-info])]
    (fn [collection-id]
      [:div {:className "autosuggest"}
       [:div [inputs/auto-suggest-view {:placeholder "Type  stuff"
                                        :get-suggestion-value get-suggestion-value
                                        :suggestions @(subscribe [:suggestions])
                                        :on-suggestions-fetch-requested
                                        #(dispatch [:get-search-suggestions (oget % "value")])
                                        :on-suggestion-selected
                                        #(dispatch   [:get-score-by-url (oget %2 "suggestionValue")])
                                        :on-suggestions-clear-requested
                                        #(dispatch [:clear-score-suggestions])}]]
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

