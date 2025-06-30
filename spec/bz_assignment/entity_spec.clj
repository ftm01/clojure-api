(ns bz-assignment.entity_spec
  (:require [speclj.core :refer :all]
            [bz-assignment.controller.entitycontroller :refer [entity-type-handler]]))

(describe "entity-type-handler"

          (it "returns 200 and the expected list of entity types"
              (let [response (entity-type-handler nil)
                    expected-body [{:id "1" :type "Company"}
                                   {:id "2" :type "Individual"}]]

                (should= 200 (:status response))
                (should= expected-body (:body response)))))
