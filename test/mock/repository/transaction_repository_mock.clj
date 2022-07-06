(ns mock.repository.transaction-repository-mock
  (:require [domain.repository.transaction-respository :as transaction-repository]
            [infra.repository.id-generator :as gen-id]))

(defrecord transaction-repository-mock [state]
  transaction-repository/transaction-repository

  (add-fund [_ add-fund-input]
    (let [id (gen-id/id)]
      (assoc-in state [:state id] add-fund-input)
      id)))
