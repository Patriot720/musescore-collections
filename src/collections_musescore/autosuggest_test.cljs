(ns collections-musescore.autosuggest-test
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r :refer [atom]]
            ["@material-ui/core" :as mui]
            [collections-musescore.views.mui-fix :refer [text-field]]
            cljsjs.react-autosuggest))


(defn getSuggestionValue [suggestion]
  suggestion)

(defn  renderSuggestion [suggestion]
  (r/as-element
   [:> mui/MenuItem {:component "div"}
    [:span suggestion]]))

(defn renderInput [props & children]
  (r/as-element [text-field (js->clj props)]))

(def Autosuggest (r/adapt-react-class js/Autosuggest))

(defn auto-suggest [id]
  (let [as-val (r/atom "")
        update-state-val (fn [evt new-val method]
                           (reset! as-val (.-newValue new-val))
                           nil)]
    (fn [id]
      [Autosuggest {:id id
                    :renderInputComponent renderInput
                    :suggestions @(subscribe [:suggestions])
                    :onSuggestionsFetchRequested
                    #(dispatch [:get-suggestions (.-value %)])
                    :getSuggestionValue getSuggestionValue
                    :onSuggestionsClearRequested
                    #(dispatch [:clear-suggestions])
                    :renderSuggestion renderSuggestion
                    :inputProps {:placeholder "Type 'c'"
                                 :value @as-val
                                 :onChange update-state-val}}])))


;; -------------------------
;; Views

(defn home-page []
  [:div [:h3 "Calling react-autosuggest from clojure"]
   [:div [auto-suggest "my-auto"]]
   [:div [:a {:href "http://react-autosuggest.js.org/"} "go to controls home page"]]])

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))