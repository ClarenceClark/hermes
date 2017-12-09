(ns hermes.routes.api
  (:require [compojure.api.sweet :refer :all]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :as auth]
            [hermes.routes.notifs :as notif-routes]
            [hermes.routes.tags :as tag-routes]))

(defapi service-routes
  {:swagger {:ui "/api-docs"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Hermes REST Api"
                           :description ""}}}}

  (context "/v1" []
    notif-routes/notif-routes
    tag-routes/tag-routes))