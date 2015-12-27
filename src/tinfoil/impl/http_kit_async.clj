(ns tinfoil.impl.http-kit-async
  (:require [clojure.core.async :as a]
            [org.httpkit.client :as client]
            [tinfoil.protos.tinfoil :as tin]))

(defn- make-req-fn
  [req-fn]
  (fn [this url opts]
    (let [response-chan (a/chan 1)]
      (req-fn (tin/request-url this url opts)
              (merge {:headers (tin/request-headers this url opts)
                      :body (tin/request-body this url opts)}
                     (tin/request-opts this url opts))
              (fn [{:keys [status headers body error]}]
                (a/put! response-chan 
                        (tin/response this
                                      {:headers (tin/response-headers this headers)
                                       :body (tin/response-body this body)
                                       :status (tin/response-status this status)}))))
      response-chan)))

(def +http-kit-async+
  {:get (make-req-fn client/get)
   :put (make-req-fn client/put)
   :post (make-req-fn client/post)
   :patch (make-req-fn client/patch)
   :delete (make-req-fn client/delete)
   :head (make-req-fn client/head)
   :options (make-req-fn client/options)})
