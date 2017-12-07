(ns hermes.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
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

    (context "/notifications" []
      (GET "/" []
        :query-params [after :- s/Int]
        :return [Notif]
        :summary "Get all notifications with `id > from`"
        (ok "hello"))
      (GET "/:id" []
        :path-params [id :- s/Int]
        :return Notif
        :summary "Get the notif with the provided id"
        (ok {}))
      (POST "/" []
        :body-params [title :- s/Str,
                      content :- s/Str,
                      tags :- [s/Int]]
        :return Notif
        :summary "Create new notification"
        (do (ok {}))))

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