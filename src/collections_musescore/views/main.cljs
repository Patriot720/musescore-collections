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
     [:> mui/Box {:p 4}
       [:> mui/Grid {:container true :spacing 3}
        (for [collection (vals @collections-atom)
              :let [id (:id collection)]]
          ^{:key id}
          [:> mui/Grid {:className "collection" :item true}
           [collection-view collection]])]]]])

(defn header []
  [:div])

(defn main []
  [:div
   [header]
   [collections-view (subscribe [:collections])]])
