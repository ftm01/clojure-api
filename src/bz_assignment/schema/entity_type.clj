;; Schema definitions for entity type data structures
;; Defines entity type objects and response formats
(ns bz-assignment.schema.entity_type)

;; Schema for individual entity type object (id and type)
(def entity-type
  [:map
   [:id string?]
   [:type string?]])

;; Schema for entity type list response (array of entity types)
(def entity-type-response
  [:vector entity-type])