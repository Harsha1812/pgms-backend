-- businesses
CREATE INDEX idx_business_owner_id ON businesses(owner_id);

-- branches
CREATE INDEX idx_branch_business_id ON branches(business_id);

-- users
CREATE INDEX idx_users_owner_id ON users(owner_id);
CREATE INDEX idx_users_branch_id ON users(branch_id);


ALTER TABLE branches
    ADD COLUMN latitude DECIMAL(9,6),
ADD COLUMN longitude DECIMAL(9,6),
ADD COLUMN created_by UUID NOT NULL REFERENCES owners(id),
ADD COLUMN updated_by UUID NOT NULL REFERENCES owners(id);

ALTER TABLE businesses
    ADD COLUMN created_by UUID NOT NULL REFERENCES owners(id),
ADD COLUMN updated_by UUID NOT NULL REFERENCES owners(id);


ALTER TABLE users
    ADD CONSTRAINT chk_user_role
        CHECK (role IN ('OWNER_ADMIN', 'BRANCH_MANAGER', 'STAFF'));