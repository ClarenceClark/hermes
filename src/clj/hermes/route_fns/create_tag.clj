(ns hermes.route-fns.create-tag
  (:require [ring.util.http-response :as resp]
            [hermes.db.core :as qu]
            [hermes.db.utils :as dbutil]
            [clojure.tools.logging :as log]))

(defn create-tag
  "Creates a tag. Assumes it doesn't already exist."
  [userid easyid name]
  (let [tag (qu/create-tag! {:userid userid
                             :easyid easyid
                             :name name})
        url (str "/tags/" (:easyid tag))]
    (resp/created url (dbutil/dbtag->user tag))))

(defn create-tag-with-id-resp
  "Creates a tag and generates its ring response"
  [userinfo easyid name]
  (let [userid (:id userinfo)
        nametag (qu/get-tag-by-name {:userid userid
                                     :name name})
        idtag (qu/get-tag-by-id {:userid userid
                                 :easyid easyid})
        tag-exists? (or nametag idtag)]
    (if tag-exists?
      (resp/conflict {:error "Name or id already exists."})
      (create-tag userid easyid name))))

(defn create-tag-resp
  "Creates a tag, automatically assigning a unique id"
  [userinfo name]
  (let [maxres (qu/get-max-easyid {:userid (:id userinfo)})
        maxid (or (:max maxres) 0)]
    (create-tag-with-id-resp userinfo (inc maxid) name)))