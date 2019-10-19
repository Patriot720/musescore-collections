(ns collections-musescore.autosuggest
  (:require
   [reagent.core :as r :refer [atom]]
   ["@material-ui/core" :as mui]
   [collections-musescore.views.util :refer [text-field]]
   cljsjs.react-autosuggest))

(defn-  renderSuggestion [suggestion]
  (r/as-element
   [:> mui/MenuItem {:component "div"
                     :className "suggestion-render"} ;; TODO useStyles
    [:span (.-title suggestion)]]))

(defn- renderSuggestionsContainer [props]
  (r/as-element [:> mui/Paper (assoc (js->clj (.-containerProps props))
                                     :className "container-open")
                 (.-children props)]))

(defn- renderInput [props & children]
  (r/as-element [text-field (into {:className "input full-width"} (js->clj props))]))

(def Autosuggest (r/adapt-react-class js/Autosuggest))

(defn auto-suggest-view []
  (let [input-val (r/atom "")]
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
