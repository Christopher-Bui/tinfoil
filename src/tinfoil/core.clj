(ns tinfoil.core
  (:require [tinfoil.protocols :as p]))

(defn wrap
  [type http-impl tinfoil-impl]
  (extend type
    p/Http
    http-impl
    p/Tinfoil
    tinfoil-impl))

(defmacro defwrappedapi
  [name fields http-impl tinfoil-impl]
  (assert (vector? fields) "Fields must be a vector.")
  `(do
     (defrecord ~name
         ~fields)
     (wrap ~name ~http-impl ~tinfoil-impl)))
