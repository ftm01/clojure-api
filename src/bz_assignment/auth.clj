(ns bz-assignment.auth
  (:require [environ.core :refer [env]]
             [buddy.sign.jwt :as jwt]))

(def jwt-secret (env :jwt-secret))
(def api-key (env :api-key))
(def app-id (env :app-id))
(def app-secret (env :app-secret))

(defn valid-app? [applicationId secret]
  (and (= applicationId app-id)
       (= secret app-secret)))

(defn generate-token [applicationId]
  (jwt/sign {:sub 1234567890
             :admin true
             :name "John Doe"
             :iat (quot (System/currentTimeMillis) 1000)}
             ;:exp (+ (quot (System/currentTimeMillis) 1000) 3600)}
            jwt-secret))

(defn verify-token [token]
  (try
    (jwt/unsign token jwt-secret)
    (catch Exception _ nil)))