(ns domain.transaction-test
  (:require [domain.transaction :as transaction]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]))

(deftest deposit
  (testing "should be possible to add funds when input data is valid"
    (is (match? {:balance 20.0
                 :username "john snow"}
                (transaction/deposit {:account {:balance  10.0
                                                :username "john snow"}
                                      :amount  10.0}))))

  (testing "should not be possible to add funds when input data is invalid"
    (is (match? {:error vector?}
                (transaction/deposit {:account {:balance  10.0
                                                :username "john snow"}
                                      :amount  -1})))))

(deftest debit
  (testing "should be possible to debit when input data is valid"
    (is (match? {:balance 5.0
                 :username "john snow"}
                (transaction/debit {:account {:balance  10.0
                                              :username "john snow"}
                                    :amount  5.0}))))

  (testing "should not be possible to debit when input data is invalid"
    (is (match? {:error vector?}
                (transaction/debit {:account {:balance  10.0
                                              :username "john snow"}
                                    :amount  -1})))))

(deftest transfer
  (testing "should be possible to do the transfer when suficient balance"
    (is (match? {:transfer {:source  {:balance 0.0}
                            :destiny {:balance 200.0}}}
                (transaction/transfer {:source  {:balance 100.0}
                                       :destiny {:balance 100.0}
                                       :amount  100.0}))))

  (testing "should not be possible to do the transfer when insuficient balance"
    (is (match? {:error [string?]}
                (transaction/transfer {:source  {:balance 0.0}
                                       :destiny {:balance 100.0}
                                       :amount  100.0})))
    (is (match? {:error [string?]}
                (transaction/transfer {:source  {:balance 10.0}
                                       :destiny {:balance 100.0}
                                       :amount  100.0})))))
