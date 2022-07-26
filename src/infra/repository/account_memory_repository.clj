(ns infra.repository.account-memory-repository
  (:require [domain.repository.account-repository :as account-repository]
            [infra.repository.id-generator :as gen-id]))

(defrecord account-memory-repository [state]
  account-repository/account-repository

  (create [_ account]
    (let [id (gen-id/id)]
      (swap! state assoc id account)
      id))

  (fetch [_ reference-id]
    (get @state reference-id {}))

  (patch [_ reference-id account]
    (let [account-to-update (get @state reference-id)
          nd                (merge account-to-update account)]
      (swap! state assoc reference-id nd)
      nd)))
