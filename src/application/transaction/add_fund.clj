(ns application.transaction.add-fund
  (:require [domain.validator :as validator]
            [domain.schema.transaction :as schema]
            [domain.repository.transaction-respository :as transaction-repository]
            [domain.transaction :as transaction]))

(defn- process [repository add-fund-input]
  (->> add-fund-input
       transaction/deposit
       (transaction-repository/add-fund repository)
       (assoc {} :id)))

(defn execute [{:keys [repository]} {:keys [amount] :as input}]
  (if-let [error (validator/humanized-error schema/is-amount-valid? amount)]
    error
    (process repository input)))
