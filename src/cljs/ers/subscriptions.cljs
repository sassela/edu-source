(ns ers.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [re-frame.db]))

(register-sub :items/list-items (fn [db _] (reaction (:items/list-items @db))))