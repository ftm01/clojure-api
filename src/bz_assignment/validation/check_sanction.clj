;(ns bz-assignment.validation.check_sanction
;  (:require [clojure.string :as str]))
;
;(defn missing? [m k]
;  (let [v (get m k)]
;    (or (nil? v)
;        (and (string? v) (str/blank? v)))))
;
;(defn string-number? [s]
;  (try
;    (when (string? s)
;      (Double/parseDouble s)
;      true)
;    (catch Exception _ false)))
;
;(defn valid-entity-types? [value]
;  (and (vector? value)
;       (every? vector? value)
;       (every? #(every? (fn [param1]
;                          (and (map? param1)
;                               (contains? param1 :id)
;                               (contains? param1 :type))) %) value)))
;
;(defn valid-sanction-lists? [value]
;  (and (vector? value)
;       (every? #(and (map? %)
;                     (contains? % :id)
;                     (contains? % :name)) value)))
;
;(defn validate-check-request [body]
;  (println body)
;  (let [errors
;        (cond-> []
;
;                (missing? body :full_name)
;                (conj "Missing or empty `full_name`")
;
;                (missing? body :threshold)
;                (conj "Missing `threshold`")
;                (and (some? (:threshold body))
;                     (not (string-number? (:threshold body))))
;                (conj "`threshold` must be a numeric string")
;
;                (and (contains? body :gender)
;                     (not (#{"Male" "Female"} (:gender body))))
;                (conj "`gender` must be 'Male' or 'Female'")
;
;                (and (contains? body :birth_date)
;                     (not (string? (:birth_date body))))
;                (conj "`birth_date` must be a string")
;
;                (and (contains? body :country_code)
;                     (not (string? (:country_code body))))
;                (conj "`country_code` must be a string")
;
;                (and (contains? body :address)
;                     (not (string? (:address body))))
;                (conj "`address` must be a string")
;
;                (missing? body :entity_types)
;                (conj "Missing `entity_types`")
;                (and (some? (:entity_types body))
;                     (not (valid-entity-types? (:entity_types body))))
;                (conj "`entity_types` must be an array of arrays of objects with `id` and `type`")
;
;                (missing? body :sanction_lists)
;                (conj "Missing `sanction_lists`")
;                (and (some? (:sanction_lists body))
;                     (not (valid-sanction-lists? (:sanction_lists body))))
;                (conj "`sanction_lists` must be an array of objects with `id` and `name`")
;
;                (missing? body :free_text)
;                (conj "Missing or empty `free_text`"))]
;    errors))

(ns bz-assignment.validation.check_sanction)

(defn string-number? [s]
  (and (string? s) (re-matches #"\d+" s)))

(defn validate-check-request [body]
  (let [errors
        (cond-> {}

                (not (string? (:full_name body)))
                (assoc :full_name "Full name is required and must be a string.")

                (not (string-number? (:threshold body)))
                (assoc :threshold "Threshold is required and must be a string representing a number.")

                (and (:gender body) (not (#{"Male" "Female"} (:gender body))))
                (assoc :gender "Gender must be 'Male' or 'Female'.")

                (and (:birth_date body) (not (string? (:birth_date body))))
                (assoc :birth_date "Birth date must be a string.")

                (and (:country_code body) (not (string? (:country_code body))))
                (assoc :country_code "Country code must be a string.")

                (and (:address body) (not (string? (:address body))))
                (assoc :address "Address must be a string.")

                (or (not (vector? (:entity_types body)))
                    (empty? (:entity_types body))
                    (some #(not (vector? %)) (:entity_types body))
                    (some #(some (fn [et]
                                   (or (not (map? et))
                                       (not (string? (:id et)))
                                       (not (string? (:type et)))))
                                 %)
                          (:entity_types body)))
                (assoc :entity_types "Entity types must be an array of arrays of objects with 'id' and 'type' as strings.")

                (or (not (vector? (:sanction_lists body)))
                    (empty? (:sanction_lists body))
                    (some (fn [s]
                            (or (not (map? s))
                                (not (int? (:id s)))
                                (not (string? (:name s)))))
                          (:sanction_lists body)))
                (assoc :sanction_lists "Sanction lists must be an array of objects with integer 'id' and string 'name'.")

                (not (string? (:free_text body)))
                (assoc :free_text "Free text is required and must be a string."))]
    (when (seq errors) errors)))

