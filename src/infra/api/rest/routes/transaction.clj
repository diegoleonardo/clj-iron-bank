(ns infra.api.rest.routes.transaction
  (:require [infra.api.rest.middleware :as middleware]
            [infra.api.rest.routes.response-handler :as response]
            [application.transaction.add-fund :as add-fund]
            [application.transaction.remove-fund :as remove-fund]
            [application.transaction.transfer-fund :as transfer-fund]))

(defn- add [{:keys [body-params deps]}]
  (-> (add-fund/execute! deps body-params)
      response/response))

(defn- debit [{:keys [body-params deps]}]
  (-> (remove-fund/execute! deps body-params)
      response/response))

(defn- transfer [{:keys [body-params deps]}]
  (-> (transfer-fund/execute! deps  body-params)
      response/response))

(defn route [deps]
  ["/transactions"
   {:middleware [(middleware/wrap-dependencies deps)]}
   ["/funds" {:post   {:summary "Route to add funds"
                       :handler add}
              :delete {:summary "Route to remove funds"
                       :handler debit}}]
   ["/transfers" {:post {:summary "Route to transfer funds"
                         :handler transfer}}]])
