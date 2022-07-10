(ns domain.account-test
  (:require [domain.account :as account]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]
            [utils.account :as account-utils]))

(deftest create-account
  (testing "should be possible create an account when input data is valid"
    (is (match? {:person  map?
                 :account {:balance 0}}
                (account/create account-utils/account-example))))

  (testing "should not be create an account when input data is invalid"
    (is (match? {:error {:person  map?
                         :account map?}}
                (account/create {:person  {}
                                 :account {:username ""
                                           :password ""
                                           :document {:cpf  "foo"
                                                      :code :br}
                                           :type     1
                                           :number   "12345"
                                           :code     "4321"}})))))
