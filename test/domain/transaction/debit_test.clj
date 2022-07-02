(ns domain.transaction.debit-test
  (:require [domain.transaction.debit :as debit]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]))

(deftest debit
  (testing "should be possible to debit when input data is valid"
    (is (match? {:debit {:account {:balance 5.0}}}
                (debit/debit {:account {:balance  10.0
                                        :username "diego"}
                              :amount  5.0}))))

  (testing "should not be possible to debit when input data is invalid"
    (is (match? {:error vector?}
                (debit/debit {:account {:balance  10.0
                                        :username "diego"}
                              :amount  -1})))))
