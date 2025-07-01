;; Schema definitions for authentication requests and responses
;; Defines data structures for login and token operations
(ns bz-assignment.schema.auth_token)

;; Schema for login request body (applicationId and secret)
(def auth-request
  [:map {:closed true}
   [:applicationId string?]
   [:secret string?]])

;; Schema for successful token response (JWT token)
(def token-response
  [:map {:closed true}
   [:token string?]])