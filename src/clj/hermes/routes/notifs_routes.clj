(ns hermes.routes.notifs-routes
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [hermes.routes.api :as a]))

(def notifs-routes
  (context "/notifications" []
    (GET "/" []
        :query-params [after :- s/Int]
        :return [a/Notif]
        :summary "Get all notifications with `id > from`"
        (ok "hello"))
    (GET "/:id" []
        :path-params [id :- s/Int]
        :return a/Notif
        :summary "Get the notif with the provided id"
        (ok {}))
    (POST "/" []
         :body-params [title :- s/Str,
                       content :- s/Str,
                       tags :- [s/Int]]
         :return a/Notif
         :summary "Create new notification"
         (do (ok {})))))