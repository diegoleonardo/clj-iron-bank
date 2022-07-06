(ns mock.transaction-repository-mock
  (:require [domain.repository.transaction-respository :as transaction-repository]
            [infra.repository.id-generator :as gen-id]))

(defrecord transaction-repository-mock [state]
  transaction-repository/transaction-repository

  (save [_ {:keys [reference-id] :as transaction}]
    (let [id (gen-id/id)
          transaction-to-persist (assoc transaction :id id)
          transactions (get state reference-id)]
      (conj transactions transaction-to-persist)
      id))

  (current-balance [_ reference-id]
    (->> reference-id
         (get state)
         last
         :balance)))
