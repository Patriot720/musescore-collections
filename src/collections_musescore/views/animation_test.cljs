(ns collections-musescore.views.animation-test
  (:require
  ;  [cljs.animation.core :refer [css-transition-group]]
   [cljsjs.react-transition-group]
   [reagent.core :as reagent]))

(def css-transition
  (reagent/adapt-react-class js/ReactTransitionGroup.CSSTransition))

(def transition-group
  (reagent/adapt-react-class js/ReactTransitionGroup.TransitionGroup))

(def app-state
  (reagent/atom {:items []
                 :items-counter 0}))

(defn add-item []
  (let [items (:items @app-state)]
    (swap! app-state update-in [:items-counter] inc)
    (swap! app-state assoc :items (conj items (:items-counter @app-state)))))

(defn delete-item []
  (let [items (:items @app-state)]
    (swap! app-state assoc :items (vec (butlast items)))))

(defn home []
  [:div
   [:div (str "Total list items to date:  " (:items-counter @app-state))]
   [:button {:on-click #(add-item)} "add"]
   [:button {:on-click #(delete-item)} "delete"]
   [:ul
    [transition-group {}
     (map-indexed (fn [i x]
                    ^{:key i}  [css-transition {:id x :classNames "foo" :timeout 200}
                                [:li (str "List Item " x)]])
                  (:items @app-state))]]])