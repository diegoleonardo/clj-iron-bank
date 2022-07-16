(ns application.account.create
  (:require [domain.validator :as validator]
            [domain.schema.account :as schema]
            [domain.account :as account]
            [domain.repository.account-repository :as account-repository]
            [application.response-handler :as response]))

(defn- process [{:keys [repository]} account-input]
  (let [account    (account/create account-input)
        account-id (account-repository/create repository account)]
    {:reference-id account-id}))

(defn execute! [deps account-input]
  (if-let [error (validator/humanized-error schema/create account-input)]
    (response/error error)
    (->> account-input
         (process deps)
         response/success)))
