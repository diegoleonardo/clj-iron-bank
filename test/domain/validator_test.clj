(ns domain.validator-test
  (:require [domain.validator :as validator]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]))

(def schema [:map
             [:name string?]])

(deftest valid?
  (testing "should return a boolean when executing valid function"
    (is (boolean? (validator/valid? schema {:name "Diego"})))))

(deftest humanized-error
  (testing "should return nil when input data is valid"
    (is (nil? (validator/humanized-error schema {:name "Diego"}))))

  (testing "should return a map with errors when input data is invalid"
    (is (match? {:error map?}
                (validator/humanized-error schema {:age 20})))))
