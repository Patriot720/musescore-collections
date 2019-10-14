(ns collections-musescore.views.views
  (:require
   [reagent.core :as reagent]
   ["@material-ui/core" :as mui]
   ["@material-ui/icons" :as mui-icons]
   [collections-musescore.views.inputs :as inputs]
   [collections-musescore.views.score-views :refer [score-view]]
   [re-frame.core :refer [subscribe dispatch]]))

(set! *warn-on-infer* true)

(defn event-value
  [^js/Event e]
  (let [^js/HTMLInputElement el (.-target e)]
    (.-value el)))


(defn remove-collection-with-animation [id collection-exists? animation-length]
  (reset! collection-exists? false)
  (js/setTimeout #(dispatch [:remove-collection-with-animation id]) animation-length))

(defn collection-view []
  (let [collection-exists? (reagent/atom true)
        animation-length 100]

    (fn [{:keys [id title scores]}]
      [:> mui/Fade {:in @collection-exists? :timeout animation-length}
       [:> mui/Card
        [:> mui/CardContent
         [:> mui/Typography {:variant "h3"} title]
         [inputs/score-modal id]
         [:ul.scores
          (for [score (vals scores)]
            ^{:key (:id score)}
            [score-view id score])]
         [:> mui/CardActions
          [:> mui/Button
           {:variant "contained"
            :color "secondary"
            :on-click (partial remove-collection-with-animation id collection-exists? animation-length)}
           "DELETE collection" [:> mui-icons/Delete {:className "right-button"
                                                     :fontSize "small"}]]]]]])))

(defn collections-view [collections-atom]
  [:section.section
   [:> mui/Container
    [inputs/input-form {:dispatch-key :add-collection
                        :label "Add collection"
                        :button-text "Add"}]
    [:> mui/Paper {:className "paper-transition"}
     [:> mui/Box {:p 4}
      [:<>
       [:> mui/Grid {:container true :spacing 3}
        (for [collection (vals @collections-atom)
              :let [id (:id collection)]]
          ^{:key (str "collection_" id)}
          [:> mui/Grid {:item true}
           [collection-view collection]])]]]]]])

(defn main []
  [collections-view (subscribe [:collections])])
