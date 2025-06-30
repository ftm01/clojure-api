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
