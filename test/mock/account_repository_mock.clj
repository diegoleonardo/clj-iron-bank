(ns mock.account-repository-mock
  (:require [domain.repository.account-repository :as account-repository])
  (:import [java.util UUID]))

(defn- id []
  (-> (UUID/randomUUID)
      str))

(defrecord account-repository-mock [state]
  account-repository/account-repository

  (create [_ account]
    (let [id (id)]
      (assoc-in state [:state id] account)
      id))

  (fetch [_ reference-id]
    (get state reference-id))

  (patch [_ reference-id account]
    (-> (update state
                reference-id
                (fn [_ new]
                  new)
                account)
        (get reference-id))))
