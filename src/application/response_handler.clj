(ns application.response-handler)

(defn error [error]
  {:success false :data error})

(defn success [data]
  {:success true :data data})
