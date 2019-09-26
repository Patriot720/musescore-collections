(ns collections-musescore.views.inputs
  (:require
   [reagent.core :as reagent]
   ["@material-ui/core" :as mui]
   ["@material-ui/core/colors" :as mui-colors]
   ["@material-ui/icons" :as mui-icons]
   cljsjs.react-autosuggest
   [collections-musescore.views.mui-fix :refer [text-field]]
   [collections-musescore.api :refer [get-info-by-url]]
   [re-frame.core :refer [subscribe dispatch]]))


(defn render-suggestion [text]
  (reagent/as-element
   [:div text]))

(def Autosuggest (reagent/adapt-react-class js/Autosuggest)) ; TOdo refactor to :> ?

(defn autosuggest-view []
  (let [as-val (reagent/atom "")
        update-state-val (fn [evt new-val method]
                           (reset! as-val (.-newValue new-val))
                           nil)]
    (fn [{:keys [suggestions
                 on-suggestion-update-requested
                 render-suggestion]}]

      [Autosuggest {:suggestions @suggestions
                    :onSuggestionsUpdateRequested on-suggestion-update-requested
                    :renderSuggestion render-suggestion
                    :inputProps {:placeholder "Type 'c'"
                                 :value @as-val
                                 :onChange update-state-val}}])))

(defn add-collection-form []
  (let [title (reagent/atom "")
        save #(dispatch [:add-collection @title])
        stop #(reset! title "")]
    (fn []
      [:> mui/Grid {:item true}
       [text-field
        {:value @title
         :label "Add collection"
         :rows 10
         :on-change   #(reset! title (-> % .-target .-value))
         :on-key-down #(case (.-which %)
                         13 (save)
                         27 (stop)
                         nil)}]
       [:> mui/Button {:variant "contained"
                       :color "primary"
                       :on-click #(save)} "ADD"]])))

(defn tab-panel [value index & children]
  [:> mui/Typography {:component "div"
                      :role "tabpanel"
                      :hidden (not (= value index))}
   (into [:> mui/Box {:p 3}] children)])

(defn add-score-form [collection-id]
  (let [title (reagent/atom  "")
        url (reagent/atom  "")
        save #(dispatch [:add-score collection-id @title @url])
        stop #(reset! title "")]
    (fn [collection-title]
      [:> mui/Grid {:container true
                    :spacing 3}
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
           [:> mui/Tab {:label "By URL"}]
           [:> mui/Tab {:label "Search"}]
           [:> mui/Tab {:label "3one"}]]
          [tab-panel @tab-value 0
           [add-score-form collection-id]]
          [tab-panel @tab-value 1
           [add-score-by-url-form collection-id]]
          [tab-panel @tab-value 2
           [autosuggest-view (subscribe [:suggestions]) #(dispatch [:get-suggestions %]) render-suggestion]]]]]])))
