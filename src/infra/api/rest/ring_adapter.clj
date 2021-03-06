(ns infra.api.rest.ring-adapter
  (:require [infra.api.http-server :as http-server]
            [ring.adapter.jetty :as jetty]))

(defrecord ring-adapter [routes]
  http-server/http-server

  (listen [_ {:keys [port join?]}]
    (jetty/run-jetty routes
                     {:port  port
                      :join? join?})))
