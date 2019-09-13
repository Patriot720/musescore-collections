(ns collections-musescore.views.views
  (:require
   [reagent.core :as reagent]
   [collections-musescore.views.animation :refer [css-transition transition-group]]
   [re-frame.core :refer [subscribe dispatch]]))

(def dummy-collection {:title "SightRead"
                       :scores [{:title "Roses of castemire"
                                 :url "https://musescore.com/user/158751/scores/2163051"}
                                {:url "https://musescore.com/user/158751/scores/2163051"
                                 :title "stuff of gods"}
                                {:url "https://musescore.com/user/158751/scores/2163051"
                                 :title "stuff of gods"}
                                {:title "cool beans"
                                 :url "https://musescore.com/user/158751/scores/2163051"}]})

(def dummy-collections (reagent/atom [dummy-collection dummy-collection]))


; (defn score-view [{:keys [title url]}])


(defn add-collection-button []
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


(defn add-score [collection-title]
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

(defn score-view [collection-title {:keys [title url]}]
  [:li
   [:a {:href url :target "_blank"} title]
   [:button {:on-click
             #(dispatch [:remove-score collection-title title])} "DELETE"]])

(defn collection-view [{:keys [title scores]}]
  [:div [:h1 title]
   [add-score title]
   [:ul
    [transition-group {}
     (for [score scores]
       ^{:key (gensym (:title score))}
       [css-transition {:id (:title score) :classNames "score" :timeout 200} [score-view title score]])]]])

(defn collections-view [collections-atom]
  [:div.collections.is-half
   [add-collection-button]
   [:ul
    [transition-group {}
     (for [collection @collections-atom]
       ^{:key (gensym (:title collection))}
       [css-transition {:id (:title collection) :classNames "score" :timeout 200}
        [collection-view collection]])]]])

(defn main []
  [collections-view (subscribe [:collections])])
