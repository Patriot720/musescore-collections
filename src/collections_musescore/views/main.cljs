(ns collections-musescore.views.main
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.views.inputs :as inputs]
            [collections-musescore.views.collection :refer [collection-view]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(set! *warn-on-infer* true)

(defn collections-view [collections-atom]
  [:section.section
   [:> mui/Container
    [inputs/input-field {:dispatch-key :add-collection
                         :label "Add collection"
                         :button-text "Add"}]
     [:> mui/Grid {:container true :spacing 3}
      (for [collection (vals @collections-atom)
            :let [id (:id collection)]]
        ^{:key id}
        [:> mui/Grid {:className "collection" :item true}
         [collection-view collection]])]]])

(defn- render-search-suggestion [suggestion]
  (reagent/as-element
   [:> mui/MenuItem {:component "div"
                     :className "suggestion-render"} ;; TODO useStyles
    [:span  suggestion]]))

(defn render-search-input
  [props & children]
  (reagent/as-element
   [:div {:className "search"}
    [:> mui-icons/Search {:className "searchIcon"}]
    [inputs/input-base (into {:className "search-input" :full-width true :variant "filled"} (js->clj props))]]))

(defn header []
  [:div {:className "header"}
   [:> mui/AppBar 
    [:> mui/Toolbar
     [:div {:className "search-suggest"}
     [inputs/auto-suggest-view {:placeholder "WAT"
                                :suggestions  ["cool" "cool" "cool" "cool"]
                                :render-suggestion render-search-suggestion
                                :render-input render-search-input
                                :update-suggestions #()
                                :clear-suggestions #()}]]]]
   [:> mui/Container
   [:> mui/Typography {:variant "h2" :gutterBottom true :component "h1"}
    "Musescore collections"]]])

(defn main []
  [:div
   [header]
   [collections-view (subscribe [:collections])]])
