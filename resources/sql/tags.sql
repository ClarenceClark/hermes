-- :name create-tag! :<! :1
-- :doc create a new tag
INSERT INTO tags
(owner, userid, name)
VALUES (:ownerid, :userid, :name)
RETURNING *;

-- :name update-tag! :<! :1
-- :doc updates an existing tag record
UPDATE tags SET
  owner = :ownerid,
  userid = :userid,
  name = :name
WHERE id = :id
RETURNING *;

-- :name get-tags-for-notif :? :*
-- :doc retrieve all tags for a notification
SELECT tags.id, tags.name FROM tags
INNER JOIN notif_tag_join
ON tags.id = notif_tag_join.tagid
WHERE notifid = :notifid;