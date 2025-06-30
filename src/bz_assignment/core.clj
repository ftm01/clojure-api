(ns bz-assignment.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [bz-assignment.routes :refer [app]]))

(defn -main []
  (run-jetty app {:port 3000 :join? false}))