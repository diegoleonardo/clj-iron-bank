(ns domain.repository.account-repository)

(defprotocol account-repository
  (create [this account])
  (fetch [this account-id])
  (patch [this account-id account]))
