ALTER TABLE tags
ADD CONSTRAINT tags_userid_name_key UNIQUE (userid, name);