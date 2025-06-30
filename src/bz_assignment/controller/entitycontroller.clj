(ns bz-assignment.controller.entitycontroller)

(def entity-types
  [{:id "1" :type "Company"}
   {:id "2" :type "Individual"}])

(defn entity-type-handler [_]
  {:status 200
   :body entity-types})

;(defn entity-type-handler [_]
;  {:status 200
;   :body [{:id "1" :type "Company"}
;          {:id "2" :type "Individual"}]})