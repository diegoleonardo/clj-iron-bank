(ns application.account.fetch
  (:require [domain.repository.account-repository :as account-repository]
            [application.response-handler :as response]))

(defn execute [{:keys [repository]} reference-id]
  (if-let [account (account-repository/fetch repository reference-id)]
    (response/success account)
    (response/error {})))
