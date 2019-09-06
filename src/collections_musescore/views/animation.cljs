(ns collections-musescore.views.animation
  (:require
   [cljsjs.react-transition-group]
   [reagent.core :as reagent]))

(def css-transition
  (reagent/adapt-react-class js/ReactTransitionGroup.CSSTransition))

(def transition-group
  (reagent/adapt-react-class js/ReactTransitionGroup.TransitionGroup))