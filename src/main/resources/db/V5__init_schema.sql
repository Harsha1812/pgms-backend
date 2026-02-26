CREATE TABLE rooms (
                       id UUID PRIMARY KEY,
                       owner_id UUID NOT NULL REFERENCES owners(id),
                       branch_id UUID NOT NULL REFERENCES branches(id),

                       room_number VARCHAR(50) NOT NULL,
                       floor_number INTEGER,
                       capacity INTEGER NOT NULL,

                       is_active BOOLEAN NOT NULL DEFAULT TRUE,

                       created_by UUID NOT NULL REFERENCES owners(id),
                       updated_by UUID NOT NULL REFERENCES owners(id),
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,

                       CONSTRAINT uk_room_per_branch UNIQUE (branch_id, room_number)
);

CREATE TABLE beds (
                      id UUID PRIMARY KEY,
                      owner_id UUID NOT NULL REFERENCES owners(id),
                      branch_id UUID NOT NULL REFERENCES branches(id),
                      room_id UUID NOT NULL REFERENCES rooms(id),

                      bed_number VARCHAR(20) NOT NULL,
                      is_active BOOLEAN NOT NULL DEFAULT TRUE,

                      created_by UUID NOT NULL REFERENCES owners(id),
                      updated_by UUID NOT NULL REFERENCES owners(id),
                      created_at TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP NOT NULL,

                      CONSTRAINT uk_bed_per_room UNIQUE (room_id, bed_number)
);

CREATE TABLE room_pricing (
                              id UUID PRIMARY KEY,
                              owner_id UUID NOT NULL REFERENCES owners(id),
                              branch_id UUID NOT NULL REFERENCES branches(id),
                              room_id UUID NOT NULL REFERENCES rooms(id),

                              occupancy_type VARCHAR(30) NOT NULL, -- SINGLE, DOUBLE, TRIPLE
                              price_per_bed NUMERIC(12,2) NOT NULL,

                              effective_from DATE NOT NULL,
                              effective_to DATE,

                              created_by UUID NOT NULL REFERENCES owners(id),
                              updated_by UUID NOT NULL REFERENCES owners(id),
                              created_at TIMESTAMP NOT NULL,
                              updated_at TIMESTAMP NOT NULL
);