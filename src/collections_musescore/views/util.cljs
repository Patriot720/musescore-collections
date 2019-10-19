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
