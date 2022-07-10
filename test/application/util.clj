(ns application.util)

(defn- map-or-vector? [v]
  (or (map? v)
      (vector? v)))

(def error-matcher {:success false
                    :data    {:error map-or-vector?}})

(defn matcher
  ([data] (matcher true data))
  ([success data]
   {:success success
    :data    data}))
