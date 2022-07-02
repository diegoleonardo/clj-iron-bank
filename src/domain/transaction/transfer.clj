(ns domain.transaction.transfer
  (:require [domain.validator :as validator]
            [domain.balance :as balance]
            [domain.schema.transaction :as schema]))

(defn transfer [{:keys [source destiny amount] :as transfer}]
  (if-let [error (validator/humanized-error schema/suficient-balance?
                                            transfer)]
    error
    (let [from (balance/new-balance source clojure.core/- amount)
          to   (balance/new-balance destiny clojure.core/+ amount)]
      {:transfer {:source  from
                  :destiny to}})))
