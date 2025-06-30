(ns bz-assignment.auth_spec
  (:require [speclj.core :refer :all]
            [bz-assignment.controller.authcontroller :refer [token-handler]]
            [bz-assignment.auth :as auth]))

(describe "token-handler"

          (it "returns 200 and token when credentials are valid"
              (with-redefs [auth/valid-app? (fn [app secret] true)
                            auth/generate-token (fn [app] "mock-token")]
                (let [request {:parameters {:body {:applicationId "myApp" :secret "mySecret"}}}
                      response (token-handler request)]

                  (should= 200 (:status response))
                  (should= {:token "mock-token"} (:body response)))))

          (it "returns 401 when credentials are invalid"
              (with-redefs [auth/valid-app? (fn [app secret] false)]
                (let [request {:parameters {:body {:applicationId "myApp" :secret "wrong"}}}
                      response (token-handler request)]

                  (should= 401 (:status response))
                  (should= {:error "Invalid credentials"} (:body response))))))
