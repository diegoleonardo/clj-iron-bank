(ns domain.transaction.debit
  (:require [domain.balance :as balance]
            [domain.validator :as validator]
            [domain.schema.transaction :as schema]))

(defn debit [{:keys [account amount]}]
  (if-let [error (validator/humanized-error schema/is-amount-valid? amount)]
    error
    (let [account-with-new-balance (balance/new-balance account clojure.core/- amount)]
      {:debit {:account account-with-new-balance}})))
