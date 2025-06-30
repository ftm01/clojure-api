(ns bz-assignment.controller.sanctioncontroller
  (:require [bz-assignment.validation.check_sanction :as val]))

(def sanction-lists
  [{:id 1 :name "Eu Sanctions"}
   {:id 2 :name "Israeli Ministry of Defence Sanction List"}])

(defn sanction-list-handler [_]
  {:status 200
   :body sanction-lists})

;(defn sanction-list-handler [_]
;  {:status 200
;   :body [{:id 1 :name "Eu Sanctions"}
;          {:id 2 :name "Israeli Ministry of Defence Sanction List"}]})

(def sanction-check-results
  [{:match_score 70
    :list [{:id 1 :name "Eu Sanctions"}]
    :record {:id "23dinXRmxTu4sehASYNAGE"
             :name "Kostenko Irina Anatolievna"
             :entity_type {:id "2" :type "Individual"}
             :country "UAE"
             :additional_info "Ordinance of 4 March 2022..."}}
   {:match_score 50
    :list [{:id 2 :name "Peppercat Legislators"}]
    :record {:id "Q1000800"
             :name "Kostenko Anatolievna"
             :entity_type {:id "2" :type "Individual"}
             :country "DE"
             :additional_info "N/A"}}])

(defn check-sanction-handler [{:keys [body-params]}]
  (let [errors (val/validate-check-request body-params)]
    (prn :validation-errors errors)
    (prn :types-of-keys (map type (keys errors)))
    (if (seq errors)
      {:status 400
       :body {:error "Validation failed"
              :details errors}}
      {:status 200
       :body {:status 200
              :results sanction-check-results}})))

;(defn check-sanction-handler [{:keys [body-params]}]
;  (let [errors (val/validate-check-request body-params)]
;    (prn :validation-errors errors)
;    (prn :types-of-keys (map type (keys errors)))
;    (if (seq errors)
;      {:status 400
;       :body {:error "Validation failed"
;              :details errors}}
;      {:status 200
;       :body {:status 200
;              :results
;              [{:match_score 70
;                :list [{:id 1 :name "Eu Sanctions"}]
;                :record {:id "23dinXRmxTu4sehASYNAGE"
;                         :name "Kostenko Irina Anatolievna"
;                         :entity_type {:id "2" :type "Individual"}
;                         :country "UAE"
;                         :additional_info "Ordinance of 4 March 2022..."}}
;               {:match_score 50
;                :list [{:id 2 :name "Peppercat Legislators"}]
;                :record {:id "Q1000800"
;                         :name "Kostenko Anatolievna"
;                         :entity_type {:id "2" :type "Individual"}
;                         :country "DE"
;                         :additional_info "N/A"}}]}})))
