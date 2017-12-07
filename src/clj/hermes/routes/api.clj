(ns hermes.routes.api
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [hermes.routes.notifs-routes :as notifs-routes]
            [hermes.routes.tags-routes :as tags-routes]
            [buddy.auth :as auth]))

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

(s/defschema Notif
  {:id s/Int
   :title s/Str
   :content s/Str
   :tags [s/Int]
   :time s/Int})

(s/defschema Tag
  {:id s/Int
   :name s/Str})

(s/defschema NotifIn
  {:title s/Str
   :content s/Str
   :tags [s/Int]})

(defapi service-routes
  {:swagger {:ui "/api-docs"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Hermes REST Api"
                           :description ""}}}}

  (GET "/authenticated" []
    :auth-rules auth/authenticated?
    :current-user user
    (ok (:user user)))

  (context "/v1" []
    :tags ["hermes"]
    notifs-routes/notifs-routes
    tags-routes/tags-routes))