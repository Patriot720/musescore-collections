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

(defn score-view [{:keys [title url]}]
  [:li [:a {:href url :target "_blank"} title]])

(defn collections-view [collections]
  [:div.collections.is-half
   (for [collection collections
         :let [scores (:scores collection)
               title (:title collection)]]
     ^{:key (gensym title)}
     [:div [:h1 (:title collection)]
      [:ul
       (for [score scores]
         ^{:key (gensym (:title score))} [score-view score])]])])


(defn main []
  [collections-view @dummy-collections])
