(ns hermes.routes.user
  (:require [compojure.api.sweet :refer :all]
            [hermes.route-fns.user :as usr]
            [schema.core :as s]
            [buddy.auth :as auth]))

(def user-routes
  (context "/user" []
    :tags ["user"]

    (POST "/" []
      :body-params [email :- s/Str
                    password :- s/Str]
      :return {:email s/Str
               :apikey s/Str}
      (usr/create-user-resp email password))

    (GET  "/:id" []
      :path-params []
      :return {:email s/Str
               :apikey s/Str}
      :auth-rules auth/authenticated?
      :current-user user-info
      :header-params [authorization :- s/Str]
      :description
      "This endpoint returns your email and apikey back to you if you are
       authenticated. Use this to validate login information in apps. Throws
       a 403 if you are not. I think. I really should be sleeping, but this
       thing is due tomorrow."
      (usr/get-user-resp userinfo))))