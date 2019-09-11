(ns collections-musescore.views.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe dispatch]]
   [collections-musescore.views.animation :as animation]))

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

(defn remove-score [scores score-title]
  (remove (fn [item]
            (= (:title item) score-title)) scores))

(defn remove-score-from-collections [collections collection-title score-title]
  (swap! collections (fn [collections]
                       (map (fn [collection]
                              (if (= (:title collection) collection-title)
                                (update collection :scores remove-score score-title) collection)) collections))))

(defn score-view [{:keys [title url]}])


(defn add-collection []
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

(defn collections-view [collections-atom]
  [:div.collections.is-half
   [add-collection]
   (for [collection @collections-atom
         :let [scores (:scores collection)]]
     ^{:key (gensym (:title collection))}
     [:div [:h1 (:title collection)]
      [add-score (:title collection)]
      [:ul
       (for [score scores]
         ^{:key (gensym (:title score))}
         [:li
          [:a {:href (:url score) :target "_blank"} (:title score)]
          [:button {:on-click
                    #(remove-score-from-collections collections-atom (:title collection) (:title score))} "DELETE"]])]])])
(defn main []
  [collections-view (subscribe [:collections])])
