(ns collections-musescore.views.inputs
  (:require ["@material-ui/core" :as mui]
            [collections-musescore.views.util :refer [tab-panel text-field]]
            [re-frame.core :refer [dispatch subscribe]]

            [reagent.core :as reagent]))

(defn input-field [{:keys [dispatch-key label button-text]}]
  (let [title (reagent/atom "")
        stop #(reset! title "")
        save #(dispatch [dispatch-key @title])]
    (fn []
      [:> mui/Grid {:item true}
       [text-field
        {:value @title :label label :rows 10
         :on-change #(reset! title (-> % .-target .-value))
         :on-key-down #(case (.-which %)
                         13 (save)
                         27 (stop)
                         nil)}]
       [:> mui/Button {:variant "contained"
                       :color "primary"
                       :on-click #(save)} button-text]])))

