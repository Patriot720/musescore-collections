(ns collections-musescore.collection.views
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.score.views :as score-views]
            [collections-musescore.views.animation-util :as animation-util]
            [collections-musescore.views.inputs :as inputs]
            [re-frame.core :refer [dispatch]]
            [reagent.core :as reagent]))

(set! *warn-on-infer* true) ;; TODO fix infer errors
(defn card-content [collection-id title scores]
  (let [open? (reagent/atom true)]
    (fn [collection-id title scores]
      [:> mui/CardContent
       [:> mui/Grid {:container true :spacing 2}
        [:> mui/Grid {:item true :md "auto"} [:> mui/Typography {:variant "h3"} title]]
        [:> mui/Grid {:item true :xs 6 :md "auto"} [:> mui-icons/ArrowForwardIos {:class ["collapse-icon" (when @open? "open")] :on-click #(swap! open? not)}]]
        [:> mui/Grid {:item true :xs 12 :md 10 :lg 10 :justify "flex-end" :container true} [score-views/add-score-modal collection-id]]
        [:> mui/Grid {:item true :container true :justify "center" :xs 12}
         [:> mui/Collapse {:in @open? :timeout animation-util/animation-length}
          [:> mui/Grid {:spacing 2 :justify "space-evenly" :container true}
           (for [score (vals scores)]
             ^{:key (:id score)}
             [:> mui/Grid {:item true} [score-views/score-view collection-id score]])]]]]])))

(defn card-actions [collection-id collection-exists?]
  [:> mui/CardActions
   [:> mui/Button
    {:variant "contained"
     :color "secondary"
     :className "pull-right"
     :on-click
     (partial animation-util/delete-with-animation #(dispatch [:remove-collection collection-id]) collection-exists?)}
    "DELETE collection" [:> mui-icons/Delete {:className "right-button"
                                              :fontSize "small"}]]])

(defn collection-view []
  (let [collection-exists? (reagent/atom true)]
    (fn [{:keys [title scores] collection-id :id}]
      [:> mui/Fade {:in @collection-exists? :timeout animation-util/animation-length}
       [:> mui/Card
        [card-content collection-id title scores]
        [card-actions collection-id collection-exists?]]])))

(defn add-collection-modal [] ;; TODO refactor to base-modal
  (let [open? (reagent/atom false)]
    (fn []
      [:div
       [:> mui/Fab
        {:color "secondary"
         :className "add-collection-fab"
         :on-click #(reset! open? true)} [:> mui-icons/Add]]
       [:> mui/Modal {:on-close #(reset! open? false)
                      :open @open?}
        [:> mui/Paper {:className "add-score-modal"}
         [:> mui/AppBar  [:> mui/Toolbar {:elevation 0} [:> mui/Typography {:varinat "h4"} "Add Collection"]]]
         [:div {:className "autosuggest"}
          [inputs/input-field {:dispatch-key :add-collection :label "Add Collection" :button-text "add"}]]]]])))

;; (defn add-collection-modal []
;;   [:> mui/Fab {:color "secondary" :className "add-collection-fab"} [:> mui-icons/Add]])
