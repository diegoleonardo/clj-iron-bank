(ns infra.api.rest.routes.account
  (:require [infra.api.rest.middleware :as middleware]
            [application.account.create :as create]
            [application.account.fetch :as fetch]
            [application.account.update :as update]
            [infra.api.rest.routes.response-handler :as response])
  (:refer-clojure :exclude [update]))

(defn- create-handler [{:keys [body-params deps]}]
  (let [result (create/execute! deps body-params)]
    (response/response result)))

(defn- update-handler [{:keys [path-params body-params deps]}]
  (let [reference-id (:reference-id path-params)
        data         (->> reference-id
                          (assoc body-params :reference-id)
                          (update/execute! deps))]
    (response/response data)))

(defn- fetch-handler [{:keys [path-params deps]}]
  (let [reference-id              (:reference-id path-params)
        {:keys [data] :as result} (fetch/execute deps reference-id)]
    (if (empty? data)
      (response/not-found)
      (response/response result))))

(def wrap-keyword-code
  {:name ::wrap-keyword-code
   :wrap (fn [handler]
           (fn [{:keys [body-params] :as request}]
             (let [new-request (->> (update-in body-params [:account :document :code] keyword)
                                    (assoc request :body-params))]
               (handler new-request))))})

(defn route [deps]
  ["/account"
   {:middleware [(middleware/wrap-dependencies deps)]}
   [""
    {:middleware [wrap-keyword-code]
     :post       {:summary "Route to create an account"
                  :handler create-handler}}]
   ["/:reference-id" {:get {:summary "Route to get an account data"
                            :handler fetch-handler}

                      :patch {:summary    "Route to update an account"
                              :middleware [wrap-keyword-code]
                              :handler    update-handler}}]])
