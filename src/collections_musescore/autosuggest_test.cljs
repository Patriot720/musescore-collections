(ns collections-musescore.autosuggest-test
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r :refer [atom]]
            cljsjs.react-autosuggest))


(defn getSuggestionValue [suggestion]
  (.-name suggestion))

(defn  renderSuggestion [suggestion]
  (r/as-element
   [:span (.-name suggestion)]))

(def Autosuggest (r/adapt-react-class js/Autosuggest))

(defn auto-suggest [id]
  (let [suggestions (subscribe [:suggestions])
        as-val (r/atom "")
        update-suggestions #(dispatch [:get-suggestions (.-value %)])
        clear-suggestions #(dispatch [:clear-suggestions])
        update-state-val (fn [evt new-val method]
                           (reset! as-val (.-newValue new-val))
                           nil)]
    (fn [id]
      [Autosuggest {:id id
                    :suggestions @suggestions
                    :onSuggestionsFetchRequested update-suggestions
                    :getSuggestionValue getSuggestionValue
                    :onSuggestionsClearRequested clear-suggestions
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