-- :name create-notification! :<! :1
-- :doc create a new notification
INSERT INTO notifications (userid, title, content, sendtime)
VALUES (:userid, :title, :content, :sendtime)
RETURNING *;

-- :name get-notification-by-id :? :1
-- :doc retrieve a notification given its unique id
SELECT * FROM notifications
WHERE id = :id;

-- :name get-all-notifications-for-tag :? :*
-- :doc retrieve all notifications given a tagid
SELECT * FROM notifications
INNER JOIN notif_tags ON notifications.id = notif_tags.notifid
WHERE tagid = :easyid AND notifications.userid = :userid;

-- :name get-all-notifications-after-id :? :*
-- :doc retrieve all notifications where id > provided id
SELECT * FROM notifications
WHERE id > :id AND userid = :userid;

-- :name get-notifications-for-tags-after-id :? :*
-- :doc retrieve all notifications tagged with tagid
SELECT * FROM notifications
INNER JOIN notif_tags ON notifications.id = notif_tags.notifid
WHERE tagid IN (:v*:tagids)
AND notifications.userid = :userid
AND notifications.id > :afterid;

-- :name link-tag-and-notif :?
-- :doc insert a new record in the many-to-many notif-to-tag join table
INSERT INTO notif_tags (userid, notifid, tagid)
VALUES :tuple*:notif-tags;