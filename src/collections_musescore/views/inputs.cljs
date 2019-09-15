(ns collections-musescore.views.inputs
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe dispatch]]))

(defn input-wrap [label & stuff]
  (into [:div.box [:div.field.is-grouped
                   [:label.label label]]] stuff))

(defn add-collection-form []
  (let [title (reagent/atom "")
        save #(dispatch [:add-collection @title])
        stop #(reset! title "")]
    (fn []
      [input-wrap "Add collection"
       [:input.input
        {:on-change   #(reset! title (-> % .-target .-value))
         :on-key-down #(case (.-which %)
                         13 (save)
                         27 (stop)
                         nil)}]
       [:button.button.is-primary {:on-click #(save)} "ADD"]])))


(defn add-score-form [collection-id]
  (let [title (reagent/atom  "")
        url (reagent/atom  "")
        save #(dispatch [:add-score collection-id @title @url])
        stop #(reset! title "")]
    (fn [collection-title]
      [input-wrap "Add score"
       [:input.input
        {:on-change   #(reset! title (-> % .-target .-value))}]
       [:label "URL"]
       [:input.input
        {:on-change   #(reset! url (-> % .-target .-value))
         :on-key-down #(case (.-which %)
                         13 (save)
                         27 (stop)
                         nil)}]
       [:button.button.is-primary {:on-click #(save)} "ADD"]])))