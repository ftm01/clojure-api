;; Authentication controller for handling login requests
;; Processes token generation and credential validation
(ns bz-assignment.controller.authcontroller
  (:require [bz-assignment.auth :as auth]))

;; Handles POST /auth/token requests - validates credentials and returns JWT token
(defn token-handler [{:keys [parameters]}]
  (let [{:keys [applicationId secret]} (:body parameters)]
    (if (auth/valid-app? applicationId secret)
      {:status 200
       :body {:token (auth/generate-token applicationId)}}
      {:status 401
       :body {:error "Invalid credentials"}})))

