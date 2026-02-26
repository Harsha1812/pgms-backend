CREATE TABLE hostels (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         city VARCHAR(255),

                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL,
                         created_by BIGINT,
                         last_updated_by BIGINT
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       hostel_id BIGINT NOT NULL,

                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       is_active BOOLEAN NOT NULL,

                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,
                       created_by BIGINT,
                       last_updated_by BIGINT,

                       CONSTRAINT fk_hostel
                           FOREIGN KEY (hostel_id)
                               REFERENCES hostels(id)
                               ON DELETE CASCADE
);