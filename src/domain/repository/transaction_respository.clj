(ns domain.repository.transaction-respository)

(defprotocol transaction-repository
  (save [this transaction])
  (current-balance [this reference-id]))
