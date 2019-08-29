(ns collections-musescore.views)
(def dummy-collection {
                       :title "SightRead" 
                       :scores[{:title "Roses of castemire"
                                :url "https://musescore.com/user/158751/scores/2163051"}
                               {:url "https://musescore.com/user/158751/scores/2163051"
                                :title "stuff of gods"}
                               {:url "https://musescore.com/user/158751/scores/2163051"
                                :title "stuff of gods"}
                               {:title "cool beans"
                                :url "https://musescore.com/user/158751/scores/2163051"}]})
(def dummy-collections [dummy-collection dummy-collection])

(defn main []
  [:div.collections
   (for [collection dummy-collections]
     ^{:key (gensym (:title collection))}[:div (:title collection)
      (for [score (:scores collection)]
        ^{:key (gensym (:title score))}[:a {:href (:url score) :target "_blank"} (:title score)])])])
