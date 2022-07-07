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

  (transfer [_ source destiny]
    (let [source-id (gen-id/id)
          destiny-id (gen-id/id)
          source-transaction (assoc source :id source-id)
          destiny-transaction (assoc destiny :id destiny-id)]
      (-> state
          (assoc (:reference-id source) (conj (get state (:reference-id source)) source-transaction))
          (assoc (:reference-id destiny) (conj (get state (:reference-id destiny)) destiny-transaction)))
      {:source-id source-id
       :destiny-id destiny-id}))

  (current-balance [_ reference-id]
    (->> reference-id
         (get state)
         last
         :balance)))
