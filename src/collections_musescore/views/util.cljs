(ns collections-musescore.views.util
  (:require   [reagent.core :as r]
              ["@material-ui/core" :as mui]
              ["@material-ui/core/colors" :as mui-colors]
              ["@material-ui/icons" :as mui-icons]
              [reagent.impl.template :as rtpl]))


(defn tab-panel [value index & children]
  [:> mui/Typography {:component "div"
                      :role "tabpanel"
                      :hidden (not (= value index))}
   (into [:> mui/Box {:p 3}] children)])


(defmulti grid-item (fn [argument] (type argument)))
(defmethod grid-item 'cljs.core/PersistentArrayMap
  [props & children]
  (into [:> mui/Grid (into {:item true} props)] children))

(defmethod grid-item :default [& children]
  (into [:> mui/Grid] children))

(defn grid-container [props & children]
  (into [:> mui/Grid (into {:container true} props)] children))


(def ^:private input-component
  (r/reactify-component
   (fn [props]
     [:input (-> props
                 (assoc :ref (:inputRef props))
                 (dissoc :inputRef))])))

(def ^:private textarea-component
  (r/reactify-component
   (fn [props]
     [:textarea (-> props
                    (assoc :ref (:inputRef props))
                    (dissoc :inputRef))])))

;; To fix cursor jumping when controlled input value is changed,
;; use wrapper input element created by Reagent instead of
;; letting Material-UI to create input element directly using React.
;; Create-element + convert-props-value is the same as what adapt-react-class does.
(defn text-field [props & children]
  (let [props (-> props
                  (assoc-in [:InputProps :inputComponent] (cond
                                                            (and (:multiline props) (:rows props) (not (:maxRows props)))
                                                            textarea-component

                                                            ;; FIXME: Autosize multiline field is broken.
                                                            (:multiline props)
                                                            nil

                                                            ;; Select doesn't require cursor fix so default can be used.
                                                            (:select props)
                                                            nil

                                                            :else
                                                            input-component))
                  rtpl/convert-prop-value)]
    (apply r/create-element mui/TextField props (map r/as-element children))))