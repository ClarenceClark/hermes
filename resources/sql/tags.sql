-- :name create-tag! :<! :1
-- :doc create a new tag
INSERT INTO tags
(userid, easyid, name)
VALUES (:userid, :easyid, :name)
RETURNING *;

-- :name update-tag! :<! :1
-- :doc updates an existing tag record
UPDATE tags SET
  name = :name
WHERE userid = :userid AND easyid = :easyid
RETURNING *;

-- :name get-tag-by-id :? :1
-- :doc retrieve a tag given a userid and an ownerid
SELECT * FROM tags
WHERE userid = :userid AND easyid = :easyid;

-- :name get-tag-by-name :? :1
-- :doc retrieve a tag given a userid and a name
SELECT * FROM tags
WHERE userid = :userid AND name = :name;

-- :name get-all-tags-for-user :? :*
-- :doc get all tags owned by owner
SELECT * FROM tags
WHERE userid = :userid;

-- :name get-max-easyid :? :1
-- :doc gets the highest tag easyid for an owner
SELECT max(easyid) FROM tags
WHERE userid = :userid;

-- :name get-tags-for-notif :? :*
-- :doc retrieve all tags for a notification
SELECT * FROM tags
INNER JOIN notif_tags
ON tags.userid = notif_tags.userid AND tags.easyid = notif_tags.tagid
WHERE notif_tags.notifid = :notifid;