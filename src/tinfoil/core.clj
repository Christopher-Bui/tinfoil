(ns tinfoil.core
  (:require [tinfoil.protos
             [http :as http]
             [tinfoil :as tin]]))

(defn wrap
  [type http-impl tinfoil-impl]
  (extend type
    http/Http
    http-impl
    tin/Tinfoil
    tinfoil-impl))

(defmacro defwrappedapi
  [name fields http-impl tinfoil-impl]
  (assert (vector? fields) "Fields must be a vector.")
  `(do
     (defrecord ~name
         ~fields)
     (wrap ~name ~http-impl ~tinfoil-impl)))
