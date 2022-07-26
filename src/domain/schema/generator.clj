(ns domain.schema.generator
  (:require [malli.generator :as gen]))

(defn gen [schema]
  (gen/generate schema))
