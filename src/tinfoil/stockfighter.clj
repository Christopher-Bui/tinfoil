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
  []
  (p/get +sf+ "/heartbeat" {}))

(defn venue-heartbeat
  [venue]
  (p/get +sf+ "/venues/:venue/heartbeat" {:url-params {:venue venue}}))

(defn venue-stocks
  [venue]
  (p/get +sf+ "/venues/:venue/stocks" {:url-params {:venue venue}}))

(comment
  (heartbeat)
  (venue-heartbeat "TESTEX")
  (venue-stocks "TESTEX")
  )
