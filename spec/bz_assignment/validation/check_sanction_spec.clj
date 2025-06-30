(ns bz-assignment.validation.check_sanction_spec
  (:require [speclj.core :refer :all]
            [bz-assignment.validation.check_sanction :refer [string-number? validate-check-request]]))

(describe "string-number?"

          (it "returns true for numeric strings"
              (should (string-number? "123"))
              (should (string-number? "007")))

          (it "returns false for non-numeric strings"
              (should-not (string-number? "abc"))
              (should-not (string-number? "12a3"))
              (should-not (string-number? nil))
              (should-not (string-number? 123))))

(describe "validate-check-request"

          (it "returns no errors for valid input"
              (let [valid {:full_name "Alice"
                           :threshold "80"
                           :gender "Female"
                           :birth_date "1980-01-01"
                           :country_code "US"
                           :address "Main St"
                           :entity_types [[{:id "1" :type "Company"}]]
                           :sanction_lists [{:id 1 :name "OFAC"}]
                           :free_text "check"}]
                (should-be-nil (validate-check-request valid))))

          (it "returns errors for missing required fields"
              (let [invalid {:threshold "not-a-number"
                             :gender "Unknown"
                             :entity_types [["bad-data"]]
                             :sanction_lists [{:id "a" :name 123}]
                             :free_text 100}]
                (let [errors (validate-check-request invalid)]
                  (should (map? errors))
                  (should (contains? errors :full_name))
                  (should (contains? errors :threshold))
                  (should (contains? errors :gender))
                  (should (contains? errors :entity_types))
                  (should (contains? errors :sanction_lists))
                  (should (contains? errors :free_text))))))

