(ns infra.api.rest.routes.account-test
  (:require [infra.api.rest.routes.account :as account]
            [clojure.test :refer [deftest testing is]]
            [infra.api.http-router :as router]
            [infra.api.rest.reitit-adapter :as adapter]
            [matcher-combinators.test :refer [match?]]
            [muuntaja.core :as m]
            [utils.account :as account-utils]))

(def path "/api/v1/account")

(deftest create-account
  (testing "should create a route to create an account"
    (let [route    (-> (account-utils/deps)
                       account/route
                       adapter/->reitit-adapter
                       router/create)
          response (route {:request-method :post
                           :uri            path
                           :headers        {"content-type" "application/edn"
                                            "accept"       "application/transit+json"}
                           :body-params    account-utils/account-example})]
      (is (match? {:status 200} response))
      (is (match? {:account-id string?}
                  (m/decode-response-body response))))))

(deftest fetch
  (testing "should create a route to fetch an account"
    (testing "found"
      (let [route    (-> (account-utils/deps account-utils/account-data-store-example)
                         account/route
                         adapter/->reitit-adapter
                         router/create)
            response (route {:request-method :get
                             :uri            (str path "/" account-utils/reference-id)
                             :headers        {"content-type" "application/json"
                                              "accept"       "application/transit+json"}})]
        response
        (is (match? {:status 200} response))
        (is (match? account-utils/expected-response
                    (m/decode-response-body response)))))

    (testing "not found"
      (let [route    (-> (account-utils/deps)
                         account/route
                         adapter/->reitit-adapter
                         router/create)
            response (route {:request-method :get
                             :uri            (str path "/" account-utils/reference-id)
                             :headers        {"content-type" "application/json"
                                              "accept"       "application/transit+json"}})]
        (is (match? {:status 404} response))
        (is (match? {}
                    (m/decode-response-body response)))))))

(deftest patch
  (testing "should create a route to patch an account"
    (let [route    (-> (account-utils/deps account-utils/account-data-store-example)
                       account/route
                       adapter/->reitit-adapter
                       router/create)
          response (route {:request-method :patch
                           :uri            (str path "/" account-utils/reference-id)
                           :headers        {"content-type" "application/edn"
                                            "accept"       "application/transit+json"}
                           :body-params    account-utils/account-example})]
      (is (match? {:status 200} response))
      (is (match? account-utils/expected-response
                  (m/decode-response-body response))))))
