(ns bz-assignment.routes
  (:require
    [reitit.ring :as ring]
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.coercion.malli]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.exception :as exception]
    [muuntaja.core :as m]

    [bz-assignment.middleware :refer [wrap-authentication]]

    [bz-assignment.schema.auth_token :as auth-s]
    [bz-assignment.schema.entity_type :as entity-s]
    [bz-assignment.schema.sanction :as sanction-s]
    [bz-assignment.schema.error :as error-s]

    [bz-assignment.controller.authcontroller :as auth]
    [bz-assignment.controller.entitycontroller :as entity]
    [bz-assignment.controller.sanctioncontroller :as sanction]))

(def app
  (ring/ring-handler
    (ring/router
      [
       ["/swagger.json"
        {:get {:no-doc true
               :swagger {:info {:title "Clojure API" :description "Global Remit Assignment API"}
                         :tags [{:name "Authentication API" :description "Endpoints related to Authentication"}
                                {:name "Entity API" :description "Endpoints related to Entity resources"}
                                {:name "Sanction API" :description "Endpoints related to Sanction resources"}]}
               :handler (swagger/create-swagger-handler)}}]
       ["/auth/token"
        {:post {:summary "Authenticate and return JWT"
                :tags ["Authentication API"]
                :parameters {:body auth-s/auth-request}
                :responses {200 {:body auth-s/token-response}
                            401 {:body error-s/error-response}}
                :handler auth/token-handler}}]
       ["/api"
        {:middleware [wrap-authentication]}
        ["/entity-type"
         {:get {:summary "List entity types"
                :tags ["Entity API"]
                :responses {200 {:body entity-s/entity-type-response}}
                :handler entity/entity-type-handler}}]

        ["/sanction-list"
         {:get {:summary "List sanctions"
                :tags ["Sanction API"]
                :responses {200 {:body sanction-s/sanction-list}}
                :handler sanction/sanction-list-handler}}]

        ["/check-sanction"
         {:post {:summary "Check subject against sanctions"
                 :tags ["Sanction API"]
                 :description ["Endpoints related to Sanction resources"]
                 :parameters {}
                 :responses {200 {:body sanction-s/check-sanction-response}
                             400 {:body [:map
                                         [:error string?]
                                         [:details [:map-of any? string?]]]}}
                 :handler sanction/check-sanction-handler}}]]]

      {:data {:coercion reitit.coercion.malli/coercion
              :muuntaja m/instance
              :middleware [swagger/swagger-feature
                           muuntaja/format-negotiate-middleware
                           muuntaja/format-response-middleware
                           muuntaja/format-request-middleware
                           coercion/coerce-response-middleware
                           coercion/coerce-request-middleware
                           exception/exception-middleware]
              }})

    (ring/routes
      (swagger-ui/create-swagger-ui-handler {:path "/api-docs"
                                             :url "/swagger.json"})
      (swagger/create-swagger-handler))))

