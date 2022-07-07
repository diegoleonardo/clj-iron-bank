(ns application.account.update
  (:require [domain.validator :as validator]
            [domain.schema.account :as schema]
            [domain.repository.account-repository :as account-repository]))

(defn- process [{:keys [repository]} {:keys [reference-id] :as account-input}]
  (if-let [_ (account-repository/fetch repository reference-id)]
    (account-repository/patch repository reference-id account-input)
    {:error ["The account doesn't exist"]}))

(defn execute! [deps account-input]
  (if-let [error (validator/humanized-error schema/update account-input)]
    error
    (process deps account-input)))
