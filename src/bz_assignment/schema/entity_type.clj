(ns bz-assignment.schema.entity_type)

(def entity-type
  [:map
   [:id string?]
   [:type string?]])

(def entity-type-response
  [:vector entity-type])