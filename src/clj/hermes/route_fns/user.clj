(ns hermes.route-fns.user
  (:require [buddy.hashers :as pass]
            [buddy.core.hash :as hash]
            [buddy.core.nonce :as nonce]
            [buddy.core.codecs :as codecs]
            [hermes.db.core :as qu]
            [ring.util.http-response :as respond]))

(def passhash-options
  {:alg :bcrypt+sha512
   :iterations 12})

(defn- create-new-user
  "Creates a new user record given an email and plaintext password."
  [email password]
  (let [passhash (pass/derive password passhash-options)
        apikey (-> (nonce/random-bytes 12)
                   (hash/sha512)
                   (codecs/bytes->hex))
        user (qu/create-user! {:email email
                               :passhash passhash
                               :apikey apikey})]
    (respond/created (str "/notifications/" (:id user))
                     (select-keys user [:email :apikey]))))

(defn create-user-resp
  "Get the response to a user create request."
  [email password]
  (let [user (qu/get-user-by-email {:email email})]
    (if (not-empty user)
      (respond/conflict {:error "Email already registered"})
      (create-new-user email password))))