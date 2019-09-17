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

(defn styles [^js/Mui.Theme theme]
  #js {:right-icon #js {:marginLeft (.spacing theme 1)}})

(defn score-view [collection-id {:keys [id title url]}]
  [:li.score
   [:a {:href url :target "_blank"}
    [:h1 title]
    [:button.button.is-danger {:on-click
                               #(dispatch [:remove-score collection-id id])} "DELETE"]]])

(defn collection-view [{:keys [id title scores]}]
  (let [state (reagent/atom true)]
    (fn [{:keys [id title scores]}]
      [:> mui/Fade {:in @state :timeout 100}
       [:> mui/Card
        [:> mui/CardContent
         [:> mui/Typography {:variant "h3"} title]

         [inputs/add-score-form id]
         [:ul.scores
          [transition-group {:component "ul" :className "scores-div"}
           (for [score (vals scores)]
             ^{:key (:id score)}
             [css-transition {:id (:id score) :classNames "score" :timeout 300} [score-view id score]])]]
         [:> mui/CardActions
          [:> mui/Button  ; TODO to button class
           {:variant "contained"
            :color "secondary"
            :on-click (fn []
                        (reset! state false)
                        (js/setTimeout #(dispatch [:remove-collection id]) 100))}
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
