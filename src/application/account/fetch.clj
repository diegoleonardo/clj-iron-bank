(ns application.account.fetch
  (:require [domain.repository.account-repository :as account-repository]))

(defn execute [{:keys [repository]} reference-id]
  (account-repository/fetch repository reference-id))
