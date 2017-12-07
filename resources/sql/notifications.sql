-- :name create-notification! :<! :1
-- :doc create a new notification
INSERT INTO notifications
(owner, title, content, sendtime)
VALUES (:owner, :title, :content, :sendtime)
RETURNING *;

-- :name get-notification-by-id :? :1
-- :doc retrieve a notification given its unique id
SELECT * FROM notifications
WHERE id = :id;

-- :name get-notifications-for-tag :? :*
-- :doc retrieve all notifications given a tagid
SELECT * FROM notifications
INNER JOIN notif_tag_join
ON notifications.id = notif_tag_join.notifid
WHERE tagid = :tagid;