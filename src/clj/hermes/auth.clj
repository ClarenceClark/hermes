(ns hermes.auth
  (:require [buddy.hashers :as hash]
            [buddy.auth.backends.httpbasic :as ht]
            [hermes.db.core :as qu]))

(defn- get-user-info
  "Get the user information in a map."
  [email]
  (let [user-info (qu/get-user-by-email {:email email})]
    (when-not (empty? user-info)
      user-info
      nil)))

(defn- basic-auth
  "Determine if the email corresponds with the password or the apikey. If
  either one matches, returns a map with :id and :email of the user."
  [request auth-data]
  (let [email (:email auth-data)
        secret (:password auth-data)
        user-info (get-user-info email)]
    (if (and user-info                                      ; user-info not nil
             ; Check both apikey and password
             (or (= secret (:apikey user-info))
                 (hash/check secret (:password user-info))))
      (select-keys user-info [:id :email])
      false)))

(def httpbasic-backend
  "Basic HTTP authentication backend for Buddy. Adds a map with the keys
  [:id :email] to the request bound to :identity."
  (ht/http-basic-backend
    {:authfn basic-auth}))