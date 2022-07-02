(ns domain.balance-test
  (:require [domain.balance :as balance]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]))

(deftest decimal->cents
  (testing "should parse to cents"
    (is (= 4503 (balance/decimal->cents 45.03)))))

(deftest cents->decimal
  (testing "should parse to decimal"
    (is (= 68.75 (balance/cents->decimal 6875)))))

(deftest new-balance
  (testing "should add funds to balance"
    (is (match? {:balance 10.0}
                (balance/new-balance {:balance 5.0}
                                     clojure.core/+
                                     5.0))))

  (testing "should debit funds to balance"
    (is (match? {:balance 5.0}
                (balance/new-balance {:balance 10.0}
                                     clojure.core/-
                                     5.0)))))
