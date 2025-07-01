;; Entity controller for managing entity type operations
;; Provides entity type data and handling
(ns bz-assignment.controller.entitycontroller)

;; Predefined entity types available in the system
(def entity-types
  [{:id "1" :type "Company"}
   {:id "2" :type "Individual"}])

;; Handles GET /api/entity-type requests - returns list of available entity types
(defn entity-type-handler [_]
  {:status 200
   :body entity-types})