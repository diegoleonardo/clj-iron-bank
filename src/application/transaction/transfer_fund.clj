(ns application.transaction.transfer-fund
  (:require [domain.transaction :as transaction]
            [domain.validator :as validator]
            [domain.schema.transaction :as schema]
            [domain.repository.transaction-respository :as transaction-repository]))

(defn- process [repository transfer-input]
  (let [{{:keys [source destiny]} :transfer} (transaction/transfer transfer-input)
        {:keys [source-id destiny-id]}       (transaction-repository/transfer repository source destiny)]
    {:source  {:id source-id :balance (:balance source)}
     :destiny {:id destiny-id :balance (:balance destiny)}}))

(defn execute! [{:keys [repository]} {:keys [source destiny amount] :as transfer-input}]
  (let [source-current-balance (transaction-repository/current-balance repository (:reference-id source))
        destiny-current-balance (transaction-repository/current-balance repository (:reference-id destiny))
        transfer-with-balance (-> transfer-input
                                  (assoc-in [:source :balance] source-current-balance)
                                  (assoc-in [:destiny :balance] destiny-current-balance))]
    (if-let [error (or (validator/humanized-error schema/is-amount-valid? amount)
                       (validator/humanized-error schema/is-amount-valid? source-current-balance)
                       (validator/humanized-error schema/is-amount-valid? destiny-current-balance)
                       (validator/humanized-error schema/suficient-balance? transfer-with-balance))]
      error
      (process repository transfer-with-balance))))
