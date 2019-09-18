(ns collections-musescore.views.views
  (:require
   [reagent.core :as reagent]
   ["@material-ui/core" :as mui]
   ["@material-ui/core/styles" :refer [createMuiTheme withStyles]]
   ["@material-ui/core/colors" :as mui-colors]
   ["@material-ui/icons" :as mui-icons]
   [collections-musescore.views.inputs :as inputs]
   [re-frame.core :refer [subscribe dispatch]]))

(set! *warn-on-infer* true)

(defn event-value
  [^js/Event e]
  (let [^js/HTMLInputElement el (.-target e)]
    (.-value el)))


(defn score-view [collection-id {:keys [id title url]}]
  [:a {:href url :target "_blank"}
   [:> mui/Grid {:container true :alignItems "center" :justify "space-evenly"}
    [:> mui/Grid {:item true}
     [:h1 title]]
    [:> mui/Grid {:item true}
     [:> mui/Button {:on-click
                     #(dispatch [:remove-score collection-id id])} "DELETE"]]]])

(defn remove-collection [id collection-exists? animation-length]
  (reset! collection-exists? false)
  (js/setTimeout #(dispatch [:remove-collection id]) animation-length))

(defn collection-view []
  (let [collection-exists? (reagent/atom true)
        animation-length 100]

    (fn [{:keys [id title scores]}]
      [:> mui/Fade {:in @collection-exists? :timeout animation-length}
       [:> mui/Card
        [:> mui/CardContent
         [:> mui/Typography {:variant "h3"} title]
        ;  [inputs/add-score-form id]
         [inputs/add-score-modal id]
         [:ul.scores
          (for [score (vals scores)]
            ^{:key (:id score)}
            [score-view id score])]
         [:> mui/CardActions
          [:> mui/Button
           {:variant "contained"
            :color "secondary"
            :on-click (partial remove-collection id collection-exists? animation-length)}
           "DELETE collection" [:> mui-icons/Delete {:className "right-button"
                                                     :fontSize "small"}]]]]]])))

(defn collections-view [collections-atom]

  [:section.section
   [:> mui/Container
    [inputs/add-collection-form]
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
