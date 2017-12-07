(ns hermes.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [hermes.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[hermes started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[hermes has shut down successfully]=-"))
   :middleware wrap-dev})
