(ns ers.handlers
  (:require [ajax.core :refer [GET]]
            [ers.util :as util]
            [re-frame.core :refer [register-handler dispatch]]
            [schema.core :as s :include-macros true]))


(def jorum-route "http://api.jorum.ac.uk")

(defn standard-request
  [handler-name]
  {:response-format :json
   :keywords?       true
   :error-handler   #(println (str "ERROR: " handler-name))})


(register-handler :print-db
  (fn [db [_]]
    (prn "DB: " db)
    db))


(register-handler :items/update
  (fn [db [_id items]]
    (assoc db :items/list-items items)))


(register-handler
  :items/get-all
  (fn [db [_]]
    (GET
      (str jorum-route "/rest/items")
      (assoc (standard-request :items/get-all)
        :handler (fn [response]
                   (dispatch [:items/update (:item response)]))))
    db))


(register-handler
  :items/search
  (fn [db [_id keywords]]
    (GET
      (str jorum-route "/rest/items/search?keyword=" (first keywords)
        #_(apply str (map #(str "&keyword=" %) (rest keyword)))
        #_(str "/rest/items/search?" #_(util/compose-query params)))
      {:response-format :json
       :keywords?       true
       :handler         (fn [response]
                          (dispatch [:items/update (:item response)]))
       :error-handler   #(println "ERROR: :items/search")})
    db))


(register-handler :input/update-search
  (fn [db [_id items]]
    (assoc db :input/search items)))

(register-handler :init
  (fn [db [_]]
    (assoc db :input/search ""
              :items/list-items [])))