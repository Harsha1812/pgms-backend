CREATE TABLE tenants (
                         id UUID PRIMARY KEY,

                         owner_id UUID NOT NULL REFERENCES owners(id),
                         branch_id UUID NOT NULL REFERENCES branches(id),

                         full_name VARCHAR(150) NOT NULL,
                         phone VARCHAR(20) NOT NULL,
                         email VARCHAR(150),

                         government_id VARCHAR(100), -- Aadhaar / Passport
                         emergency_contact_name VARCHAR(150),
                         emergency_contact_phone VARCHAR(20),

                         status VARCHAR(30) NOT NULL, -- ACTIVE, INACTIVE, BLACKLISTED

                         joined_date DATE NOT NULL,
                         exit_date DATE,

                         is_active BOOLEAN NOT NULL DEFAULT TRUE,

                         created_by UUID NOT NULL REFERENCES owners(id),
                         updated_by UUID NOT NULL REFERENCES owners(id),
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL,

                         CONSTRAINT chk_tenant_status
                             CHECK (status IN ('ACTIVE', 'INACTIVE', 'BLACKLISTED'))
);

CREATE TABLE bed_allocations (
                                 id UUID PRIMARY KEY,

                                 owner_id UUID NOT NULL REFERENCES owners(id),
                                 branch_id UUID NOT NULL REFERENCES branches(id),

                                 bed_id UUID NOT NULL REFERENCES beds(id),
                                 tenant_id UUID NOT NULL REFERENCES tenants(id),

                                 allocation_start_date DATE NOT NULL,
                                 allocation_end_date DATE,

                                 monthly_rent NUMERIC(12,2) NOT NULL,

                                 is_active BOOLEAN NOT NULL DEFAULT TRUE,

                                 created_by UUID NOT NULL REFERENCES owners(id),
                                 updated_by UUID NOT NULL REFERENCES owners(id),
                                 created_at TIMESTAMP NOT NULL,
                                 updated_at TIMESTAMP NOT NULL,

                                 CONSTRAINT chk_rent_positive CHECK (monthly_rent > 0)
);

CREATE INDEX idx_tenant_owner ON tenants(owner_id);
CREATE INDEX idx_tenant_branch ON tenants(branch_id);

CREATE INDEX idx_allocation_owner ON bed_allocations(owner_id);
CREATE INDEX idx_allocation_bed ON bed_allocations(bed_id);
CREATE INDEX idx_allocation_tenant ON bed_allocations(tenant_id);
CREATE INDEX idx_allocation_active ON bed_allocations(is_active);