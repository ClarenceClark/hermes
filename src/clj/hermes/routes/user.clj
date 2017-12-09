(ns hermes.routes.user
  (:require [compojure.api.sweet :refer :all]
            [hermes.route-fns.user :as usr]
            [schema.core :as s]))

(def user-routes
  (context "/v1/user" []
    :tags ["user"]

    (POST "/" []
      :body-params [email :- s/Str
                    password :- s/Str]
      :return [email :- s/Str
               apikey :- s/Str]
      (usr/create-user-resp email password))))