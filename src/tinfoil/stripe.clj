(ns tinfoil.stripe
  (:require [tinfoil.core :refer [defwrappedapi]]
            [tinfoil.protocols :as p]
            [tinfoil.impl.clj-http :refer [+clj-http+]]
            [tinfoil.impl.tinfoil :refer [+tinfoil+]]))

(def +stripe+
  {:request-url (fn [this url opts]
                  (str (:base-url this) url))
   :request-headers (fn [this url opts]
                      (merge {"Stripe-Version" (:version this)}
                             (:headers opts)))
   :request-opts (fn [this url opts]
                   (assoc opts :basic-auth [(:secret-key this) ""]))})

(defwrappedapi Stripe
  [base-url version public-key secret-key]
  +clj-http+
  (merge +tinfoil+ +stripe+))

(def +api+ (map->Stripe {:base-url "https://api.stripe.com/v1"
                         :version "2015-10-16"
                         :public-key ""
                         :secret-key ""}))

(defn balance
  []
  (p/get +api+ "/balance" {}))
