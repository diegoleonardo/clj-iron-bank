(ns infra.api.rest.routes.transaction-test
  (:require [clojure.test :refer [deftest testing is]]
            [infra.api.http-router :as router]
            [infra.api.rest.reitit-adapter :as adapter]
            [matcher-combinators.test :refer [match?]]
            [muuntaja.core :as m]
            [mock.utils :as utils]
            [infra.api.rest.routes.transaction :as transaction]))

(defn route [state]
  (-> (utils/repository {:type  :transaction
                         :state state})
      transaction/route
      adapter/->reitit-adapter
      router/create))

(deftest add-fund
  (testing "should create a route to add funds"
    (let [route    (route {"1" [{:balance 10.0}]})
          response (route {:request-method :post
                           :uri            "/v1/transactions/funds"
                           :headers        {"content-type" "application/edn"
                                            "accept"       "application/transit+json"}
                           :body-params    {:reference-id "1"
                                            :amount       10.0}})]
      (is (match? {:status 200} response))
      (is (match? {:balance 20.0 :id string?}
                  (m/decode-response-body response)))))

  (testing "should return bad request when data is invalid"
    (let [route    (route {"1" [{:balance 10.0}]})
          response (route {:request-method :post
                           :uri            "/v1/transactions/funds"
                           :headers        {"content-type" "application/edn"
                                            "accept"       "application/transit+json"}
                           :body-params    {:amount       10.0}})]
      (is (match? {:status 400} response)))))

(deftest remove-fund
  (testing "should create a route to remove funds"
    (let [route    (route {"1" [{:balance 10.0}]})
          response (route {:request-method :delete
                           :uri            "/v1/transactions/funds"
                           :headers        {"content-type" "application/edn"
                                            "accept"       "application/transit+json"}
                           :body-params    {:reference-id "1"
                                            :amount       10.0}})]
      (is (match? {:status 200} response))
      (is (match? {:balance 0.0 :id string?}
                  (m/decode-response-body response)))))

  (testing "should return bad request when data is invalid"
    (let [route    (route {"1" [{:balance 10.0}]})
          response (route {:request-method :delete
                           :uri            "/v1/transactions/funds"
                           :headers        {"content-type" "application/edn"
                                            "accept"       "application/transit+json"}
                           :body-params    {:amount 10.0}})]
      (is (match? {:status 400} response)))))

(deftest transfer-fund
  (testing "should create a route to remove funds"
    (let [source-id  "1"
          destiny-id "2"
          route      (route {source-id  [{:balance 15.0}]
                             destiny-id [{:balance 5.0}]})
          response   (route {:request-method :post
                             :uri            "/v1/transactions/transfers"
                             :headers        {"content-type" "application/edn"
                                              "accept"       "application/transit+json"}
                             :body-params    {:source  {:reference-id source-id}
                                              :destiny {:reference-id destiny-id}
                                              :amount  5.0}})]
      (is (match? {:status 200} response))
      (is (match? {:source  {:balance 10.0 :id string?}
                   :destiny {:balance 10.0 :id string?}}
                  (m/decode-response-body response)))))

  (testing "should return a bad request when data is invalid"
    (let [source-id  "1"
          destiny-id "2"
          route      (route {source-id  [{:balance 15.0}]
                             destiny-id [{:balance 5.0}]})
          response   (route {:request-method :post
                             :uri            "/v1/transactions/transfers"
                             :headers        {"content-type" "application/edn"
                                              "accept"       "application/transit+json"}
                             :body-params    {:source  {:reference-id source-id}
                                              :amount  5.0}})]
      (is (match? {:status 400} response)))))
