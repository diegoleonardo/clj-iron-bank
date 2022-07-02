(ns domain.transaction
  (:require [domain.validator :as validator]
            [domain.balance :as balance]
            [domain.schema.transaction :as schema]))

(defn deposit [{:keys [account amount]}]
  (if-let [error (validator/humanized-error schema/is-amount-valid? amount)]
    error
    (balance/new-balance account clojure.core/+ amount)))

(defn debit [{:keys [account amount]}]
  (if-let [error (validator/humanized-error schema/is-amount-valid? amount)]
    error
    (balance/new-balance account clojure.core/- amount)))

(defn transfer [{:keys [source destiny amount] :as transfer}]
  (if-let [error (validator/humanized-error schema/suficient-balance?
                                            transfer)]
    error
    (let [from (balance/new-balance source clojure.core/- amount)
          to   (balance/new-balance destiny clojure.core/+ amount)]
      {:transfer {:source  from
                  :destiny to}})))
