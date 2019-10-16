(ns collections-musescore.views.inputs
  (:require ["@material-ui/core" :as mui]
            [collections-musescore.autosuggest :as autosuggest]
            [collections-musescore.views.util :refer [tab-panel text-field]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(defn input-form [{:keys [dispatch-key label button-text]}]
  (let [title (reagent/atom "")
        stop #(reset! title "")
        save #(dispatch [dispatch-key @title])]
    (fn []
      [:> mui/Grid {:item true}
       [text-field
        {:value @title :label label :rows 10
                    :on-change #(reset! title (-> % .-target .-value))
                    :on-key-down #(case (.-which %)
                                    13 (save)
                                    27 (stop)
                                    nil)}
        ]
       [:> mui/Button {:variant "contained"
                       :color "primary" ;; TODO button styles deconstruction
                       :on-click #(save)} button-text]])))

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

(defn score-modal [collection-id]
  (let [open? (reagent/atom false)
        tab-value (reagent/atom 0)]
    (fn [collection-id]
      [:div
       [:> mui/Button {:variant "contained"
                       :on-click #(reset! open? true)} "Open modal add score"]
       [:> mui/Modal {:on-close #(reset! open? false)
                      :open @open?}
        [:> mui/Paper {:className "score-modal"}
         [:div
          [:> mui/Tabs {:value @tab-value :on-change #(reset! tab-value %2)}
           [:> mui/Tab {:label "Manual"}]
           [:> mui/Tab {:label "Search or url"}]
           [tab-panel @tab-value 0
            [score-form collection-id]]
           [tab-panel @tab-value 1
            [autosuggest/home-page]]]]]]])))
