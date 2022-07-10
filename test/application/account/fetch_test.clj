(ns application.account.fetch-test
  (:require [application.account.fetch :as fetch]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]
            [utils.account :as account-utils]))

(deftest execute
  (let [ref-id account-utils/reference-id
        dependencies (account-utils/deps {ref-id account-utils/account-example})]
    (testing "should return an account when send valid id"
      (is (match? account-utils/expected-matcher
                  (fetch/execute dependencies ref-id))))))
