(ns collections-musescore.views.inputs
  (:require ["@material-ui/core" :as mui]
            [re-frame.core :refer [dispatch subscribe]]
            [oops.core :refer [oget oset! ocall oapply ocall! oapply!
                               oget+ oset!+ ocall+ oapply+ ocall!+ oapply!+]]
            [reagent.impl.template :as rtpl]
            [reagent.core :as reagent]
            cljsjs.react-autosuggest))

(set! *warn-on-infer* true) ;; TODO fix infer errors
(def ^:private input-component
  (reagent/reactify-component
   (fn [props]
     [:input (-> props
                 (assoc :ref (:inputRef props))
                 (dissoc :inputRef))])))

(def ^:private textarea-component
  (reagent/reactify-component
   (fn [props]
     [:textarea (-> props
                    (assoc :ref (:inputRef props))
                    (dissoc :inputRef))])))

(defn text-field [props & children]
  (let [props (-> props
                  (assoc-in [:InputProps :inputComponent] (cond
                                                            (and (:multiline props) (:rows props) (not (:maxRows props)))
                                                            textarea-component

                                                            ;; FIXME: Autosize multiline field is broken.
                                                            (:multiline props)
                                                            nil

                                                            ;; Select doesn't require cursor fix so default can be used.
                                                            (:select props)
                                                            nil

                                                            :else
                                                            input-component))
                  rtpl/convert-prop-value)]
    (apply reagent/create-element mui/TextField props (map reagent/as-element children))))

(defn input-base [props & children]
  (let [props (-> props
                  rtpl/convert-prop-value)]
    (apply reagent/create-element mui/InputBase props (map reagent/as-element children))))

(defn input-field [{:keys [dispatch-function label button-text]}] ;; TODO only one move somewhere
  (let [title (reagent/atom "")
        stop #(reset! title "")
        save ;; #(dispatch [dispatch-key @title])
        #((partial dispatch-function @title))
        ]
    (fn []
      [:> mui/Grid {:item true :container true :spacing 2}
       [:> mui/Grid {:item true} [text-field
                                  {:value @title :label label :rows 10
                                   :on-change #(reset! title  (oget % "target.value"))
                                   :on-key-down #(case (oget % "which")
                                                   13 (save)
                                                   27 (stop)
                                                   nil)}]]
       [:> mui/Grid {:item true}
        [:> mui/Button {:variant "contained"
                        :color "primary"
                        :on-click #(save)} button-text]]])))

(defn-  renderSuggestion [suggestion]
  (when (oget suggestion "title") (reagent/as-element
                                   [:> mui/MenuItem {:component "div"
                                                     :className "suggestion-render"} ;; TODO useStyles
                                    [:span (oget suggestion "title")]])))

(defn- renderSuggestionsContainer [props]
  (reagent/as-element
   [:> mui/Paper (js->clj (oget props "containerProps"))
    (oget props "children")]))

(defn- renderInput [props & children]
  (reagent/as-element [text-field (into {:className "input full-width"} (js->clj props))]))

(def Autosuggest (reagent/adapt-react-class js/Autosuggest))
;; fs
(defn auto-suggest-view []
  (let [input-val (reagent/atom "")]
    (fn [{:keys [placeholder suggestions
                 on-suggestions-fetch-requested
                 get-suggestion-value
                 render-suggestion
                 render-input
                 on-suggestion-selected
                 on-suggestions-clear-requested]}]
      [Autosuggest {:renderInputComponent (or render-input renderInput)
                    :renderSuggestionsContainer renderSuggestionsContainer
                    :suggestions suggestions
                    :onSuggestionSelected on-suggestion-selected
                    :onSuggestionsFetchRequested on-suggestions-fetch-requested
                    :getSuggestionValue (or get-suggestion-value
                                            identity)
                    :onSuggestionsClearRequested on-suggestions-clear-requested
                    :renderSuggestion (or render-suggestion renderSuggestion)
                    :inputProps {:placeholder placeholder
                                 :value @input-val
                                 :onChange
                                 #(reset! input-val (oget %2 "newValue"))}}])))
