(ns application.account.create
  (:require [domain.validator :as validator]
            [domain.schema.account :as schema]
            [domain.account :as account]
            [domain.repository.account-repository :as account-repository]))

(defn- process [{:keys [repository]} account-input]
  (let [account (account/create account-input)
        account-id (account-repository/create repository account)]
    {:account-id account-id}))

(defn execute! [deps account-input]
  (if-let [error (validator/humanized-error schema/create account-input)]
    error
    {:success true
     :data    (process deps account-input)}))
