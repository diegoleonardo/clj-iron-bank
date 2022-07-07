(ns domain.repository.account-repository)

(defprotocol account-repository
  (create [this account])
  (fetch [this reference-id])
  (patch [this reference-id account]))
