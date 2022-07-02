(ns domain.transaction.deposit-test
  (:require [domain.transaction.deposit :as deposit]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]))

(deftest debit
  (testing "should be possible to add funds when input data is valid"
    (is (match? {:deposit {:account {:balance 20.0}}}
                (deposit/deposit {:account {:balance  10.0
                                            :username "diego"}
                                  :amount  10.0}))))

  (testing "should not be possible to add funds when input data is invalid"
    (is (match? {:error vector?}
                (deposit/deposit {:account {:balance  10.0
                                            :username "diego"}
                                  :amount  -1})))))
