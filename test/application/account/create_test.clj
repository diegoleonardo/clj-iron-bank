(ns application.account.create-test
  (:require [application.account.create :as account-create]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]
            [utils.account :as account-utils]
            [utils.application-matcher :as application-matcher]))

(defn- assert-error [data]
  (is (match? application-matcher/error-matcher
              (account-create/execute! account-utils/deps
                                       data))))

(defn- dissoc-item [data key property]
  (let [new-item (-> data
                     key
                     (dissoc property))]
    (assoc data key new-item)))

(deftest execute
  (testing "should be possible creating an account"
    (is (match? (application-matcher/matcher {:reference-id string?})
                (account-create/execute! (account-utils/deps)
                                         account-utils/account-example))))

  (testing "should not be possible creating an account"
    (testing "when person data doens't exist"
      (assert-error (dissoc account-utils/account-example :person)))

    (testing "when person's required field is missing"
      (assert-error (dissoc-item account-utils/account-example :person :first-name))
      (assert-error (dissoc-item account-utils/account-example :person :last-name))
      (assert-error (dissoc-item account-utils/account-example :person :age)))

    (testing "when person's data is invalid"
      (assert-error (assoc-in account-utils/account-example [:person :first-name] 10))
      (assert-error (assoc-in account-utils/account-example [:person :last-name] 10))
      (assert-error (assoc-in account-utils/account-example [:person :age] "10")))

    (testing "when the person's age is below the minimum age"
      (assert-error (assoc-in account-utils/account-example [:person :age] 17)))

    (testing "when the account's data doesn't exist"
      (assert-error (dissoc account-utils/account-example :account)))

    (testing "when the account's required field is missing"
      (assert-error (dissoc-item account-utils/account-example :account :username))
      (assert-error (dissoc-item account-utils/account-example :account :password))
      (assert-error (dissoc-item account-utils/account-example :account :email))
      (assert-error (dissoc-item account-utils/account-example :account :document))
      (assert-error (dissoc-item account-utils/account-example :account :type)))

    (testing "when account's data is invalid"
      (assert-error (assoc-in account-utils/account-example [:account :username] 10))
      (assert-error (assoc-in account-utils/account-example [:account :password] 10))
      (assert-error (assoc-in account-utils/account-example [:account :email] 10))
      (assert-error (assoc-in account-utils/account-example [:account :type] 10)))

    (testing "when account's type neither Natural Person (NP) nor Legal Person (LP)"
      (assert-error (assoc-in account-utils/account-example [:account :type] "FP")))

    (testing "when account's document code is :us, but the id property is invalid or doesn't exist"
      (assert-error (assoc-in account-utils/account-example [:account :document] {:code :us}))
      (assert-error (assoc-in account-utils/account-example [:account :document] {:cpf "12345" :code :us}))
      (assert-error (assoc-in account-utils/account-example [:account :document] {:id "12345" :code :us})))

    (testing "when account's document code is :br, but the cpf property is invalid or doesn't exist"
      (assert-error (assoc-in account-utils/account-example [:account :document] {:code :br}))
      (assert-error (assoc-in account-utils/account-example [:account :document] {:cpf 1 :code :br}))
      (assert-error (assoc-in account-utils/account-example [:account :document] {:id "12345" :code :br})))))
