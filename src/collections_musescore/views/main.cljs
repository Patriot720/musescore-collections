(ns collections-musescore.views.main
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.views.inputs :as inputs]
            [collections-musescore.collection.views :refer [collection-view add-collection-modal]]
            [collections-musescore.search.views :as search]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(set! *warn-on-infer* false) ;; TODO fix infer errors


(defn collections-view [collections-atom]
  [:section.section
   [:> mui/Container
    [:> mui/Grid {:container true :spacing 3}
     (for [collection (vals @collections-atom)
           :let [id (:id collection)]]
       ^{:key id}
       [:> mui/Grid {:className "collection" :item true}
        [collection-view collection]])]]])
(defn header []

  [:div {:className "header"}
   [:> mui/AppBar
    [:> mui/Toolbar
     [:div {:className "search-suggest"}
      [search/search-view]]]]
   [:> mui/Container
    [add-collection-modal]
    [:> mui/Typography {:variant "h2" :gutterBottom true :component "h1"}
     "MuseScore collections"]]])

(defn main []
  [:div
   [header]
   [collections-view (subscribe [:collections])]])
