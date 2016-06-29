(ns ers.handlers
  (:require [ajax.core :refer [GET]]
            [re-frame.core :refer [register-handler dispatch]]
            [schema.core :as s :include-macros true]))


(def jorum-route "http://api.jorum.ac.uk")


(register-handler :print-db
  (fn [db [_]]
    (prn "DB: " db)
    db))


(register-handler :items/update
  (fn [db [_id items]]
    (assoc db :items/list-items items)))


(register-handler
  :items/get-all
  (fn [db]
    (GET
      (str jorum-route "/rest/items")
      {:response-format :json
       :keywords?       true
       :handler         (fn [response]
                          (dispatch [:items/update (:item response)]))
       :error-handler   #(println "ERROR: :items/get-all")})
    db))