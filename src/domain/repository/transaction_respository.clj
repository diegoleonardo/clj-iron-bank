(ns domain.repository.transaction-respository)

(defprotocol transaction-repository
  (add-fund [this add-fund-input])
  (current-balance [this reference-id]))
