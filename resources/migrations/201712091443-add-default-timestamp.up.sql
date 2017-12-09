ALTER TABLE ONLY notifications
  ALTER COLUMN sendtime
  SET DEFAULT current_timestamp;