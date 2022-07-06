(ns application.transaction.add-fund
  (:require [domain.validator :as validator]
            [domain.schema.transaction :as schema]
            [domain.repository.transaction-respository :as transaction-repository]
            [domain.transaction :as transaction]))

(defn- process [repository add-fund-input]
  (let [{:keys [balance] :as fund-added} (transaction/deposit add-fund-input)]
    (->> fund-added
         (transaction-repository/add-fund repository)
         (assoc {} :balance balance :id))))

(defn execute! [{:keys [repository]} {:keys [amount] :as input}]
  (if-let [error (validator/humanized-error schema/is-amount-valid? amount)]
    error
    (process repository input)))
