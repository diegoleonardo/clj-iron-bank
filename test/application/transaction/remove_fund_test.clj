(ns application.transaction.remove-fund-test
  (:require [application.transaction.remove-fund :as remove-fund]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]
            [mock.utils :as mock-utils]
            [application.util :as utils]))

(def id "1234")

(def repository (mock-utils/repository {:type :transaction
                                        :state {}}))

(deftest execute!
  (testing "should remove fund to an account"
    (let [repo (mock-utils/repository {:type  :transaction
                                       :state {id [{:balance 5.0}]}})]
      (is (match? (utils/matcher {:id      string?
                                  :balance 0.0})
                  (remove-fund/execute! repo
                                        {:reference-id id
                                         :amount       5.0})))))

  (testing "should not be able to remove fund to an account when amount is invalid"
    (is (match? utils/error-matcher
                (remove-fund/execute! repository
                                      {:account {:balance 5.0}
                                       :amount  -1}))))

  (testing "should return error when it is not possible fetch the current balance"
    (is (match? utils/error-matcher
                (remove-fund/execute! repository
                                      {:reference-id id
                                       :amount       5.0})))))
