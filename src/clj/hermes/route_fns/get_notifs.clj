(ns hermes.route-fns.get-notifs
  (:require [ring.util.http-response :as resp]
            [hermes.db.core :as qu]))

(defn get-notif-resp
  "Creates the response to a GET notif for id"
  [userinfo notifid]
  (let [notif (qu/get-notification-by-id {:id notifid})
        notif-user (:userid notif)
        user-owns-notif (= (:id userinfo) notif-user)]
    (if user-owns-notif
      (resp/ok notif)
      (resp/forbidden {:error "User doesn't own this notification."}))))

(defn get-all-notifs-resp
  [userinfo afterid tagids]
  (let [notifs (qu/get-notifications-for-tags-after-id
                 {:tagids tagids
                  :afterid afterid
                  :userid (:id userinfo)})]
    (resp/ok notifs)))

