(ns collections-musescore.views.inputs
  (:require ["@material-ui/core" :as mui]
            [collections-musescore.views.util :refer [tab-panel text-field]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            cljsjs.react-autosuggest
            ))

(defn input-field [{:keys [dispatch-key label button-text]}]
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
                         nil)}]
       [:> mui/Button {:variant "contained"
                       :color "primary"
                       :on-click #(save)} button-text]])))

(defn-  renderSuggestion [suggestion]
  (reagent/as-element
   [:> mui/MenuItem {:component "div"
                     :className "suggestion-render"} ;; TODO useStyles
    [:span (.-title suggestion)]]))

(defn- renderSuggestionsContainer [props]
  (reagent/as-element [:> mui/Paper (assoc (js->clj (.-containerProps props))
                                     :className "container-open")
                 (.-children props)]))

(defn- renderInput [props & children]
  (reagent/as-element [text-field (into {:className "input full-width"} (js->clj props))]))

(def Autosuggest (reagent/adapt-react-class js/Autosuggest))

(defn auto-suggest-view []
  (let [input-val (reagent/atom "")]
    (fn [{:keys [placeholder suggestions
                 update-suggestions
                 get-suggestion-value
                 on-suggestion-selected
                 clear-suggestions]}]
      [Autosuggest {:renderInputComponent renderInput
                    :renderSuggestionsContainer renderSuggestionsContainer
                    :suggestions suggestions
                    :onSuggestionSelected on-suggestion-selected
                    :onSuggestionsFetchRequested update-suggestions
                    :getSuggestionValue (if get-suggestion-value
                                          get-suggestion-value
                                          identity)
                    :onSuggestionsClearRequested clear-suggestions
                    :renderSuggestion renderSuggestion
                    :inputProps {:placeholder placeholder
                                 :value @input-val
                                 :onChange
                                 #(reset! input-val (.-newValue %2))}}])))
