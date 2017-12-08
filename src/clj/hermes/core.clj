(ns hermes.core
  (:require [hermes.handler :as handler]
            [luminus.repl-server :as repl]
            [luminus.http-server :as http]
            [luminus-migrations.core :as luminus-migrate]
            [hermes.config :refer [env]]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.tools.logging :as log]
            [mount.core :as mount]
            [migratus.core :as migratus])
  (:gen-class))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :parse-fn #(Integer/parseInt %)]])

(mount/defstate ^{:on-reload :noop}
                http-server
                :start
                (http/start
                  (-> env
                      (assoc :handler (handler/app))
                      (update :io-threads #(or % (* 2 (.availableProcessors (Runtime/getRuntime)))))
                      (update :port #(or (-> env :options :port) %))))
                :stop
                (http/stop http-server))

(mount/defstate ^{:on-reload :noop}
                repl-server
                :start
                (when-let [nrepl-port (env :nrepl-port)]
                  (repl/start {:port nrepl-port}))
                :stop
                (when repl-server
                  (repl/stop repl-server)))


(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (shutdown-agents))

(defn start-app [args]
  ; Start all components
  (doseq [component (-> args
                        (parse-opts cli-options)
                        mount/start-with-args
                        :started)]
    (log/info component "started"))

  ; Clean up before shutdown
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main [& args]
  (cond
    (some #{"init"} args)
    (do
      (mount/start #'hermes.config/env)
      (luminus-migrate/init (select-keys env [:database-url :init-script]))
      (System/exit 0))
    (some #{"migrate" "rollback"} args)
    (do
      (mount/start #'hermes.config/env)
      (luminus-migrate/migrate args (select-keys env [:database-url]))
      (System/exit 0))
    :else
    (start-app args))

  @(promise))
