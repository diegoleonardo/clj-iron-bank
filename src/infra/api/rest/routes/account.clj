(ns infra.api.rest.routes.account
  (:require [application.account.create :as create]
            [application.account.fetch :as fetch]
            [application.account.update :as update])
  (:refer-clojure :exclude [update]))

(defn- create-handler [{:keys [body-params deps]}]
  (let [id (create/execute! deps body-params)]
    {:status 200
     :body   id}))

(defn- update-handler [{:keys [path-params body-params deps]}]
  (let [reference-id (:reference-id path-params)
        data         (->> reference-id
                          (assoc body-params :reference-id)
                          (update/execute! deps))]
    {:status 200
     :body   data}))

(defn- fetch-handler [{:keys [path-params deps]}]
  (let [reference-id (:reference-id path-params)
        account      (fetch/execute deps reference-id)]
    {:status 200
     :body   account}))

(defn- wrap-dependencies [deps]
  {:name ::wrap-dependencies
   :wrap (fn [handler]
           (fn [request]
             (let [new-request (assoc request :deps deps)]
               (handler new-request))))})

(defn route [deps]
  ["/account"
   {:middleware [(wrap-dependencies deps)]}
   ["" {:post {:summary "Route to create an account"
               :handler create-handler}}]
   ["/:reference-id" {:get {:summary "Route to get an account data"
                            :handler fetch-handler}

                      :patch {:summary "Route to update an account"
                              :handler update-handler}}]])
