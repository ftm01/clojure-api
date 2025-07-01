;; Authentication middleware for protecting API endpoints
;; Validates API keys and JWT tokens for secure access
(ns bz-assignment.middleware
  (:require [bz-assignment.auth :as auth]
            [cheshire.core :as json]))

;; Creates a standardized unauthorized response with JSON error message
(defn unauthorized [msg]
  {:status 401
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string {:error msg})})

;; Middleware that validates API key and JWT token for protected routes
(defn wrap-authentication [handler]
  (fn [req]
    (let [auth-header (get-in req [:headers "authorization"])
          api-key-header (get-in req [:headers "x-api-key"])
          token (when (and auth-header (.startsWith auth-header "Bearer "))
                  (subs auth-header 7))]
      (cond
        (not= api-key-header auth/api-key)
        (unauthorized "Invalid or Missing API Key")

        (not token)
        (unauthorized "Missing Bearer token")

        (not (auth/verify-token token))
        (unauthorized "Invalid token")

        :else
        (handler req)))))