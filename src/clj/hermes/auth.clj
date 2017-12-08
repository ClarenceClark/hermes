(ns hermes.auth
  (:require [buddy.hashers :as hash]
            [buddy.auth.backends.httpbasic :as ht]
            [hermes.db.core :as qu]))

(defn get-user-info
  "Get the user information in a map with keys [:email :passhash]"
  [email]
  (let [user-info (qu/get-user-by-email {:email email})]
    (when-not (nil? user-info)
      {:email (:email user-info)
       :passhash (:passhash user-info)
       :apikey (:apikey user-info)})))

(defn basic-auth
  "Determine if the email corresponds with the password or the apikey. If
  either one matches, the email is added to the request with keyword
  :identity."
  [request auth-data]
  (let [email (:email auth-data)
        secret (:password auth-data)
        user-info (get-user-info email)]
    (if (and user-info ; user-info not nil
             ; Check both apikey and password
             (or (= secret (:apikey user-info))
                 (hash/check secret (:password user-info))))
      (:email user-info)
      false)))

(def httpbasic-backend
  "Basic HTTP authentication backend for Buddy"
  (ht/http-basic-backend
    {:authfn basic-auth}))