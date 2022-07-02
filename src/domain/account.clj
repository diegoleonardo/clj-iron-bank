(ns domain.account
  (:require [domain.validator :as validator]
            [domain.schema.account :as schema]))

(defn create [value]
  (if-let [error (validator/humanized-error schema/creating value)]
    error
    (-> value
        (assoc-in [:account :balance] 0))))
