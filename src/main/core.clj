(ns main.core
  (:require [main.system :as stm]))

(defonce service nil)

(defn -main [args]
  (->> args
       stm/config
       constantly
       (alter-var-root #'service)))

(defn -close []
  (.stop service))
