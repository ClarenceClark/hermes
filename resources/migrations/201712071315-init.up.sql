CREATE TABLE users (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  email TEXT NOT NULL,
  passhash VARCHAR(80) NOT NULL,
  apikey VARCHAR(64) NOT NULL
);

--;;

CREATE TABLE notifications (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  owner INTEGER NOT NULL,
  userid INTEGER NOT NULL,
  title TEXT NOT NULL,
  content TEXT NOT NULL,
  sendtime TIMESTAMP NOT NULL,

  INDEX owner_idx (owner),

  FOREIGN KEY (owner)
  REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

--;;

CREATE TABLE tags (
  id INT PRIMARY KEY AUTO_INCREMENT,
  owner INTEGER NOT NULL,
  userid INTEGER NOT NULL,
  name TEXT NOT NULL,

  INDEX owner_idx (owner),

  FOREIGN KEY (owner)
  REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

--;;

CREATE TABLE notif_tag_join (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  notifid INTEGER NOT NULL,
  tagid INTEGER NOT NULL,

  UNIQUE (notifid, tagid),
  INDEX notifid_idx (notifid),
  INDEX tagid_idx (tagid),

  FOREIGN KEY (notifid)
  REFERENCES notifications(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,

  FOREIGN KEY (tagid)
  REFERENCES tags(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);