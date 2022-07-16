(ns infra.api.rest.reitit-adapter
  (:require [infra.api.http-router :as router]
            [reitit.ring :as ring]
            [reitit.coercion.spec]
            [reitit.coercion.malli]
            [reitit.ring.malli]
            [reitit.swagger :as swagger]
            [reitit.ring.coercion :as coercion]
            [reitit.dev.pretty :as pretty]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [muuntaja.core :as m]))

(defn- create-routes [routes]
  (ring/ring-handler
   (ring/router
    ["/api/v1" routes]
    {;;:reitit.middleware/transform dev/print-request-diffs ;; pretty diffs
      ;;:validate spec/validate ;; enable spec validation for route data
      ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
      :exception pretty/exception
      :data      {:coercion   (reitit.coercion.malli/create
                               {;; set of keys to include in error messages
                                :error-keys #{#_:type :humanized #_:transformed}
                                ;; schema identity function (default: close all map schemas)
                                ;;:compile mu/closed-schema
                                ;; strip-extra-keys (effects only predefined transformers)
                                :strip-extra-keys true
                                ;; add/set default values
                                :default-values true
                                ;; malli options
                                :options nil})
                  :muuntaja   m/instance
                  :middleware [;; swagger feature
                               swagger/swagger-feature
                               ;; query-params & form-params
                               parameters/parameters-middleware
                               ;; content-negotiation
                               muuntaja/format-negotiate-middleware
                               ;; encoding response body
                               muuntaja/format-response-middleware
                               ;; change humanized response property to errors
                               ;;mdw/wrap-rename-humanized-key-to-errors
                               ;; exception handling
                               exception/exception-middleware
                               ;; decoding request body
                               muuntaja/format-request-middleware
                               ;; coercing response bodys
                               coercion/coerce-response-middleware
                               ;; coercing request parameters
                               coercion/coerce-request-middleware
                               ;; multipart
                               multipart/multipart-middleware
                               #_mdw/wrap-to-camel-case]}})))

(defrecord reitit-adapter [routes]
  router/http-router

  (create [_]
    (create-routes routes)))
