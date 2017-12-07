-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, email, passhash, apikey)
VALUES (:id, :email, :passhash, :apikey);

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users SET
  email = :email,
  passhash = :passhash,
  apikey = :apikey
WHERE id = :id;

-- :name get-user-by-id :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id;

-- :name get-user-by-email :? :1
-- :doc retrieves a user record given the email
SELECT * FROM users
WHERE email = :email;

-- :name get-apikey-by-email :? :1
SELECT apikey FROM users
WHERE email = :email;

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id;