(ns collections-musescore.views.motion-test
  (:require
   [reagent.core :as reagent]
   [collections-musescore.views.motion :refer [TransitionMotion spring]]))

(def dummy-stuff (reagent/atom [{:id 2
                                 :title "nice"}
                                {:id 3
                                 :title "cool"}]))

(defn remove-item [key stuff & args]
  (swap! stuff (fn [stuff]
                 (remove #(= (:id %) (int key)) stuff))))

(defn list-div [key style]
  [:div {:on-click (partial remove-item key dummy-stuff)
         :key key
         :style {:background-color "black"
                 :height (.-height style)
                 :opacity (.-opacity style)}} "nice"])

(defn closing-divs [{[items] :children}]
  [:div
   (for [item items
         :let [key (.-key item)
               style (.-style item)]]
     ^{:key key} [list-div key style])])

(def closing-divs-comp (reagent/reactify-component closing-divs))

(defn will-leave []
  #js {:height (spring 0) :opacity (spring 0)})

(defn ->motionConfig [stuff]
  (map
   (fn [{:keys [id]}]
     {:key (str id) :style {:opacity 1
                            :height 30}}) stuff))

(defn on-click-destroy-div []
  [TransitionMotion
   {:willLeave will-leave
    :styles (->motionConfig @dummy-stuff)}
   (fn [styles]
     (reagent/create-element closing-divs-comp #js {} [styles]))])