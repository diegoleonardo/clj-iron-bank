(ns domain.schema.account
  (:require [malli.util :as mu]))

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
                        [:type [:enum "FP" "LP"]]
                        [:balance :int]]))

(def creating [:map
               [:person person]
               [:account account]])
