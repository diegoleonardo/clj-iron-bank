(ns application.account.update-test
  (:require [application.account.update :as update-account]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]
            [utils.account :as account-utils]
            [utils.application-matcher :as application-matcher]))

(def account-example (merge {:reference-id "12345"}
                            account-utils/account-example))

(defn init-state [id data]
  {id data})

(def repository
  (let [state (-> account-example
                  :reference-id
                  (init-state account-example))]
    (account-utils/deps state)))

(deftest execute
  (testing "should update the account data when input is valid"
    (let [updated-data (update-in account-example
                                  [:person :first-name]
                                  (fn [_ n]
                                    n)
                                  "Tyrion")]
      (is (match? (application-matcher/matcher {:person {:first-name "Tyrion"}})
                  (update-account/execute! repository updated-data)))))

  (testing "should return error when trying to update without reference-id"
    (is (match? (application-matcher/matcher false {:error {:reference-id [string?]}})
                (update-account/execute! repository account-utils/account-example))))

  (testing "should return error when trying to update an unexistent account"
    (let [mock-repo (account-utils/deps)]
      (is (match? application-matcher/error-matcher
                  (update-account/execute! mock-repo
                                           account-example))))))
