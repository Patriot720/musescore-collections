(ns collections-musescore.autosuggest-test
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r :refer [atom]]
            ["@material-ui/core" :as mui]
            [collections-musescore.views.mui-fix :refer [text-field]]
            [collections-musescore.views.score-views :refer [score-view]]
            cljsjs.react-autosuggest))


(defn-  renderSuggestion [suggestion]
  (r/as-element
   [:> mui/MenuItem {:component "div"
                     :className "suggestion-render"} ;; TODO useStyles
    [:span suggestion]]))

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
                 render-suggestion
                 clear-suggestions]}]
      [Autosuggest {:renderInputComponent renderInput
                    :renderSuggestionsContainer renderSuggestionsContainer
                    :suggestions suggestions
                    :onSuggestionsFetchRequested update-suggestions
                    :getSuggestionValue (if get-suggestion-value
                                          get-suggestion-value
                                          identity)
                    :onSuggestionsClearRequested clear-suggestions
                    :renderSuggestion render-suggestion
                    :inputProps {:placeholder placeholder
                                 :value @input-val
                                 :onChange
                                 #(reset! input-val (.-newValue %2))}}])))

;; -------------------------
;; Views

(defn home-page []
  [:div {:className "autosuggest-test"}
   [:div [auto-suggest-view {:placeholder "Type  stuff"
                             :render-suggestion  renderSuggestion
                             :suggestions @(subscribe [:suggestions])
                             :update-suggestions  #(dispatch [:get-suggestions (.-value %)])
                             :clear-suggestions #(dispatch [:clear-suggestions])}]]
   [:> mui/Paper [score-view 1 {:title "Nice score"
                                :id 2
                                :url "some-url"}]]])

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))