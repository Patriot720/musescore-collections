(ns collections-musescore.search.views
  (:require [collections-musescore.views.inputs :as inputs]
            [reagent.core :as reagent]
            [oops.core :refer [oget oset! ocall oapply ocall! oapply!
                               oget+ oset!+ ocall+ oapply+ ocall!+ oapply!+]]
            ["@material-ui/core" :as mui]
            ["@material-ui/icons" :as mui-icons]
            [re-frame.core :refer [dispatch subscribe]]))

(set! *warn-on-infer* true) ;; TODO fix infer errors
(defn- scroll-to [item-id]
  (ocall js/window "scrollTo" #js {:top (oget (ocall js/document "getElementById" item-id) "offsetTop")
                           :behavior "smooth"}))

(defn- render-search-suggestion [suggestion]
  (reagent/as-element
   [:> mui/MenuItem {:component "div"
                     :className "suggestion-render"} ;; TODO useStyles
    [:span  (oget suggestion "title")]]))

(defn render-search-input
  [props & children]
  (reagent/as-element
   [:div {:className "search"}
    [:> mui-icons/Search {:className "searchIcon"}]
    [inputs/input-base (into {:className "search-input" :full-width true :variant "filled"} (js->clj props))]]))

(defn on-suggestion-select [event suggestion]
  (scroll-to (oget suggestion "suggestion.id")))

(defn search-view []
  [inputs/auto-suggest-view {:placeholder "Search scores"
                             :suggestions  @(subscribe [:search-results])
                             :on-suggestion-selected on-suggestion-select
                             :get-suggestion-value #(str "")
                             :render-suggestion render-search-suggestion
                             :render-input render-search-input
                             :on-suggestions-fetch-requested #(dispatch [:search-local-scores
                                                                         (oget  % "value")])
                             :on-suggestions-clear-requested #()}])
