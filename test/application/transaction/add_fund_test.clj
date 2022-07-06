(ns application.transaction.add-fund-test
  (:require [application.transaction.add-fund :as add-fund]
            [clojure.test :refer [deftest testing is]]
            [mock.utils :as mock-utils]
            [matcher-combinators.test :refer [match?]]))

(def id "1234")

(def repository (mock-utils/repository {:type :transaction
                                        :state {}}))

(deftest execute!
  (testing "should add fund to an account"
    (let [repo (mock-utils/repository {:type :transaction
                                       :state {id {:balance 5.0}}})]
      (is (match? {:id      string?
                   :balance 10.0}
                  (add-fund/execute! repo
                                     {:reference-id id
                                      :amount  5.0})))))

  (testing "should not be able to add fund to an account when amount is invalid"
    (is (match? {:error vector?}
                (add-fund/execute! repository
                                   {:account {:balance 5.0}
                                    :amount  -1}))))

  (testing "should return error when it is not possible fetch the current balance"
    (is (match? {:error vector?}
                (add-fund/execute! repository
                                   {:reference-id id
                                    :amount 5.0})))))
