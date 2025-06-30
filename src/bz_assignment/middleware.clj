(ns bz-assignment.middleware
  (:require [bz-assignment.auth :as auth]
            [cheshire.core :as json]))

(defn unauthorized [msg]
  {:status 401
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string {:error msg})})

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