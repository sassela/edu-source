(ns ers.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [ers.core-test]))

(doo-tests 'ers.core-test)
