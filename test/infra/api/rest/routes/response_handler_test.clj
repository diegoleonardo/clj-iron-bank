(ns infra.api.rest.routes.response-handler-test
  (:require [infra.api.rest.routes.response-handler :as response]
            [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test :refer [match?]]))

(deftest response
  (testing "should return status to ok"
    (is (match? {:status 200
                 :body   {:name "john"}}
                (response/response {:success true
                                    :data    {:name "john"}}))))

  (testing "should return status to bad-request"
    (is (match? {:status 400
                 :body   {:error ["invalid account"]}}
                (response/response {:success false
                                    :data    {:error ["invalid account"]}}))))

  (testing "should return status to not-found"
    (is (match? {:status 404
                 :body {}}
                (response/not-found)))))
