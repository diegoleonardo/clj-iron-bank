(ns application.transaction.add-fund
  (:require [domain.validator :as validator]
            [domain.schema.transaction :as schema]
            [domain.repository.transaction-respository :as transaction-repository]
            [domain.transaction :as transaction]))

(defn- process [repository add-fund-input]
  (let [{:keys [balance] :as funds-added} (transaction/deposit add-fund-input)]
    (->> funds-added
         (transaction-repository/save repository)
         (assoc {} :balance balance :id))))

(defn execute! [{:keys [repository]} {:keys [reference-id amount] :as add-fund-input}]
  (let [current (transaction-repository/current-balance repository reference-id)]
    (if-let [error (or (validator/humanized-error schema/is-amount-valid? amount)
                       (validator/humanized-error schema/is-amount-valid? current))]
      error
      (->> current
           (assoc-in add-fund-input [:account :balance])
           (process repository)))))
