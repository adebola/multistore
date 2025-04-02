ALTER TABLE authorizations ADD COLUMN user_id VARCHAR(36);
ALTER TABLE authorizations ADD CONSTRAINT fk_authorizations_user_id FOREIGN KEY (user_id) REFERENCES users (id);