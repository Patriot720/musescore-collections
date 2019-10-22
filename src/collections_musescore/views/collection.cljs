(ns collections-musescore.views.collection
  (:require [reagent.core :as reagent]
            ["@material-ui/core" :as mui]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.views.inputs :as inputs]
            [collections-musescore.views.score :as score-views]
            [re-frame.core :refer [dispatch subscribe]]))

(def animation-length 100)


(defn card-content [collection-id title scores]
  [:> mui/CardContent
   [:> mui/Typography {:variant "h3" :className "float-left"} title]

   [score-views/add-score-modal collection-id]

   [:ul.scores
    (for [score (vals scores)]
      ^{:key (:id score)}
      [score-views/score-view collection-id score])]])

(defn- delete-collection-with-animation [collection-id collection-exists?]
  (reset! collection-exists? false)
  (js/setTimeout #(dispatch [:remove-collection collection-id]) animation-length))

(defn card-actions [collection-id collection-exists?]
  [:> mui/CardActions
   [:> mui/Button
    {:variant "contained"
     :color "secondary"
     :className "pull-right"
     :on-click
     (partial delete-collection-with-animation collection-id collection-exists?)}
    "DELETE collection" [:> mui-icons/Delete {:className "right-button"
                                              :fontSize "small"}]]])

(defn collection-view []
  (let [collection-exists? (reagent/atom true)]
    (fn [{:keys [title scores] collection-id :id}]
      [:> mui/Fade {:in @collection-exists? :timeout animation-length}
       [:> mui/Card
        [card-content collection-id title scores]
        [card-actions collection-id collection-exists?]]])))
