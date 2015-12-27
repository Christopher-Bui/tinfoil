(ns tinfoil.impl.tinfoil
  "Default implementations for tinfoil fn's. The Http protocol's
  functions call these.")

(def +tinfoil+
  {:request-url (fn [this url opts] url)
   :request-headers (fn [this url opts] (:headers opts))
   :request-body (fn [this url opts] (:body opts))
   :request-opts (fn [this url opts] opts)
   :response-headers (fn [this headers] headers)
   :response-body (fn [this body] body)
   :response-status (fn [this status] status)
   :response (fn [this response] response)})
