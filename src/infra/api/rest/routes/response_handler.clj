(ns infra.api.rest.routes.response-handler)

(defn- response-map [status data]
  {:status status :body data})

(defn response [{:keys [success data]}]
  (if success
    (response-map 200 data)
    (response-map 400 data)))

(defn not-found []
  {:status 404 :body {}})
