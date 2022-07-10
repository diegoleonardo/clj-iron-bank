(ns application.transaction.remove-fund
  (:require [domain.transaction :as transaction]
            [domain.schema.transaction :as schema]
            [domain.validator :as validator]
            [domain.repository.transaction-respository :as transaction-repository]
            [application.response-handler :as response]))

(defn- process [repository remove-fund-input]
  (let [{:keys [balance] :as fund-removed} (transaction/debit remove-fund-input)]
    (->> fund-removed
         (transaction-repository/save repository)
         (assoc {} :balance balance :id)
         response/success)))

(defn execute! [{:keys [repository]} {:keys [amount reference-id] :as remove-fund-input}]
  (let [current (transaction-repository/current-balance repository reference-id)]
    (if-let [error (or (validator/humanized-error schema/is-amount-valid? amount)
                       (validator/humanized-error schema/is-amount-valid? current))]
      (response/error error)
      (->> current
           (assoc-in remove-fund-input [:account :balance])
           (process repository)))))
