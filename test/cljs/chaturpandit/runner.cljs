(ns chaturpandit.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [chaturpandit.core-test]))

(doo-tests 'chaturpandit.core-test)
