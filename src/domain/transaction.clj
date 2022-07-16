(ns domain.transaction
  (:require [domain.validator :as validator]
            [domain.balance :as balance]
            [domain.schema.transaction :as schema]))

(defn deposit [{:keys [account amount] :as transaction}]
  (if-let [error (validator/humanized-error schema/is-amount-valid? amount)]
    error
    (->> (balance/new-balance account clojure.core/+ amount)
         :balance
         (assoc transaction :balance))))

(defn debit [{:keys [account amount] :as transaction}]
  (if-let [error (validator/humanized-error schema/is-amount-valid? amount)]
    error
    (->> (balance/new-balance account clojure.core/- amount)
         :balance
         (assoc transaction :balance))))

(defn transfer [{:keys [source destiny amount] :as transfer}]
  (if-let [error (validator/humanized-error schema/suficient-balance?
                                            transfer)]
    error
    (let [from (debit {:account source :amount amount})
          to   (deposit {:account destiny :amount amount})]
      {:transfer {:source  from
                  :destiny to}})))
