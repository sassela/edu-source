(ns ers.handlers
  (:require [ajax.core :refer [GET]]
            [ers.schema :refer [db-schema]]
            [ers.util :refer [profile]]
            [re-frame.core :refer [after dispatch register-handler]]
            [schema.core :as s :include-macros true]))


(def jorum-route "http://api.jorum.ac.uk")


;; -- MIDDLEWARE --

(defn check-and-throw
  "throw an exception if db doesn't match the schema."
  [a-schema db]
  (if-let [problems  (s/check a-schema db)]
    (throw (js/Error. (str "schema check failed: " problems)))))


(def check-schema-mw (after (partial check-and-throw db-schema)))


(def middleware [check-schema-mw])


; -- EVENT HANDLERS --

(defn standard-request
  [handler-name]
  {:response-format :json
   :keywords?       true
   :error-handler   #(println (str "ERROR: " handler-name))})


(register-handler
  :print-db
  (fn [db [_]]
    (prn "DB: " db)
    db))


(register-handler
  :user/update-profile
  middleware
  (fn [db [_id item]]
    (let [item-profile (profile (:metadata item))]
      (update-in db [:user :profile] #(into (if (set? %) % (set %)) item-profile)))))


(register-handler
  :items/update
  middleware
  (fn [db [_id items]]
    (assoc db :items/list-items items)))


(register-handler
  :items/get-all
  middleware
  (fn [db [_]]
    (GET
      (str jorum-route "/rest/items?expand=metadata")
      (assoc (standard-request :items/get-all)
        :handler (fn [response]
                   (dispatch [:items/update (:item response)]))))
    db))


(register-handler
  :items/search
  middleware
  (fn [db [_id keywords]]
    (GET
      (str jorum-route "/rest/items/search?q=" keywords "&expand=metadata")
      (assoc (standard-request :items/search)
        :handler (fn [response]
                   (dispatch [:items/update (:item response)]))))
    db))


(register-handler
  :input/update-search
  middleware
  (fn [db [_id items]]
    (assoc db :input/search items)))


(register-handler
  :page/set
  middleware
  (fn [db [_id items]]
    (assoc db :page items)))


(register-handler
  :init
  middleware
  (fn [db [_]]
    (assoc db :items/list-items []
              :input/search ""
              :page :home)))