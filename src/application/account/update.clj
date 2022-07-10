(ns application.account.update
  (:require [domain.validator :as validator]
            [domain.schema.account :as schema]
            [domain.repository.account-repository :as account-repository]
            [application.response-handler :as response]))

(defn- process [{:keys [repository]} {:keys [reference-id] :as account-input}]
  (if-let [_ (account-repository/fetch repository reference-id)]
    (->> account-input
         (account-repository/patch repository reference-id)
         response/success)
    (response/error {:error ["The account doesn't exist"]})))

(defn execute! [deps account-input]
  (if-let [error (validator/humanized-error schema/update account-input)]
    (response/error error)
    (process deps account-input)))
