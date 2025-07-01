;; Authentication module for JWT token generation and validation
;; Handles application credentials and token verification
(ns bz-assignment.auth
  (:require [environ.core :refer [env]]
             [buddy.sign.jwt :as jwt]))

;; Environment variables for authentication
(def jwt-secret (env :jwt-secret))
(def api-key (env :api-key))
(def app-id (env :app-id))
(def app-secret (env :app-secret))

;; Validates application credentials against environment variables
(defn valid-app? [applicationId secret]
  (and (= applicationId app-id)
       (= secret app-secret)))

;; Generates a JWT token for authenticated applications
(defn generate-token [applicationId]
  (jwt/sign {:sub 1234567890
             :admin true
             :name "John Doe"
             :iat (quot (System/currentTimeMillis) 1000)}
             ;:exp (+ (quot (System/currentTimeMillis) 1000) 3600)}
            jwt-secret))

;; Verifies and decodes a JWT token, returns nil if invalid
(defn verify-token [token]
  (try
    (jwt/unsign token jwt-secret)
    (catch Exception _ nil)))