(ns domain.balance)

(defn decimal->cents [amount]
  (Math/round (* amount 100)))

(defn cents->decimal [cents]
  (double (/ cents 100)))

(defn new-balance [account fn amount]
  (update account
          :balance
          fn
          amount))
