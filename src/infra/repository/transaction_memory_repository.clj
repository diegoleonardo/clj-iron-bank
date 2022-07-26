(ns infra.repository.transaction-memory-repository
  (:require [domain.repository.transaction-respository :as transaction-repository]
            [infra.repository.id-generator :as gen-id]))

(defrecord transaction-memory-repository [state]
  transaction-repository/transaction-repository

  (save [_ {:keys [reference-id] :as transaction}]
    (let [id                     (gen-id/id)
          transaction-to-persist (-> (dissoc transaction :reference-id)
                                     (assoc :id id))
          transactions           (get @state reference-id [])]
      (->> (conj transactions transaction-to-persist)
           (swap! state assoc reference-id))
      id))

  (transfer [this source destiny]
    {:source-id  (transaction-repository/save this source)
     :destiny-id (transaction-repository/save this destiny)})

  (current-balance [_ reference-id]
    (-> (get @state reference-id)
        last
        (get :balance 0.0))))
