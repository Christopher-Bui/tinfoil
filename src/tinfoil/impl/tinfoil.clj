(ns tinfoil.impl.tinfoil)

(def +tinfoil+
  {:request-url (fn [this url opts] url)
   :request-headers (fn [this url opts] (:headers opts))
   :request-body (fn [this url opts] (:body opts))
   :response-headers (fn [this headers] headers)
   :response-body (fn [this body] body)
   :response-status (fn [this status] status)})