(ns tinfoil.protos.http
  (:refer-clojure :exclude [get]))

(defprotocol Http
  (get [_ url opts])
  (put [_ url opts])
  (post [_ url opts])
  (patch [_ url opts])
  (delete [_ url opts])
  (head [_ url opts])
  (options [_ url opts]))
