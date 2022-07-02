(ns application.account.create
  (:require [domain.validator :as validator]
            [domain.schema.account :as schema]))

(defn execute! [account-input]
  (if-let [error (validator/humanized-error schema/create account-input)]
    error
    {:success true
     :data    account-input}))
