CREATE TABLE users (
                       id UUID PRIMARY KEY,

                       owner_id UUID NOT NULL REFERENCES owners(id),

                       branch_id UUID REFERENCES branches(id),

                       email VARCHAR(150) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,

                       full_name VARCHAR(150) NOT NULL,
                       phone VARCHAR(20),

                       role VARCHAR(50) NOT NULL, -- OWNER_ADMIN, BRANCH_MANAGER, STAFF

                       is_active BOOLEAN NOT NULL DEFAULT TRUE,

                       created_by UUID NOT NULL REFERENCES owners(id),
                       updated_by UUID NOT NULL REFERENCES owners(id),

                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,

                       CONSTRAINT uk_user_email_per_owner UNIQUE (owner_id, email)
);