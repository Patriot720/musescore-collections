(ns collections-musescore.views.views
  (:require
   [reagent.core :as reagent]
   [collections-musescore.views.animation :refer [css-transition transition-group]]
   [collections-musescore.views.inputs :as inputs]
   [re-frame.core :refer [subscribe dispatch]]))

(defn score-view [collection-id {:keys [id title url]}]
  [:li.score
   [:a {:href url :target "_blank"} title]
   [:button {:on-click
             #(dispatch [:remove-score collection-id id])} "DELETE"]])

(defn collection-view [{:keys [id title scores]}]
  [:div [:h1 title]
   [inputs/add-score-form id]
   [:ul.scores
    [transition-group {}
     (for [score (vals scores)]
       ^{:key (:id score)}
       [css-transition {:id (:id score) :classNames "score" :timeout 300} [score-view id score]])]]])

(defn collections-view [collections-atom]
  [:div.collections.is-half.container
   [inputs/add-collection-form]
   [:ul
    [transition-group {}
     (for [collection (vals @collections-atom)
           :let [id (:id collection)]]
       ^{:key (str "collection_" id)}
       [css-transition {:id id :classNames "score" :timeout 300}
        [collection-view collection]])]]])

(defn main []
  [collections-view (subscribe [:collections])])
