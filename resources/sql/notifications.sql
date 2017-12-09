-- :name create-notification! :<! :1
-- :doc create a new notification
INSERT INTO notifications (userid, title, content, sendtime)
VALUES (:userid, :title, :content, :sendtime)
RETURNING *;

-- :name get-notifications-after-id :? :*
-- :doc retrieve all notifications where id > provided id
SELECT * FROM notifications
WHERE id > :id;

-- :name get-notification-by-id :? :1
-- :doc retrieve a notification given its unique id
SELECT * FROM notifications
WHERE id = :id;

-- :name get-notifications-for-tag :? :*
-- :doc retrieve all notifications given a tagid
SELECT * FROM notifications
INNER JOIN notif_tags
ON notifications.id = notif_tags.notifid
WHERE tagid = :tagid;

-- :name get-notifications-for-tags-after-id :? :*
-- :doc retrieve all notifications tagged with tagid
SELECT * FROM notifications
INNER JOIN notif_tags ON notifications.id = notif_tags.notifid
WHERE tagid IN (:v*:tagids)
AND notifications.id > :afterid;

-- :name link-tag-and-notif :? :n
-- :doc insert a new record in the many-to-many notif-to-tag join table
INSERT INTO notif_tags (userid, notifid, tagid)
VALUES :tuples*:notif_tags;