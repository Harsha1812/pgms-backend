CREATE TABLE super_admins (
                              id UUID PRIMARY KEY,
                              email VARCHAR(150) NOT NULL UNIQUE,
                              password_hash VARCHAR(255) NOT NULL,
                              full_name VARCHAR(150) NOT NULL,
                              is_active BOOLEAN NOT NULL DEFAULT TRUE,
                              created_at TIMESTAMP NOT NULL,
                              updated_at TIMESTAMP NOT NULL
);