(ns user
  (:require [luminus-migrations.core :as migrations]
            [hermes.config :refer [env]]
            [mount.core :as mount]
            hermes.core))

(defn start []
  (mount/start-without #'hermes.core/repl-server))

(defn stop []
  (mount/stop-except #'hermes.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))


