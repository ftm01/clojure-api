;; Main entry point for the BZ Assignment application
;; Starts the web server on port 3000
(ns bz-assignment.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [bz-assignment.routes :refer [app]]))

;; Application entry point - starts the Jetty web server
(defn -main []
  (run-jetty app {:port 3000 :join? false}))