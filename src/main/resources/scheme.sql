CREATE TABLE IF NOT EXISTS user (
    id BIGINT auto_increment PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    cents LONG DEFAULT 0
);
CREATE TABLE IF NOT EXISTS transfer(
    id BIGINT auto_increment PRIMARY KEY ,
    from_user_id LONG NOT NULL,
    to_user_id LONG NOT NULL,
    summ LONG NOT NULL
);

ALTER TABLE transfer
    ADD FOREIGN KEY (from_user_id) REFERENCES user(id);

ALTER TABLE transfer
    ADD FOREIGN KEY (to_user_id) REFERENCES user(id);