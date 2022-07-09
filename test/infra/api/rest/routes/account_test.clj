(ns infra.api.rest.routes.account-test
  (:require [infra.api.rest.routes.account :as account]
            [clojure.test :refer [deftest testing is]]
            [infra.api.http-router :as router]
            [infra.api.rest.reitit-adapter :as adapter]
            [mock.utils :as mock-utils]
            [matcher-combinators.test :refer [match?]]
            [muuntaja.core :as m]))

(def repository (mock-utils/repository {:type  :account
                                        :state {}}))

(def account-example {:person  {:first-name "John"
                                :last-name  "Snow"
                                :age        18}
                      :account {:username "john_snow"
                                :password "got_2021"
                                :email    "john_snow@got.com"
                                :document {:id   1
                                           :code :us}
                                :type     "NP"}})

(def expected-matcher {:person  map?
                       :account map?})

(defn deps
  ([] (deps {}))
  ([data]
   (mock-utils/account-repository data)))

(def reference-id "3566688d-98d9-498d-be5d-c0227cec1ec8")

(deftest route
  (testing "should create a route"
    (testing "to create an account"
      (let [route    (-> repository
                         account/route
                         adapter/->reitit-adapter
                         router/create)
            response (route {:request-method :post
                             :uri            "/v1/account"
                             :headers        {"content-type" "application/edn"
                                              "accept"       "application/transit+json"}
                             :body-params    account-example})]
        (is (match? {:status 200} response))
        (is (match? {:success true
                     :data    {:account-id string?}}
                    (m/decode-response-body response)))))

    (testing "to fetch an account"
      (let [route    (-> (deps {reference-id account-example})
                         account/route
                         adapter/->reitit-adapter
                         router/create)
            response (route {:request-method :get
                             :uri            (str "/v1/account/" reference-id)
                             :headers        {"content-type" "application/edn"
                                              "accept"       "application/transit+json"}})]
        (is (match? {:status 200} response))
        (is (match? expected-matcher
                    (m/decode-response-body response)))))

    (testing "to patch an account"
      (let [route    (-> (deps {reference-id account-example})
                         account/route
                         adapter/->reitit-adapter
                         router/create)
            response (route {:request-method :patch
                             :uri            (str "/v1/account/" reference-id)
                             :headers        {"content-type" "application/edn"
                                              "accept"       "application/transit+json"}
                             :body-params    account-example})]
        (is (match? {:status 200} response))
        (is (match? expected-matcher
                    (m/decode-response-body response)))))))
