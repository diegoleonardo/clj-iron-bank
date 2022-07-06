(ns mock.mock-utils
  (:require [mock.repository.account-repository-mock :as repo-mock]
            [mock.repository.transaction-repository-mock :as transaction-repo-mock]))

(defn account-repository
  ([] (account-repository {}))
  ([state] {:repository (repo-mock/->account-repository-mock state)}))

(defmulti repository :type)

(defmethod repository :transaction [{:keys [state]}]
  {:repository (transaction-repo-mock/->transaction-repository-mock state)})
