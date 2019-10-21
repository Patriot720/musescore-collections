(ns collections-musescore.macros
(:require [environ.core :as environ ]))

(defmacro env [file-key]
  (environ/env file-key))
