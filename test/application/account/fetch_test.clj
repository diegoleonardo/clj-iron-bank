(ns application.account.fetch-test
  (:require [application.account.fetch :as fetch]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]
            [mock.utils :as mock-utils]))

(def expected-matcher {:person map?
                       :account map?})

(defn deps
  ([] (deps {}))
  ([data]
   (mock-utils/account-repository data)))

(def account-id "3566688d-98d9-498d-be5d-c0227cec1ec8")

(def data {:person  {:first-name "John"
                     :last-name  "Snow"
                     :age        18}
           :account {:username "john_snow"
                     :password "got_2021"
                     :email    "john_snow@got.com"
                     :document {:id   1
                                :code :us}
                     :type     "NP"}})

(deftest execute
  (let [dependencies (deps {account-id data})]
    (testing "should return an account when send valid id"
      (is (match? expected-matcher
                  (fetch/execute dependencies account-id))))))
