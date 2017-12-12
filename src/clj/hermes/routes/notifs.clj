(ns hermes.routes.notifs
  (:require [schema.core :as s]
            [hermes.schemas :as model]
            [hermes.route-fns.get-notifs :refer :all]
            [hermes.route-fns.create-notif :refer :all]
            [compojure.api.sweet :refer :all]
            [buddy.auth :as auth]
            [hermes.auth]
            [hermes.middleware :as mw]))

(def notif-routes
  (context "/notifications" []
    :tags ["api"]
    :auth-rules auth/authenticated?
    :current-user userinfo
    :header-params [authorization :- s/Str]

    (GET "/" []
      :query-params [{after :- s/Int 0}
                     {tags :- [s/Int] []}]
      :return [model/Notif]
      :summary "Get all notifications where id is after the specified value"
      :description
      "Gets all notifications after a certain id. Doesn't matter if the
      user owns the specified id, since ids that he does own after that will
      be returned, not including the one they he specified. If no tags are
      specified, everything is returned."
      (get-all-notifs-resp userinfo after tags))
    (GET "/:id" []
      :path-params [id :- s/Int]
      :return model/Notif
      :summary "Get the notif with the provided id"
      :description
      "Returns the notification with the specified id. Throws 403 if they are
      not the owners of that resource."
      (get-notif-resp userinfo id))
    (POST "/" []
      :body-params [title :- s/Str,
                    content :- s/Str,
                    tags :- [s/Int]]
      :return model/Notif
      :summary "Create new notification"
      (create-notif-resp userinfo title content tags))))