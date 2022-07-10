(ns utils.account
  (:require [mock.utils :as mock-utils]))

(def reference-id "3566688d-98d9-498d-be5d-c0227cec1ec8")

(def account-example {:person  {:first-name "John"
                                :last-name  "Snow"
                                :age        18}
                      :account {:username "john_snow"
                                :password "got_2021"
                                :email    "john_snow@got.com"
                                :document {:id   1
                                           :code :us}
                                :type     "NP"}})

(def account-data-store-example {reference-id account-example})

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

(def expected-response {:person  map?
                        :account map?})

(def expected-matcher (matcher {:person map?
                                :account map?}))

(defn deps
  ([] (deps {}))
  ([data]
   (mock-utils/account-repository data)))
