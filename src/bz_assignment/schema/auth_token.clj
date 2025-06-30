(ns bz-assignment.schema.auth_token)

(def auth-request
  [:map {:closed true}
   [:applicationId string?]
   [:secret string?]])

(def token-response
  [:map {:closed true}
   [:token string?]])