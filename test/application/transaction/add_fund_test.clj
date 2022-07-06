(ns application.transaction.add-fund-test
  (:require [application.transaction.add-fund :as add-fund]
            [clojure.test :refer [deftest testing is]]
            [mock.mock-utils :as mock-utils]
            [matcher-combinators.test :refer [match?]]))

(def repository (mock-utils/repository {:type :transaction
                                        :state {}}))

(deftest execute
  (testing "should add fund to an account"
    (is (match? {:id string?}
                (add-fund/execute repository
                                  {:account {:balance 5.0}
                                   :amount  5.0}))))

  (testing "should not be able to add fund to an account when amount is invalid"
    (is (match? {:error vector?}
                (add-fund/execute repository
                                  {:account {:balance 5.0}
                                   :amount  -1})))))
