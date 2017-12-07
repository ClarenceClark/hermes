(ns hermes.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [hermes.core-test]))

(doo-tests 'hermes.core-test)

