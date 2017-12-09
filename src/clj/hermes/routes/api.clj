(ns hermes.routes.api
  (:require [compojure.api.sweet :refer :all]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :as auth]
            [hermes.routes.notifs :as notif-routes]
            [hermes.routes.tags :as tag-routes]))

(defn access-error [_ _]
  (unauthorized {:error "unauthorized"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))

(defapi service-routes
  {:swagger {:ui "/api-docs"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Hermes REST Api"
                           :description ""}}}}

  (context "/v1" []
    notif-routes/notif-routes
    tag-routes/tag-routes))