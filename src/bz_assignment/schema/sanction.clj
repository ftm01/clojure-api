;(ns bz-assignment.schema.sanction
;  (:require [bz-assignment.schema.entity_type :as e]))
;
;(def sanction-list
;  [:map
;   [:id int?]
;   [:name string?]])
;
;(def check-sanction-request
;  [:map
;   [:full_name string?]
;   [:threshold [:and string? #(re-matches #"\d+" %)]]
;   [:gender {:optional true} [:enum "Male" "Female"]]
;   [:birth_date {:optional true} string?]
;   [:country_code {:optional true} string?]
;   [:address {:optional true} string?]
;   [:entity_types [:vector [:vector e/entity-type]]]
;   [:sanction_lists [:vector sanction-list]]
;   [:free_text string?]])
;
;(def check-sanction-response
;  [:map
;   [:status int?]
;   [:results [:vector [:map
;                       [:match_score int?]
;                       [:list sanction-list]
;                       [:record [:map
;                                 [:id string?]
;                                 [:name string?]
;                                 [:entity_type e/entity-type]
;                                 [:country string?]
;                                 [:additional_info string?]]]]]]])
;
(ns bz-assignment.schema.sanction
  (:require [bz-assignment.schema.entity_type :as e]))

;(def sanction-list
;  [:map
;   [:id int?]
;   [:name string?]])
;
(def sanction-list
  [:vector
   [:map
    [:id int?]
    [:name string?]]])

;(def check-sanction-request
;  [:map
;   [:full_name string?]
;   [:threshold [:and string? #(re-matches #"\d+" %)]]
;   [:gender {:optional true} [:enum "Male" "Female"]]
;   [:birth_date {:optional true} string?]
;   [:country_code {:optional true} string?]
;   [:address {:optional true} string?]
;   [:entity_types [:vector [:vector [:ref ::e/entity-type]]]]
;   [:sanction_lists [:vector sanction-list]]
;   [:free_text string?]])

;(def check-sanction-request
;  [:map
;   [:full_name string?]
   ;[:threshold [:and string? #(re-matches #"\d+" %)]] ; numeric string
   ;[:gender {:optional true} [:enum "Male" "Female"]]
   ;[:birth_date {:optional true} string?]
   ;[:country_code {:optional true} string?]
   ;[:address {:optional true} string?]
   ;[:entity_types [:vector [:vector e/entity-type]]]
   ;[:sanction_lists sanction-list]
   ;[:free_text string?]])

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
        [:country string?]
        [:additional_info string?]]]]]]])

;(def check-sanction-response
;  [:map
;   [:status int?]
;   [:results [:vector [:map
;                       [:match_score int?]
;                       [:list sanction-list]
;                       [:record [:map
;                                 [:id string?]
;                                 [:name string?]
;                                 [:entity_type e/entity-type-response]
;                                 [:country string?]
;                                 [:additional_info string?]]]]]]])
