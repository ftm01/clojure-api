;; Schema definitions for sanction-related data structures
;; Defines request/response formats for sanction operations
(ns bz-assignment.schema.sanction
  (:require [bz-assignment.schema.entity_type :as e]))

;; Schema for sanction list response (array of sanction list objects)
(def sanction-list
  [:vector
   [:map
    [:id int?]
    [:name string?]]])

;; Schema for sanction check request body (all required and optional fields)
(def check-sanction-request
  [:map
   [:full_name string?]
   [:threshold string?]
   [:gender {:optional true} [:enum "Male" "Female"]]
   [:birth_date {:optional true} string?]
   [:country_code {:optional true} string?]
   [:address {:optional true} string?]
   [:entity_types [:vector [:vector e/entity-type]]]
   [:sanction_lists [:vector [:map [:id int?] [:name string?]]]]
   [:free_text string?]])

;; Schema for sanction check response (status and results array)
(def check-sanction-response
  [:map
   [:status int?]
   [:results
    [:vector
     [:map
      [:match_score int?]
      [:list sanction-list]
      [:record
       [:map
        [:id string?]
        [:name string?]
        [:entity_type e/entity-type]
        [:address string?]
        [:date string?]
        [:birth_date string?]
        [:country string?]
        [:additional_info string?]]]]]]])