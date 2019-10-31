(ns collections-musescore.search.views
  (:require [collections-musescore.views.inputs :as inputs]
   [reagent.core :as reagent]
   ["@material-ui/core" :as mui]
   ["@material-ui/icons" :as mui-icons]
   [re-frame.core :refer [dispatch subscribe]]))

(defn- scroll-to [item-id]
  (js/window.scrollTo #js {:top (.-offsetTop
                                 (js/document.getElementById item-id))
                           :behavior "smooth"}))

(defn- render-search-suggestion [suggestion]
  (reagent/as-element
   [:> mui/MenuItem {:component "div"
                     :className "suggestion-render"} ;; TODO useStyles
    [:span  (.-title suggestion)]]))

(defn render-search-input
  [props & children]
  (reagent/as-element
   [:div {:className "search"}
    [:> mui-icons/Search {:className "searchIcon"}]
    [inputs/input-base (into {:className "search-input" :full-width true :variant "filled"} (js->clj props))]]))

(defn on-suggestion-select [event suggestion]
  (scroll-to (.-id (.-suggestion suggestion))))

(defn search-view []
  [inputs/auto-suggest-view {:placeholder "Search scores"
                             :suggestions  @(subscribe [:search-results])
                             :on-suggestion-selected on-suggestion-select
                             :get-suggestion-value #(str "")
                             :render-suggestion render-search-suggestion
                             :render-input render-search-input
                             :on-suggestions-fetch-requested #(dispatch [:search-local-scores
                                                             (.-value %)])
                             :on-suggestions-clear-requested #()}])
