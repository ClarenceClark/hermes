CREATE TABLE users (
  id       SERIAL PRIMARY KEY,
  email    TEXT         NOT NULL UNIQUE,
  passhash VARCHAR(256) NOT NULL,
  apikey   VARCHAR(256) NOT NULL
);

--;;

CREATE TABLE notifications (
  id       SERIAL PRIMARY KEY,
  userid   INTEGER   NOT NULL
    REFERENCES users (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  title    TEXT      NOT NULL,
  content  TEXT      NOT NULL,
  sendtime TIMESTAMP NOT NULL
    DEFAULT current_timestamp
);

--;;

CREATE TABLE tags (
  userid INTEGER NOT NULL
    REFERENCES users (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  easyid INTEGER NOT NULL,
  name   TEXT    NOT NULL,

  PRIMARY KEY (userid, easyid),
  UNIQUE (userid, name)
);

--;;

CREATE TABLE notif_tags (
  tagid   INTEGER NOT NULL,
  userid  INTEGER NOT NULL,

  notifid INTEGER NOT NULL
    REFERENCES notifications (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,

  PRIMARY KEY (notifid, tagid, userid),

  FOREIGN KEY (userid, tagid)
  REFERENCES tags (userid, easyid)
);