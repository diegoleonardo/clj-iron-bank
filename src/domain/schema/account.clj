(ns domain.schema.account
  (:require [malli.util :as mu])
  (:refer-clojure :exclude [update]))

(def minimum-age 18)

(def person [:map {:closed true}
             [:first-name :string]
             [:last-name :string]
             [:age
              [:and
               int?
               [:>= minimum-age]]]])

(def address [:map
              [:street :string]
              [:number :int]
              [:zipcode :int]
              [:country :string]
              [:state :string]
              [:city :string]])

(def identification-document
  [:multi {:dispatch :code}
   [:us [:map [:id :int]]]
   [:br [:map [:cpf :string]]]])

(def user (-> [:map
               [:document identification-document]]
              (mu/assoc :username :string)
              (mu/assoc :password :string)
              (mu/assoc :email :string)))

(def account (mu/merge user
                       [:map
                        [:number :int]
                        [:code :int]
                        [:type [:enum "NP" "LP"]]
                        [:balance :int]]))

(def create [:map
             [:person person]
             [:account (-> account
                           (mu/dissoc :number)
                           (mu/dissoc :code)
                           (mu/dissoc :balance))]])

(def update (-> [:map
                 [:reference-id :any]]
                (mu/merge create)))
