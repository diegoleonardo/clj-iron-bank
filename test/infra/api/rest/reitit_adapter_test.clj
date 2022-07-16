(ns infra.api.rest.reitit-adapter-test
  (:require [infra.api.http-router :as router]
            [infra.api.rest.reitit-adapter :as adapter]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]))

(def route-example [["/test"
                     {:get
                      {:handler (fn [_]
                                  {:status 200
                                   :body   "hello world"})}}]])

(deftest create-route
  (testing "should be possible creating a route"
    (let [route (router/create (adapter/->reitit-adapter route-example))]
      (is (match? {:status 200
                   :body   "hello world"}
                  (route {:request-method :get
                          :uri            "/api/v1/test"}))))))
