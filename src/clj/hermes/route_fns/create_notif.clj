(ns hermes.route-fns.create-notif
  (:require [hermes.db.core :as qu]
            [ring.util.http-response :as resp]
            [clj-time.core :as t])
  (:import [org.postgresql.util PSQLException]))

(defn- get-tag
  "Gets the userid for each tag if given a name. Returns the the required
  format for link-tag-and-notif."
  [userid tag]
  (if (number? tag)
    (qu/get-tag-by-id {:userid userid
                       :easyid tag})
    (qu/get-tag-by-name {:userid userid
                         :name tag})))

(defn- get-tag-join-map [userid notifid easyid]
  {:userid userid
   :notifid notifid
   :tagid easyid})

(defn- create-notif
  "Create a new notification and links it with its tags. Assumes that all
  tags referenced by the notification exist."
  [userid title content tags]
  (let [notif-data {:userid userid
                    :title title
                    :content content
                    :sendtime (t/now)}
        notif (qu/create-notification! notif-data)
        notifid (:id notif)
        easyids (map :easyid tags)
        ntjoin (->> easyids
                    (map #(vector userid notifid %))
                    (vec))
        created-path (str "/notifications/" (:id notif))
        ret-notif (assoc notif :tags easyids)]
    ; Associate notifs to tags
    (qu/link-tag-and-notif {:notif-tags ntjoin})
    (resp/created created-path
                  ret-notif)))

(defn create-notif-resp
  "Checks if the insert request is valid (ie all tags exist) and
  creates a notif if it is."
  [userinfo title content usertags]
  (let [tags (map get-tag usertags)]
    (if (.contains tags nil)
      (resp/bad-request {:error "One of the tags don't exist"})
      (create-notif (:id userinfo) title content tags))))
