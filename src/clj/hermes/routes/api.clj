(ns hermes.routes.api
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :as auth]
            [hermes.route-fns.user :as userfns]
            [hermes.route-fns.get-notifs :as get-notif-fns]
            [hermes.route-fns.create-notif :as create-notif-fns]))

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

  (GET "/authenticated" []
    :auth-rules auth/authenticated?
    :current-user user
    (ok (:email user)))

  (context "/v1" []
    :tags ["hermes"]
    :auth-rules auth/authenticated?
    :current-user user

    (context "/tags" []
      (GET "/" []
        :query-params []
        :return [Tag]
        :summary "Get all tags"
        (ok "tags"))
      (POST "/" []
        :body-params [name :- s/Str]
        :return Tag
        :summary "Create new tag"
        (ok ""))
      (context "/:id" []
        :path-params [id :- s/Int]
        (GET "/" []
          :return Tag
          :summary "Get the tag with the provided id"
          (ok "tags"))
        (PUT "/" []
          :body-params [name :- s/Str]
          :return Tag
          :summary "Update tag of the provided id"
          (ok ""))
        (DELETE "/" []
          :return Tag
          :summary "Delete tag with the provided id"
          (ok ""))))))