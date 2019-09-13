(ns collections-musescore.views.views
  (:require
   [reagent.core :as reagent]
   [collections-musescore.views.animation :refer [css-transition transition-group]]
   [collections-musescore.views.inputs :as inputs]
   [re-frame.core :refer [subscribe dispatch]]))

(defn score-view [collection-title {:keys [title url]}]
  [:li
   [:a {:href url :target "_blank"} title]
   [:button {:on-click
             #(dispatch [:remove-score collection-title title])} "DELETE"]])

(defn collection-view [{:keys [title scores]}]
  [:div [:h1 title]
   [inputs/add-score-form title]
   [:ul
    [transition-group {}
     (for [score scores]
       ^{:key (gensym (:title score))}
       [css-transition {:id (:title score) :classNames "score" :timeout 200} [score-view title score]])]]])

(defn collections-view [collections-atom]
  [:div.collections.is-half
   [inputs/add-collection-form]
   [:ul
    [transition-group {}
     (for [collection @collections-atom]
       ^{:key (gensym (:title collection))}
       [css-transition {:id (:title collection) :classNames "score" :timeout 200}
        [collection-view collection]])]]])

(defn main []
  [collections-view (subscribe [:collections])])
