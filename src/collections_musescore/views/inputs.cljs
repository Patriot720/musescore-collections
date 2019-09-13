(ns collections-musescore.views.inputs
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe dispatch]]))

(defn add-collection-form []
  (let [title (reagent/atom "")
        save #(dispatch [:add-collection @title])
        stop #(reset! title "")]
    (fn []
      [:div
       [:input
        {:on-change   #(reset! title (-> % .-target .-value))
         :on-key-down #(case (.-which %)
                         13 (save)
                         27 (stop)
                         nil)}]
       [:button {:on-click #(save)} "ADD"]])))


(defn add-score-form [collection-title]
  (let [title (reagent/atom  "")
        url (reagent/atom  "")
        save #(dispatch [:add-score collection-title @title @url])
        stop #(reset! title "")]
    (fn [collection-title]
      [:div
       [:label "Score name"]
       [:input
        {:on-change   #(reset! title (-> % .-target .-value))}]
       [:label "URL"]
       [:input
        {:on-change   #(reset! url (-> % .-target .-value))
         :on-key-down #(case (.-which %)
                         13 (save)
                         27 (stop)
                         nil)}]
       [:button {:on-click #(save)} "ADD"]])))