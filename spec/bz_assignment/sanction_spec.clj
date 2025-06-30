(ns bz-assignment.sanction_spec
  (:require [speclj.core :refer :all]
            [bz-assignment.controller.sanctioncontroller :as sanctioncontroller]
            [cheshire.core :as json]))

(describe "sanction-list-handler"

          (it "returns 200 and the expected list of sanctions"
              (let [response (sanctioncontroller/sanction-list-handler nil)
                    expected [{:id 1 :name "Eu Sanctions"}
                              {:id 2 :name "Israeli Ministry of Defence Sanction List"}]]

                (should= 200 (:status response))
                (should= expected (:body response)))))

(describe "check-sanction-handler"

          (it "returns 200 and result list for valid request"
              (let [valid-request {:body-params
                                   {:full_name "Ahmad Beit"
                                    :threshold "50"
                                    :gender "Male"
                                    :birth_date "1974-04-04"
                                    :country_code "UAE"
                                    :address "123 Street"
                                    :entity_types [[{:id "1" :type "Company"}]]
                                    :sanction_lists [{:id 1 :name "Eu Sanctions"}]
                                    :free_text "Sample Info"}}

                    response (sanctioncontroller/check-sanction-handler valid-request)]

                (should= 200 (:status response))
                (should= 200 (get-in response [:body :status]))
                (should (vector? (get-in response [:body :results])))))

          (it "returns 400 and validation errors for invalid request"
              (let [invalid-request {:body-params
                                     {:threshold "fifty"
                                      :entity_types [["invalid"]]
                                      :sanction_lists [{:id "a" :name 123}]
                                      :free_text 999}}

                    response (sanctioncontroller/check-sanction-handler invalid-request)]

                (should= 400 (:status response))
                (should= "Validation failed" (get-in response [:body :error]))
                (should (map? (get-in response [:body :details])))
                (should (contains? (get-in response [:body :details]) :full_name))
                (should (contains? (get-in response [:body :details]) :threshold)))))

;(describe "check-sanction-handler"
;
;          (it "returns 400 with validation errors for invalid input"
;              (let [invalid-body "{}"
;                    req (-> (mock/request :post "/check-sanction")
;                            (assoc :body (java.io.ByteArrayInputStream. (.getBytes invalid-body)))
;                            (assoc-in [:headers "content-type"] "application/json"))
;                    res (check-sanction-handler req)
;                    body (json/parse-string (:body res) true)]
;                (should= 400 (:status res))
;                (should (contains? body :errors))
;                (should (> (count (:errors body)) 0))))
;
;          (it "returns 200 and mock sanction results for valid input"
;              (let [valid-body (json/generate-string
;                                 {:full_name "Ahmad Beit"
;                                  :threshold "50"
;                                  :gender "Male"
;                                  :birth_date "1974-04-04"
;                                  :country_code "UAE"
;                                  :address ""
;                                  :entity_types [[{:id "1" :type "Company"}
;                                                  {:id "2" :type "Individual"}]]
;                                  :sanction_lists [{:id 1 :name "Eu Sanctions"}
;                                                   {:id 2 :name "Israeli Ministry of Defence Sanction List"}]
;                                  :free_text "КОСТЕНКО Ирина Анатольевна"})
;                    req (-> (mock/request :post "/check-sanction")
;                            (assoc :body (java.io.ByteArrayInputStream. (.getBytes valid-body)))
;                            (assoc-in [:headers "content-type"] "application/json"))
;                    res (check-sanction-handler req)
;                    body (json/parse-string (:body res) true)]
;
;                (should= 200 (:status res))
;                (should (contains? body :results))
;                (should= 2 (count (:results body)))
;                (should (every? #(contains? % :match_score) (:results body))))))
