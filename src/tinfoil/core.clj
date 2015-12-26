(ns tinfoil.core
  (:require [tinfoil.protocols :as p]))

(defn wrap
  [type http-impl tinfoil-impl]
  (extend type
    p/Http
    http-impl
    p/Tinfoil
    tinfoil-impl))
