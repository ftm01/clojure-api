;; Schema definitions for error response formats
;; Standardizes error message structures
(ns bz-assignment.schema.error)

;; Schema for error response (single error message)
(def error-response
  [:map [:error string?]])