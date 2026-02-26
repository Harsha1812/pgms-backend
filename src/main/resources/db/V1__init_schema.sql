CREATE TABLE owners (
                        id UUID PRIMARY KEY,
                        email VARCHAR(150) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL,
                        full_name VARCHAR(150) NOT NULL,
                        phone VARCHAR(20),

                        is_active BOOLEAN NOT NULL DEFAULT TRUE,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP NOT NULL
);

CREATE TABLE businesses (
                            id UUID PRIMARY KEY,
                            owner_id UUID NOT NULL REFERENCES owners(id),

                            name VARCHAR(150) NOT NULL,
                            code VARCHAR(50) NOT NULL,

                            is_active BOOLEAN NOT NULL DEFAULT TRUE,
                            created_at TIMESTAMP NOT NULL,
                            updated_at TIMESTAMP NOT NULL,

                            CONSTRAINT uk_business_code UNIQUE (owner_id, code)
);

CREATE TABLE branches (
                          id UUID PRIMARY KEY,
                          business_id UUID NOT NULL REFERENCES businesses(id),

                          name VARCHAR(150) NOT NULL,
                          code VARCHAR(50) NOT NULL,

                          city VARCHAR(100) NOT NULL,
                          state VARCHAR(100) NOT NULL,
                          country VARCHAR(100) NOT NULL DEFAULT 'India',

                          address_line1 VARCHAR(255) NOT NULL,
                          address_line2 VARCHAR(255),
                          pincode VARCHAR(20),

                          total_floors INTEGER,
                          total_rooms INTEGER,

                          is_active BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,

                          CONSTRAINT uk_branch_code UNIQUE (business_id, code)
);
