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

  (fetch [_ account-id]
    (get state account-id))

  (patch [_ account-id account]
    (-> (update state
                account-id
                (fn [_ new]
                  new)
                account)
        (get account-id))))
