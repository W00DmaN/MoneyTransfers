CREATE TABLE IF NOT EXISTS user (
    id LONG PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    cents BIGINT DEFAULT 0
);
CREATE TABLE IF NOT EXISTS transactions(
    sender_id LONG NOT NULL,
    recipient_id LONG NOT NULL,
    summ LONG NOT NULL
);

ALTER TABLE transactions
    ADD FOREIGN KEY (sender_id) REFERENCES user(id);

ALTER TABLE transactions
    ADD FOREIGN KEY (recipient_id) REFERENCES user(id);