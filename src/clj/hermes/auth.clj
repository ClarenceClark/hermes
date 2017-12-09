(ns hermes.auth
  (:require [buddy.hashers :as hash]
            [buddy.auth.backends.httpbasic :as ht]
            [compojure.api.sweet :refer :all]
            [compojure.api.meta :refer [restructure-param]]
            [buddy.auth.accessrules :refer [restrict]]
            [ring.util.http-response :as resp]
            [hermes.db.core :as qu]
            [clojure.tools.logging :as log]))

(defn access-error [_ _]
  (resp/unauthorized {:error "unauthorized"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))

(defn- get-user-info
  "Get the user information in a map."
  [email]
  (let [user-info (qu/get-user-by-email {:email email})]
    user-info))

(defn- basic-auth
  "Determine if the email corresponds with the password or the apikey. If
  either one matches, returns a map with :id and :email of the user."
  [request auth-data]
  (let [email (:username auth-data)
        secret (:password auth-data)
        user-info (get-user-info email)]
    (if (and user-info
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