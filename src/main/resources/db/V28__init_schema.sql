-- Drop old FK constraints

ALTER TABLE businesses
DROP CONSTRAINT businesses_created_by_fkey;

ALTER TABLE businesses
DROP CONSTRAINT businesses_updated_by_fkey;

ALTER TABLE branches
DROP CONSTRAINT branches_created_by_fkey;

ALTER TABLE branches
DROP CONSTRAINT branches_updated_by_fkey;

ALTER TABLE users
DROP CONSTRAINT users_created_by_fkey;

ALTER TABLE users
DROP CONSTRAINT users_updated_by_fkey;


-- Recreate FKs referencing users(id)

ALTER TABLE businesses
    ADD CONSTRAINT businesses_created_by_fkey
        FOREIGN KEY (created_by) REFERENCES users(id);

ALTER TABLE businesses
    ADD CONSTRAINT businesses_updated_by_fkey
        FOREIGN KEY (updated_by) REFERENCES users(id);

ALTER TABLE branches
    ADD CONSTRAINT branches_created_by_fkey
        FOREIGN KEY (created_by) REFERENCES users(id);

ALTER TABLE branches
    ADD CONSTRAINT branches_updated_by_fkey
        FOREIGN KEY (updated_by) REFERENCES users(id);

ALTER TABLE users
    ADD CONSTRAINT users_created_by_fkey
        FOREIGN KEY (created_by) REFERENCES users(id);

ALTER TABLE users
    ADD CONSTRAINT users_updated_by_fkey
        FOREIGN KEY (updated_by) REFERENCES users(id);