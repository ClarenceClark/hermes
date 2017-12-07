(ns hermes.routes.tags-routes
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [hermes.routes.api :as a]))

(def tags-routes
  (context "/tags" []
    (GET "/" []
         :query-params []
         :return [a/Tag]
         :summary "Get all tags"
         (ok "tags"))
    (POST "/" []
          :body-params [name :- s/Str]
          :return a/Tag
          :summary "Create new tag"
          (ok ""))
    (context "/:id" []
      :path-params [id :- s/Int]
      (GET "/" []
           :return a/Tag
           :summary "Get the tag with the provided id"
           (ok "tags"))
      (PUT "/" []
           :body-params [name :- s/Str]
           :return a/Tag
           :summary "Update tag of the provided id"
           (ok ""))
      (DELETE "/" []
              :return a/Tag
              :summary "Delete tag with the provided id"
              (ok "")))))