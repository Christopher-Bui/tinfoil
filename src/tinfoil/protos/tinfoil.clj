(ns tinfoil.protos.tinfoil)

(defprotocol Tinfoil
  ;; Request
  (request-url [this url opts])
  (request-headers [this url opts])
  (request-body [this url opts])
  (request-opts [this url opts])
  ;; Response
  (response-headers [this headers])
  (response-body [this body])
  (response-status [this status])
  (response [this response]))
