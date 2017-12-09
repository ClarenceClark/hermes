(ns hermes.route-fns.create-notif
  (:require [hermes.db.core :as qu]
            [ring.util.http-response :as resp]
            [clj-time.core :as t]
            [hermes.db.utils :as dbutils]
            [clojure.tools.logging :as log]))

(defn- get-tag
  "Gets the userid for each tag if given a name. Returns the the required
  format for link-tag-and-notif."
  [userid tag]
  (if (number? tag)
    (qu/get-tag-by-id {:userid userid
                       :easyid tag})
    (qu/get-tag-by-name {:userid userid
                         :name tag})))

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
        ntjoin (map #(vector userid notifid %) easyids)
        created-path (str "/notifications/" (:id notif))
        ret-notif (assoc notif :tags easyids)
        user-notif (dbutils/dbnotif->user ret-notif)]
    ; Associate notifs to tags
    (qu/link-tag-and-notif {:notif-tags ntjoin})
    (resp/created created-path
                  user-notif)))

(defn create-notif-resp
  "Checks if the insert request is valid (ie all tags exist) and
  creates a notif if it is."
  [userinfo title content usertags]
  (let [userid (:id userinfo)
        tags (map #(get-tag userid %) usertags)]
    (if (.contains tags nil)
      (resp/bad-request {:error "One or more of the tags don't exist"})
      (create-notif userid title content tags))))
