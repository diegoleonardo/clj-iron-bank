(ns domain.schema.transaction)

(def suficient-balance? [:and
                         [:map
                          [:source
                           [:map
                            [:balance [:and
                                       number?
                                       [:>= 0.0]]]]]
                          [:amount number?]]
                         [:fn {:error/message "Insuficient balance"}
                          (fn [{:keys [source amount]}]
                            (let [balance (:balance source)]
                              (>= balance amount)))]])

(def is-amount-valid? [:and
                       [number?]
                       [:> 0.0]])
