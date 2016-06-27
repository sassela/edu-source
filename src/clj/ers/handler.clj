(ns ers.handler
  (:require [compojure.api.core :as compojure-api]
            [compojure.api.swagger :as swagger]
            [compojure.core :as compojure]
            [ring.swagger.ui :as ring]
            [schema.core :as schema])
  (:use ring.middleware.edn))


(def source "http://api.jorum.ac.uk/rest")


(def cors-headers
  {"Access-Control-Allow-Origin"  "*"
   "Access-Control-Allow-Headers" "Content-Type"
   "Access-Control-Allow-Methods" "GET,POST"})


;; wrap-cors fn credit: https://github.com/r0man/ring-cors/issues/1
(defn wrap-cors
  "Allow requests from all origins"
  [handler]
  (fn [request]
    (let [response (handler request)]
      (update-in response [:headers] merge cors-headers))))


(defn generate-response [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body    (pr-str data)})


(compojure-api/defapi api
  (ring/swagger-ui)
  (swagger/swagger-docs
    {:info {:title       "eduSource API"
            :description "API documentation for Educational Recommender System."}
     :tags [{:name "API" :description "API documentation for Educational Recommender System."}]}))


(def app
  (-> api wrap-cors wrap-edn-params))