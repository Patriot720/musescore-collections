(ns collections-musescore.views.views
  (:require
   [reagent.core :as reagent]
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

(defn collections-view [collections-atom]
  [:div.collections.is-half
   (for [collection @collections-atom
         :let [scores (:scores collection)]]
     ^{:key (gensym (:title collection))}
     [:div [:h1 (:title collection)]
      [:ul
       (for [score scores]
         ^{:key (gensym (:title score))}
         [:li
          [:a {:href (:url score) :target "_blank"} (:title score)]
          [:button {:on-click
                    #(remove-score-from-collections collections-atom (:title collection) (:title score))} "DELETE"]])]])])


(defn main []
  [collections-view dummy-collections])
