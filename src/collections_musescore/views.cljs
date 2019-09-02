(ns collections-musescore.views)
(def dummy-collection {:title "SightRead"
                       :scores [{:title "Roses of castemire"
                                 :url "https://musescore.com/user/158751/scores/2163051"}
                                {:url "https://musescore.com/user/158751/scores/2163051"
                                 :title "stuff of gods"}
                                {:url "https://musescore.com/user/158751/scores/2163051"
                                 :title "stuff of gods"}
                                {:title "cool beans"
                                 :url "https://musescore.com/user/158751/scores/2163051"}]})
(def dummy-collections [dummy-collection dummy-collection])

(defn score-view [score]
  [:li [:a {:href (:url score) :target "_blank"} (:title score)]])

(defn scores-view [scores]
  [:ul
   (for [score scores]
     ^{:key (gensym (:title score))} [score-view score])])

(defn collection-view [collection]
  [:div [:h1 (:title collection)]
   [scores-view (:scores collection)]])

(defn collections-view [collections]
  [:div.collections.is-half
   (for [collection collections]
     ^{:key (gensym (:title collection))} [collection-view collection])])


(defn main []
  [:div.container [collections-view dummy-collections]])
