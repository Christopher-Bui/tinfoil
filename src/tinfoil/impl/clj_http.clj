(ns tinfoil.impl.clj-http
  (:require [clj-http.client :as client]
            [tinfoil.protocols :as p]))

(defn- make-req-fn
  [req-fn]
  (fn [this url opts]
    (let [{:keys [headers body status]}
          (req-fn (p/request-url this url opts)
                  (merge {:headers (p/request-headers this url opts)
                          :body (p/request-body this url opts)}
                         opts))]
      {:headers (p/response-headers this headers)
       :body (p/response-body this body)
       :status (p/response-status this status)})))

(def +clj-http+
  {:get (make-req-fn client/get)
   :put (make-req-fn client/put)
   :post (make-req-fn client/post)
   :patch (make-req-fn client/patch)
   :delete (make-req-fn client/delete)
   :head (make-req-fn client/head)
   :options (make-req-fn client/options)})
