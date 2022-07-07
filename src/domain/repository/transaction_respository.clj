(ns domain.repository.transaction-respository)

(defprotocol transaction-repository
  (save [this transaction])
  (transfer [this source destiny])
  (current-balance [this reference-id]))
