(ns main.system
  (:require [infra.api.rest.ring-adapter :as ring-adapater]
            [infra.api.rest.reitit-adapter :as reitit-adapter]
            [infra.api.rest.routes.account :as account-router]
            [infra.api.rest.routes.transaction :as transaction-router]
            [infra.repository.account-memory-repository :as account-memory-repository]
            [infra.repository.transaction-memory-repository :as transaction-memory-repository]
            [infra.api.http-router :as http-router]
            [infra.api.http-server :as http-server]))

(def ^:private profiles #{:prod :homolog :dev :test})

(defn- profile [environment]
  (let [env (keyword environment)]
    (get profiles env :test)))

(defn- repositories [env]
  (let [profile (profile env)]
    (cond
      (= profile :test)
      {:account-repo     {:repository (account-memory-repository/->account-memory-repository (atom {}))}
       :transaction-repo {:repository (transaction-memory-repository/->transaction-memory-repository (atom {}))}}

      :else
      {:account-repo     {:repository (account-memory-repository/->account-memory-repository (atom {}))}
       :transaction-repo {:repository (transaction-memory-repository/->transaction-memory-repository (atom {}))}})))

(defn config [env]
  (let [{:keys [account-repo transaction-repo]} (repositories env)
        routes                                  (-> []
                                                    (into [(account-router/route account-repo)])
                                                    (into [(transaction-router/route transaction-repo)]))
        server-adapter                          (->> routes
                                                     reitit-adapter/->reitit-adapter
                                                     http-router/create
                                                     ring-adapater/->ring-adapter)]
    (http-server/listen server-adapter {:port  3000
                                        :join? false})))
