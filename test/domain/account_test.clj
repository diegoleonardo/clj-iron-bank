(ns domain.account-test
  (:require [domain.account :as account]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]
            [domain.validator :as validator]))

(deftest create-account
  (testing "should be possible create an account when input data is valid"
    (is (match? {:person  map?
                 :account {:balance 0}}
                (account/create {:person  {:first-name "Diego"
                                           :last-name  "Santos"
                                           :age        20}
                                 :account {:document {:id   1
                                                      :code :us}
                                           :username "diegolpd"
                                           :password "12345"
                                           :email    "foo@bar.com.br"
                                           :number   1
                                           :code     1
                                           :type     "LP"
                                           :balance  10000}}))))

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
