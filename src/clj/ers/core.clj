(ns ers.core
  (:require [ers.handler :as handler]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(defn -main [& args]
  (println "starting app")
  (jetty/run-jetty #'handler/app {:port 8080}))
