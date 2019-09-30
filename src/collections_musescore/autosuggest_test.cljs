(ns collections-musescore.autosuggest-test
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r :refer [atom]]
            ["@material-ui/core" :as mui]
            ["@material-ui/core/styles" :refer [createMuiTheme withStyles]]
            [collections-musescore.views.mui-fix :refer [text-field]]
            cljsjs.react-autosuggest))

(defn custom-styles [^js/Mui.Theme theme]
  #js {:root #js {:height 250
                  :flex-grow 1}
       :container #js {:position 'relative'}
       :suggestionsContainerOpen #js {:position 'absolute'
                                      :zIndex 1
                                      :marginTop (.spacing theme 1)
                                      :left 0
                                      :right 0}
       :suggestion #js {:display 'block'}
       :suggestionsList #js {:margin 0
                             :padding 0
                             :listStyleType 'none'}
       :divider #js {:height (.spacing theme 2)}})

(defn getSuggestionValue [suggestion]
  suggestion)

(defn  renderSuggestion [suggestion]
  (r/as-element
   [:> mui/MenuItem {:component "div"
                     :className "suggestion-render"} ;; TODO useStyles
    [:span suggestion]]))

(defn renderSuggestionsContainer [props]
  (r/as-element [:> mui/Paper (assoc (js->clj (.-containerProps props))
                                     :className "container-open")
                 (.-children props)]))
(defn renderInput [props & children]
  (r/as-element [text-field (into {:className "input full-width"} (js->clj props))]))

(def Autosuggest (r/adapt-react-class js/Autosuggest))

(defn auto-suggest []
  (let [input-val (r/atom "")]
    (fn []
      [Autosuggest {:renderInputComponent renderInput
                    :renderSuggestionsContainer renderSuggestionsContainer
                    :suggestions @(subscribe [:suggestions])
                    :onSuggestionsFetchRequested
                    #(dispatch [:get-suggestions (.-value %)])
                    :getSuggestionValue getSuggestionValue
                    :onSuggestionsClearRequested
                    #(dispatch [:clear-suggestions])
                    :renderSuggestion renderSuggestion
                    :inputProps {:placeholder "Type 'c'"
                                 :value @input-val
                                 :onChange
                                 #(reset! input-val (.-newValue %2))}}])))


;; -------------------------
;; Views

(defn home-page []
  [:div {:className "autosuggest-test"}
   [:div [auto-suggest "my-auto"]]])

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))