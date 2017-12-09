(ns hermes.routes.tags
  (:require [schema.core :as s]
    [hermes.schemas :as model]
    [compojure.api.sweet :refer :all]
    [buddy.auth :as auth]))

(def tag-routes
  (context "/tags" []
    :tags ["api"]
    :auth-rules auth/authenticated?
    :current-user userinfo

    (GET "/" []
      :return [model/Tag]
      :summary "Get all tags"
      (ok "tags"))
    (POST "/" []
      :body-params [name :- s/Str]
      :return model/Tag
      :summary "Create new tag"
      (ok ""))

    (context "/:id" []
      :path-params [id :- s/Int]

      (GET "/" []
           :return model/Tag
           :summary "Get the tag with the provided id"
           (ok "tags"))
      (PUT "/" []
           :body-params [name :- s/Str]
           :return model/Tag
           :summary "Update tag of the provided id"
           (ok ""))
      (DELETE "/" []
              :return model/Tag
              :summary "Delete tag with the provided id"
              (ok "")))))