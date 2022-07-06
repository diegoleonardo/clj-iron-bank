(ns mock.utils
  (:require [mock.account-repository-mock :as account-repo-mock]
            [mock.transaction-repository-mock :as transaction-repo-mock]))

(defn account-repository
  ([] (account-repository {}))
  ([state] {:repository (account-repo-mock/->account-repository-mock state)}))

(defmulti repository :type)

(defmethod repository :transaction [{:keys [state]}]
  {:repository (transaction-repo-mock/->transaction-repository-mock state)})

(defmethod repository :account [{:keys [state]}]
  {:repository (account-repo-mock/->account-repository-mock (or state {}))})
