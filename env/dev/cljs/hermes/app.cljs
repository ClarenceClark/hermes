(ns ^:figwheel-no-load hermes.app
  (:require [hermes.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
