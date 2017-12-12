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

(defn get-user-resp
  "Get the apikey of a user given his password (we assume its their password,
   hitting this endpoint using their apikey would be kinda useless. I guess
   they can be trying to validate their apikey? Who knows. I just write apis.
   I don't to psychological analysis of devs using my apis."
  [userinfo]
  (let [user (qu/get-user-by-id {:id (:id user)})]
    (if (not-empty user)
      (respond/ok (select-keys user [:email :apikey]))
      (respond/internal-server-error
        {:error "I don't know what is doing on!!!! Its late and night
                 and I'm too tired to think about it. Please tell me if
                 you see this message. You shouldn't. In theory."}))))