(ns domain.transaction.transfer-test
  (:require [domain.transaction.transfer :as transfer]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]))

(deftest transfer
  (testing "should be possible to do the transfer when suficient balance"
    (is (match? {:transfer {:source  {:balance 0.0}
                            :destiny {:balance 200.0}}}
                (transfer/transfer {:source  {:balance 100.0}
                                    :destiny {:balance 100.0}
                                    :amount  100.0}))))

  (testing "should not be possible to do the transfer when insuficient balance"
    (is (match? {:error [string?]}
                (transfer/transfer {:source  {:balance 0.0}
                                    :destiny {:balance 100.0}
                                    :amount  100.0})))
    (is (match? {:error [string?]}
                (transfer/transfer {:source  {:balance 10.0}
                                    :destiny {:balance 100.0}
                                    :amount  100.0})))))
