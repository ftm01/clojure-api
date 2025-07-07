(defproject bz-assignment "0.1.0-SNAPSHOT"
  :description "Clojure Microservice with JWT and API key authentication for Global Remit"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring "1.10.0"]
                 [ring/ring-mock "0.4.0"]
                 [metosin/reitit "0.7.0-alpha5"]
                 [metosin/reitit-ring "0.7.0-alpha5"]
                 [metosin/reitit-swagger "0.7.0-alpha5"]
                 [metosin/reitit-swagger-ui "0.7.0-alpha5"]
                 [metosin/malli "0.13.0"]
                 [compojure "1.7.0"]
                 [cheshire "5.11.0"]
                 [speclj "3.4.6"]
                 [buddy/buddy-sign "3.5.351"]
                 [environ "1.2.0"]
                 [ring-middleware-format "0.7.5"]]
  :plugins [[lein-environ "1.2.0"]
            [speclj "3.4.6"]]
  :main bz-assignment.core
  :aot [bz-assignment.core]
  :profiles
  {:dev {:env {:jwt-secret "my-super-secret-key"
               :api-key "my-api-key"
               :app-id "myApplicationId"
               :app-secret "mySecret"}}}
  :test-paths ["spec"])
