(set-env!
 :dependencies '[[adzerk/boot-cljs "2.1.4" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                 [buddy "2.0.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [clj-time "0.14.2"]
                 [cljs-ajax "0.7.3"]
                 [compojure "1.6.0"]
                 [conman "0.7.4"]
                 [cprop "0.1.11"]
                 [crisptrutski/boot-cljs-test "0.3.4" :scope "test"]
                 [deraen/boot-sass "0.3.1" :scope "test"]
                 [funcool/struct "1.1.0"]
                 [luminus-http-kit "0.1.5"]
                 [luminus-migrations "0.4.3"]
                 [luminus-nrepl "0.1.4"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "1.0.1"]
                 [metosin/compojure-api "1.1.11"]
                 [metosin/muuntaja "0.4.1"]
                 [metosin/ring-http-response "0.9.0"]
                 [mount "0.1.11"]
                 [mysql/mysql-connector-java "6.0.5"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946" :scope "provided"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.clojure/tools.reader "1.1.1"]
                 [org.webjars.bower/tether "1.4.0"]
                 [org.webjars/bootstrap "4.0.0-beta.2"]
                 [org.webjars/font-awesome "4.7.0"]
                 [ragtime "0.7.2"]
                 [re-frame "0.10.2"]
                 [reagent "0.7.0"]
                 [reagent-utils "0.2.1"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-defaults "0.3.1"]
                 [secretary "1.2.3"]
                 [selmer "1.11.3"]

                 ; Generate lein file
                 [onetom/boot-lein-generate "0.1.3" :scope "test"]]

 :source-paths #{"src/cljs" "src/cljc" "src/clj"}
 :resource-paths #{"resources"})

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl]])

(require '[deraen.boot-sass :refer [sass]])

(deftask dev
  "Enables configuration for a development setup."
  []
  (set-env!
   :source-paths #(conj % "env/dev/clj" "src/cljs" "src/cljc" "env/dev/cljs")
   :resource-paths #(conj % "env/dev/resources")
   :dependencies #(concat % '[[prone "1.1.4"]
                              [ring/ring-mock "0.3.0"]
                              [ring/ring-devel "1.6.1"]
                              [pjstadig/humane-test-output "0.8.2"]
                              [binaryage/devtools "0.9.7"]
                              [com.cemerick/piggieback "0.2.2"]
                              [crisptrutski/boot-cljs-test "0.3.2-SNAPSHOT" :scope "test"]
                              [doo "0.1.8"]
                              [figwheel-sidecar "0.5.14"]
                              [org.clojure/clojurescript cljs-version :scope "test"]
                              [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                              [pandeiro/boot-http "0.7.6" :scope "test"]
                              [powerlaces/boot-figreload "0.1.1-SNAPSHOT" :scope "test"]
                              [weasel "0.7.0" :scope "test"]]))
  (System/setProperty "database-url"
                      "mysql://localhost:3306/hermes_dev?user=hermes")
  (task-options! repl {:init-ns 'user})
  (require 'pjstadig.humane-test-output)
  (let [pja (resolve 'pjstadig.humane-test-output/activate!)]
    (pja))
  identity)

(deftask testing
  "Enables configuration for testing."
  []
  (dev)
  (set-env! :resource-paths #(conj % "env/test/resources"))
  (merge-env! :source-paths ["src/cljc" "src/cljs" "test/cljs"])
  identity)

(deftask prod
  "Enables configuration for production building."
  []
  (merge-env! :source-paths #{"env/prod/clj" "env/prod/cljs"}
              :resource-paths #{"env/prod/resources"})
  identity)

(deftask start-server
  "Runs the project without building class files.

  This does not pause execution. Combine with a wait task or use the \"run\"
  task."
  []
  (require 'hermes.core)
  (let [start-app (resolve 'hermes.core/start-app)]
    (with-pass-thru _
      (start-app nil))))

(deftask run
  "Starts the server and causes it to wait."
  []
  (comp
   (start-server)
   (wait)))

(require '[clojure.java.io :as io])
(require '[crisptrutski.boot-cljs-test :refer [test-cljs]])
(deftask figwheel
  "Runs figwheel and enables reloading."
  []
  (dev)
  (require '[powerlaces.boot-figreload :refer [reload]])
  (let [reload (resolve 'powerlaces.boot-figreload/reload)]
    (comp
     (start-server)
     (watch)
     (cljs-repl)
     (reload :client-opts {:debug true})
     (speak)
     (cljs))))

(deftask run-cljs-tests
  "Runs the doo tests for ClojureScript."
  []
  (comp
   (testing)
   (test-cljs :cljs-opts {:output-to "target/test.js", :main "hermes.doo-runner", :optimizations :whitespace, :pretty-print true})))

(deftask uberjar
  "Builds an uberjar of this project that can be run with java -jar"
  []
  (comp
   (prod)
   (aot :namespace #{'hermes.core})
   (cljs :optimizations :advanced)
   (uber)
   (jar :file "hermes.jar" :main 'hermes.core)
   (sift :include #{#"hermes.jar"})
   (target)))

(require 'boot.lein)
(deftask generate-lein []
         (boot.lein/generate))