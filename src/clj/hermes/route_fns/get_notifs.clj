(ns hermes.route-fns.get-notifs
  (:require [ring.util.http-response :as resp]
            [hermes.db.core :as qu]
            [hermes.db.utils :as dbutil]))

(defn get-notif-resp
  "Creates the response to a GET notif for id"
  [userinfo notifid]
  (let [notif (qu/get-notification-by-id {:id notifid})
        notif-user (:userid notif)
        user-owns-notif (= (:id userinfo) notif-user)]
    (if user-owns-notif
      (resp/ok (dbutil/dbnotif->user notif))
      (resp/forbidden {:error "User doesn't own this notification."}))))

(defn get-all-notifs-resp
  [userinfo afterid tagids]
  (let [notifs (if (empty? tagids)
                 (qu/get-notifications-for-tags-after-id
                   {:tagids tagids
                    :afterid afterid
                    :userid (:id userinfo)})
                 (qu/get-all-notifications-after-id
                   {:id afterid
                    :userid (:id userinfo)}))]
    (resp/ok (dbutil/dbnotiflist->user notifs))))

(defn get-notifs-for-tag
  [userinfo tag-easyid]
  (let [userid (:id userinfo)
        distinct-easyids (distinct tag-easyid)
        tag (dbutil/get-db-tag userid distinct-easyids nil)]
    (if (empty? tag)
      (resp/not-found {:error "Tag with id not found"})
      (-> (qu/get-all-notifications-for-tag
            {:userid userid
             :easyid tag-easyid})
          (dbutil/dbnotiflist->user)
          (resp/ok)))))