(ns mock.mock-utils
  (:require [mock.repository.account-repository-mock :as repo-mock]))

(defn account-repository []
  (repo-mock/->account-repository-mock {}))