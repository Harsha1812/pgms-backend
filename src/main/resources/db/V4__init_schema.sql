ALTER TABLE branches
    ADD COLUMN owner_id UUID;

UPDATE branches b
SET owner_id = bs.owner_id
    FROM businesses bs
WHERE b.business_id = bs.id;

ALTER TABLE branches
    ALTER COLUMN owner_id SET NOT NULL;

ALTER TABLE branches
    ADD CONSTRAINT branches_owner_id_fkey
        FOREIGN KEY (owner_id) REFERENCES owners(id);

CREATE INDEX idx_branch_owner_id
    ON branches(owner_id);

CREATE INDEX idx_users_owner_branch
    ON users(owner_id, branch_id);

ALTER TABLE branches
    ADD CONSTRAINT chk_total_floors_non_negative
        CHECK (total_floors IS NULL OR total_floors >= 0);

ALTER TABLE branches
    ADD CONSTRAINT chk_total_rooms_non_negative
        CHECK (total_rooms IS NULL OR total_rooms >= 0);