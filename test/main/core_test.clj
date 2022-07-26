(ns main.core-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [matcher-combinators.test :refer [match?]]
            [utils.account :as account-utils]
            [main.core :as core]
            [utils.http-utils :as http-utils]))

(def account-endpoint "http://localhost:3000/api/v1/account")

(def funds-endpoint "http://localhost:3000/api/v1/transactions/funds")

(def transfers-endpoint "http://localhost:3000/api/v1/transactions/transfers")

(defn- create-account [account]
  (->> account
       (http-utils/post account-endpoint)
       http-utils/str-body->map
       :reference-id))

(defn once-fixture [tests]
  (try
    (core/-main "test")
    (tests)
    (finally
      (core/-close))))

(use-fixtures :once once-fixture)

(deftest account
  (testing "should start up the application and execute the account flow"
    (let [account-example    account-utils/account-example
          response           (http-utils/post account-endpoint account-example)
          account-id         (->> response
                                  http-utils/str-body->map
                                  :reference-id)
          account-path-param (str account-endpoint "/" account-id)]
      (testing "should have been created an account"
        (is (match? {:status 200}
                    response)))
      (testing "should be possible fetch the account"
        (let [fetch_response (http-utils/get-request account-path-param)]
          (is (match? {:status 200 :body string?}
                      fetch_response))))
      (testing "should be possible update the account"
        (let [updated-last-name "Targaryen"
              updated-account   (assoc-in account-example [:person :last-name] updated-last-name)
              patch_response    (http-utils/patch account-path-param updated-account)]
          (is (match? {:status 200 :body string?}
                      patch_response))
          (is (= updated-last-name
                 (-> (http-utils/get-request account-path-param)
                     http-utils/str-body->map
                     (get-in [:person :last-name])))))))))

(deftest transactions
  (testing "should start up the application and execute the transaction flow"
    (let [account-id-one (create-account account-utils/account-example)
          account-id-two (-> (assoc-in account-utils/account-example [:person :last-name] "Targaryen")
                             create-account)]
      (testing "should be possible to add funds to an account"
        (let [response (http-utils/post funds-endpoint {:reference-id account-id-one
                                                        :amount       15.0})
              balance  (->> response
                            http-utils/str-body->map
                            :balance)]
          (is (match? {:status 200 :body string?}
                      response))
          (is (= 15.0 balance))))

      (testing "should be possible to debit funds to an account"
        (let [response (http-utils/delete funds-endpoint {:reference-id account-id-one
                                                          :amount       5.0})
              balance  (->> response
                            http-utils/str-body->map
                            :balance)]
          (is (match? {:status 200 :body string?}
                      response))
          (is (= 10.0 balance))))

      (testing "should be possible to transfer funds between accounts"
        (let [response (http-utils/post transfers-endpoint {:source  {:reference-id account-id-one}
                                                            :destiny {:reference-id account-id-two}
                                                            :amount  5.0})
              body     (http-utils/str-body->map response)]
          (is (match? {:status 200 :body string?}
                      response))
          (is (match? {:source  {:balance 5.0}
                       :destiny {:balance 5.0}}
                      body)))))))
