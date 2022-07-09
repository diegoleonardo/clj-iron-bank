(ns infra.api.http-router)

(defprotocol http-router
  (create [this]))
