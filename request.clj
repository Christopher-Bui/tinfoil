(ns tinfoil.protocols.request
  (:require [clj-http.client :as client])
  (:refer-clojure :exclude [get]))

(defprotocol Request
  (get [_ url opts])
  (put [_ url opts])
  (post [_ url opts])
  (patch [_ url opts])
  (delete [_ url opts])
  (head [_ url opts])
  (options [_ url opts]))

(defprotocol Tinfoil
  ;; Request
  (request-url [this url opts])
  (request-headers [this url opts])
  (request-body [this url opts])
  ;; Response
  (response-headers [this headers])
  (response-body [this body])
  (response-status [this status]))

(comment
  (do
    (def ^:private ->kw-str (comp str keyword))
    (defn build-url
      "Replaces values with colons in front of them with the equivalent
  value from a map"
      [url params]
      (reduce-kv (fn [url k v]
                   (.replaceAll url (->kw-str k) v))
                 url
                 params))
    
    (defrecord Response
        [headers body status])
    
    (def +clj-http+
      {:get (fn [this url opts]
              (let [{:keys [headers body status]}
                    (client/get (request-url this url opts)
                                (merge {:headers (request-headers this url opts)
                                        :body (request-body this url opts)}
                                       opts))]
                (map->Response {:headers (response-headers this headers)
                                :body (response-body this body)
                                :status (response-status this status)})))})

    (def +tinfoil+
      {:request-url (fn [this url opts] (str (:base-url this) (build-url url (:url-params opts))))
       :request-headers (fn [this url opts] {})
       :request-body (fn [this url opts] "")
       :response-headers (fn [this headers] headers)
       :response-body (fn [this body] body)
       :response-status (fn [this status] status)})

    (defrecord StockFighterAPI
        [base-url api-key auth-header])

    (defn wrap
      [type request-impl tinfoil-impl]
      (extend type
        Request
        request-impl
        Tinfoil
        tinfoil-impl))

    (wrap StockFighterAPI +clj-http+ +tinfoil+)

    (def +sf+ (map->StockFighterAPI {:base-url "https://api.stockfighter.io/ob/api"
                                     :api-key ""
                                     :auth-header "X-Stockfighter-Authorization"}))
    
    (defn heartbeat
      []
      (get +sf+ "/heartbeat" {}))
    
    (defn venue-heartbeat
      [venue]
      (get +sf+ "/venues/:venue/heartbeat" {:url-params {:venue venue}}))

    (defn venue-stocks
      [venue]
      (get +sf+ "/venues/:venue/stocks" {:url-params {:venue venue}}))
    )
  )
