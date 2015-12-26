(ns tinfoil.protocols
  (:refer-clojure :exclude [get]))

(defprotocol Http
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
