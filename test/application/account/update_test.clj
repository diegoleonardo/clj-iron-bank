(ns application.account.update-test
  (:require [application.account.update :as update-account]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]
            [mock.utils :as mock-utils]))

(def account {:person  {:first-name "John"
                        :last-name  "Snow"
                        :age        18}
              :account {:username "john_snow"
                        :password "got_2021"
                        :email    "john_snow@got.com"
                        :document {:id   1
                                   :code :us}
                        :type     "NP"}})

(def account-example (merge {:identifier "12345"}
                            account))

(defn init-state [id data]
  {id data})

(def repository
  (let [state (-> account-example
                  :identifier
                  (init-state account-example))]
    (mock-utils/repository {:type :account
                            :state state})))

(deftest execute
  (testing "should update the account data when input is valid"
    (let [updated-data (update-in account-example
                                  [:person :first-name]
                                  (fn [_ n]
                                    n)
                                  "Tyrion")]
      (is (match? {:person {:first-name "Tyrion"}}
                  (update-account/execute! repository updated-data)))))

  (testing "should return error when trying to update without identifier"
    (is (match? {:error {:identifier [string?]}}
                (update-account/execute! repository account))))

  (testing "should return error when trying to update an unexistent account"
    (let [mock-repo (mock-utils/repository {:type :account
                                            :state {}})]
      (is (match? {:error [string?]}
                  (update-account/execute! mock-repo
                                           account-example))))))