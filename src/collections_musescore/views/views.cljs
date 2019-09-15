(ns collections-musescore.views.views
  (:require
   [reagent.core :as reagent]
   ["@material-ui/core" :as mui]
   ["@material-ui/core/styles" :refer [createMuiTheme withStyles]]
   ["@material-ui/core/colors" :as mui-colors]
   ["@material-ui/icons" :as mui-icons]
   [collections-musescore.views.animation :refer [css-transition transition-group]]
   [collections-musescore.views.inputs :as inputs]
   [re-frame.core :refer [subscribe dispatch]]))

(set! *warn-on-infer* true)

(defn event-value
  [^js/Event e]
  (let [^js/HTMLInputElement el (.-target e)]
    (.-value el)))


(defn score-view [collection-id {:keys [id title url]}]
  [:li.score
   [:a {:href url :target "_blank"}
    [:h1 title]
    [:button.button.is-danger {:on-click
                               #(dispatch [:remove-score collection-id id])} "DELETE"]]])

(defn collection-view [{:keys [id title scores]}]
  [:div [:h1 title]
   [:button.button.is-danger.is-grouped ; TODO to button class
    {:on-click #(dispatch [:remove-collection id])} "DELETE collection"]
   [inputs/add-score-form id]
   [:ul.scores
    [transition-group {:component "ul" :className "scores-div"}
     (for [score (vals scores)]
       ^{:key (:id score)}
       [css-transition {:id (:id score) :classNames "score" :timeout 300} [score-view id score]])]]])

(defn collections-view [collections-atom]

  [:section.section
   [:> mui/Container
    [inputs/add-collection-form]
    [:> mui/Paper
     [transition-group {:component "ul"}
      (for [collection (vals @collections-atom)
            :let [id (:id collection)]]
        ^{:key (str "collection_" id)}
        [css-transition {:id id :classNames "score" :timeout 300}
         [collection-view collection]])]]]])

(defn main []
  [collections-view (subscribe [:collections])])
