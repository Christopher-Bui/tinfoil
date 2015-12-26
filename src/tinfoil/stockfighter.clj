(ns tinfoil.stockfighter
  (:require [tinfoil.core :refer [wrap defwrappedapi]]
            [tinfoil.protocols :as p]
            [tinfoil.impl.clj-http :refer [+clj-http+]]
            [tinfoil.impl.tinfoil :refer [+tinfoil+]]))

;; Stockfighter API helpers
(def ^:private ->kw-str (comp str keyword))
(defn build-url
  "Replaces values with colons in front of them with the equivalent
  value from a map"
  [url params]
  (reduce-kv (fn [url k v]
               (.replaceAll url (->kw-str k) v))
             url
             params))

(def +stockfighter+
  {:request-url (fn [this url opts] (str (:base-url this) (build-url url (:url-params opts))))
   :request-headers (fn [this url opts] {(:auth-header this) (:api-key this)})})

;; Without macro
;; (defrecord StockFighterAPI
;;     [base-url api-key auth-header])

;; (wrap StockFighterAPI +clj-http+ (merge +tinfoil+ +stockfighter+))

;; With macro
(defwrappedapi StockFighterAPI
  [base-url api-key auth-header]
  +clj-http+
  (merge +tinfoil+ +stockfighter+))

(def +sf+ (map->StockFighterAPI {:base-url "https://api.stockfighter.io/ob/api"
                                 :api-key ""
                                 :auth-header "X-Stockfighter-Authorization"}))

(defn heartbeat
  [api]
  (p/get api "/heartbeat" {}))

(defn venue-heartbeat
  [api venue]
  (p/get api "/venues/:venue/heartbeat" {:url-params {:venue venue}}))

(defn venue-stocks
  [api venue]
  (p/get api "/venues/:venue/stocks" {:url-params {:venue venue}}))

(defn all-order-statuses
  [api venue account]
  (p/get api "venues/:venue/accounts/:account/orders" {:url-params {:venue venue
                                                                     :account account}}))

(defn cancel-order
  [api venue stock order-id]
  (p/delete api "/venues/:venue/stocks/:stock/order/:order" {:url-params {:venue venue
                                                                           :stock stock
                                                                           :order order-id}}))

(comment
  (heartbeat +sf+)
  (venue-heartbeat +sf+ "TESTEX")
  (venue-stocks +sf+ "TESTEX")
  ;; 404s with test acct
  (all-order-statuses +sf+ "TESTEX" "EXB123456")
  (cancel-order +sf+ "TESTEX" "FOOBAR" "12")
  )
