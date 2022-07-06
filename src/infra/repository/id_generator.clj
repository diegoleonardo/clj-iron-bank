(ns infra.repository.id-generator
  (:import [java.util UUID]))

(defn id []
  (-> (UUID/randomUUID)
      str))
