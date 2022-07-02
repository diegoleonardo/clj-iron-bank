(ns domain.transaction.deposit
  (:require [domain.balance :as balance]
            [domain.validator :as validator]
            [domain.schema.transaction :as schema]))

(defn deposit [{:keys [account amount]}]
  (if-let [error (validator/humanized-error schema/is-amount-valid? amount)]
    error
    (let [account-with-new-balance (balance/new-balance account clojure.core/+ amount)]
      {:deposit {:account account-with-new-balance}})))
