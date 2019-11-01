(ns collections-musescore.score.modals
  (:require ["@material-ui/core" :as mui]
            [re-frame.core :refer [subscribe dispatch subscribe]]
            [collections-musescore.views.inputs :as inputs]
            [collections-musescore.score.views :refer [score-view]]
            [reagent.core :as reagent]))

(defn- get-suggestion-value [suggestion]
  (.-permalink suggestion))

(defn- score-search-form []
  (let [url-info (subscribe [:url-info])]
    (fn [collection-id]
      [:div {:className "autosuggest"}
       [:div [inputs/auto-suggest-view {:placeholder "Type  stuff"
                                        :get-suggestion-value get-suggestion-value
                                        :suggestions @(subscribe [:suggestions])
                                        :on-suggestions-fetch-requested 
                                        #(dispatch [:get-search-suggestions (.-value %)])
                                        :on-suggestion-selected
                                        #(dispatch   [:get-score-by-url (.-suggestionValue %2)])
                                        :on-suggestions-clear-requested #(dispatch [:clear-score-suggestions])}]]
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
