(ns application.account.fetch
  (:require [domain.repository.account-repository :as account-repository]))

(defn execute [{:keys [repository]} account-id]
  (account-repository/fetch repository account-id))
