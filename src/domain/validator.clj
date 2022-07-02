(ns domain.validator
  (:require [malli.core :as m]
            [malli.error :as me]))

(defn valid? [schema data]
  (m/validate schema data))

(defn humanized-error [schema data]
  (when (false? (valid? schema data))
    (->> (m/explain schema data)
         (me/humanize)
         (assoc {} :error))))
