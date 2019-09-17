(ns collections-musescore.views.inputs
  (:require
   [reagent.core :as reagent]
   ["@material-ui/core" :as mui]
   ["@material-ui/core/colors" :as mui-colors]
   ["@material-ui/icons" :as mui-icons]
   [collections-musescore.views.mui-fix :refer [text-field]]
   [re-frame.core :refer [subscribe dispatch]]))



(defn add-collection-form []
  (let [title (reagent/atom "")
        save #(dispatch [:add-collection @title])
        stop #(reset! title "")]
    (fn []
      [:> mui/Grid {:item true}
       [text-field
        {:value @title
         :label "Add collection"
         :rows 10
         :on-change   #(reset! title (-> % .-target .-value))
         :on-key-down #(case (.-which %)
                         13 (save)
                         27 (stop)
                         nil)}]
       [:> mui/Button {:variant "contained"
                       :color "primary"
                       :on-click #(save)} "ADD"]])))


(defn add-score-form [collection-id]
  (let [title (reagent/atom  "")
        url (reagent/atom  "")
        save #(dispatch [:add-score collection-id @title @url])
        stop #(reset! title "")]
    (fn [collection-title]
      [:> mui/Grid {:container true
                    :spacing 3}
       [:> mui/Grid {:item true} [text-field
                                  {:value @title
                                   :label "Add score"
                                   :on-change   #(reset! title (-> % .-target .-value))}]]
       [:> mui/Grid {:item true} [text-field
                                  {:label "url"
                                   :value @url
                                   :on-change   #(reset! url (-> % .-target .-value))
                                   :on-key-down #(case (.-which %)
                                                   13 (save)
                                                   27 (stop)
                                                   nil)}]]
       [:> mui/Grid {:item true} [:> mui/Button {:variant "contained"
                                                 :color "primary"
                                                 :on-click #(save)} "ADD"]]])))