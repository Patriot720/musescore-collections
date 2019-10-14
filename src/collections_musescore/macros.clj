(ns collections-musescore.macros)

(defmacro slurp [file]
  (clojure.core/slurp file))