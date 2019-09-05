(ns collections-musescore.views.motion-test
  (:require [collections-musescore.views.motion :refer [TransitionMotion spring]]))

(defn closing-div [{[style clicked?] :children}]
  [:div {:on-click #(reset! clicked? true)
         :style {:background-color "black"
                 :height (.-height style)
                 :opacity (.-opacity style)}} "nice"])

(def closing-div-comp (reagent/reactify-component closing-div))

(defn on-click-destroy-motion []
  (let [clicked? (reagent/atom false)]
    (fn []
      [Motion
       {:style {:height (if @clicked? (spring 0) 60)
                :opacity (if @clicked? (spring 0) 1)}}
       (fn [style]
         (reagent/create-element closing-div-comp #js {} [style clicked?]))])))