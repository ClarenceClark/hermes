(ns hermes.routes.tags
  (:require [schema.core :as s]
            [hermes.schemas :as model]
            [compojure.api.sweet :refer :all]
            [buddy.auth :as auth]
            [hermes.auth]
            [ring.util.http-response :as resp]
            [hermes.middleware :as mw]
            [hermes.route-fns.create-tag :refer :all]
            [hermes.route-fns.get-tags :refer :all]
            [hermes.route-fns.get-notifs :refer :all]
            [hermes.route-fns.modify-tags :refer :all]))

(def tag-routes
  (context "/tags" []
    :tags ["api"]
    :auth-rules auth/authenticated?
    :current-user userinfo
    :header-params [authorization :- String]

    (GET "/" []
      :return [model/Tag]
      :summary "Get all tags"
      (get-all-tags-resp userinfo))
    (POST "/" []
      :body-params [name :- s/Str]
      :return model/Tag
      :summary "Create new tag"
      (create-tag-resp userinfo name))

    (context "/:id" []
      :path-params [id :- s/Int]

      (GET "/" []
        :return model/Tag
        :summary "Get the tag with the provided id"
        (get-tag-by-id-resp userinfo id))
      (POST "/" []
        :body-params [name :- s/Str]
        :return model/Tag
        :summary "Create a tag with the specified id"
        (create-tag-with-id-resp userinfo id name))
      (PUT "/" []
        :body-params [name :- s/Str]
        :return model/Tag
        :summary "Update tag of the provided id"
        (update-tag-resp userinfo id name))
      (DELETE "/" []
        :summary "Delete tag with the provided id"
        (delete-tag-resp userinfo id))

      (GET "/notifications" []
        :return [model/Notif]
        :summary "Get all notifications with the specified tag"
        (get-notifs-for-tag userinfo id)))))