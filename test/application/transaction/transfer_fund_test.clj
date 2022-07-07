(ns application.transaction.transfer-fund-test
  (:require [application.transaction.transfer-fund :as transfer-fund]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]
            [mock.utils :as mock-utils]))

(def source-id "1234")

(def destiny-id "4321")

(def source-account {:balance 100.0})

(def destiny-account {:balance 100.0})

(def init-state {source-id [source-account]
                 destiny-id [destiny-account]})

(def repository (mock-utils/repository {:type  :transaction
                                        :state init-state}))

(def transfer-example {:source  {:reference-id source-id}
                       :destiny {:reference-id destiny-id}
                       :amount  50.0})

(defn- invalidate-data [m k v]
  (update m k (fn [_] [v])))

(defn assert-error [repository param]
  (is (match? {:error vector?}
              (transfer-fund/execute! repository param))))

(deftest execute!
  (testing "should transfer the fund between accounts"
    (is (match? {:source  {:id string? :balance 50.0}
                 :destiny {:id string? :balance 150.0}}
                (transfer-fund/execute! repository transfer-example))))

  (testing "should not transfer the fund when source balance is insuficient"
    (let [repo (mock-utils/repository {:type  :transaction
                                       :state (invalidate-data init-state source-id {:balance 10.0})})]
      (is (match? {:error vector?}
                  (transfer-fund/execute! repo transfer-example)))))

  (testing "should not transfer when"
    (testing "amount to transfer is invalid"
      (let [invalid-amount (assoc transfer-example :amount -1.0)]
        (assert-error repository invalid-amount)))

    (testing "source balance amount is invalid"
      (let [repo (mock-utils/repository {:type  :transaction
                                         :state (invalidate-data init-state source-id {:balance nil})})]
        (assert-error repo transfer-example)))

    (testing "destiny balance amount is invalid"
      (let [repo (mock-utils/repository {:type  :transaction
                                         :state (invalidate-data init-state destiny-id {:balance nil})})]
        (assert-error repo transfer-example)))))
