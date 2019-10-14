(ns collections-musescore.autosuggest-test
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r :refer [atom]]
            ["@material-ui/core" :as mui]
            [clojure.string :as string]
            [collections-musescore.views.mui-fix :refer [text-field]]
            [collections-musescore.views.score-views :refer [score-view]]
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

(defn- get-suggestion-value [suggestion]
  (.-permalink suggestion))

(def Autosuggest (r/adapt-react-class js/Autosuggest))


(defn auto-suggest-view []
  (let [input-val (r/atom "")]
    (fn [{:keys [placeholder suggestions
                 update-suggestions
                 get-suggestion-value
                 render-suggestion
                 on-suggestion-selected
                 clear-suggestions]}]
      [Autosuggest {:renderInputComponent renderInput
                    :renderSuggestionsContainer renderSuggestionsContainer
                    :suggestions suggestions
                    :onSuggestionSelected on-suggestion-selected
                    :onSuggestionsFetchRequested update-suggestions
                    :shouldRenderSuggestions #((not (string/includes? %1 "http")))
                    :getSuggestionValue (if get-suggestion-value
                                          get-suggestion-value
                                          identity)
                    :onSuggestionsClearRequested clear-suggestions
                    :renderSuggestion render-suggestion
                    :inputProps {:placeholder placeholder
                                 :value @input-val
                                 :onChange
                                 #(reset! input-val (.-newValue %2))}}])))


(defn home-page []
  [:div {:className "autosuggest-test"}
   [:div [auto-suggest-view {:placeholder "Type  stuff"
                             :render-suggestion  renderSuggestion
                             :get-suggestion-value get-suggestion-value
                             :suggestions @(subscribe [:suggestions])
                             :update-suggestions  #(dispatch [:get-suggestions (.-value %)])
                             :on-suggestion-selected #(dispatch   [:get-url-info (.-suggestionValue %2)])
                             :clear-suggestions #(dispatch [:clear-suggestions])}]]
   [:div {:style {:padding :20px}} [score-view 1 @(subscribe [:url-info])]]])

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))
(defn init! []
  (mount-root))