CREATE TABLE monthly_invoices (
                                  id UUID PRIMARY KEY,

                                  owner_id UUID NOT NULL REFERENCES owners(id),
                                  branch_id UUID NOT NULL REFERENCES branches(id),
                                  tenant_id UUID NOT NULL REFERENCES tenants(id),
                                  bed_allocation_id UUID NOT NULL REFERENCES bed_allocations(id),

                                  billing_year INTEGER NOT NULL,
                                  billing_month INTEGER NOT NULL, -- 1 to 12

                                  period_start DATE NOT NULL,
                                  period_end DATE NOT NULL,

                                  total_days INTEGER NOT NULL,
                                  chargeable_days INTEGER NOT NULL,

                                  monthly_rent_snapshot NUMERIC(12,2) NOT NULL,
                                  rent_amount NUMERIC(12,2) NOT NULL,

                                  status VARCHAR(30) NOT NULL, -- GENERATED, PAID, PARTIAL, OVERDUE

                                  due_date DATE NOT NULL,

                                  created_by UUID NOT NULL REFERENCES owners(id),
                                  updated_by UUID NOT NULL REFERENCES owners(id),
                                  created_at TIMESTAMP NOT NULL,
                                  updated_at TIMESTAMP NOT NULL,

                                  CONSTRAINT uk_invoice_per_tenant_month
                                      UNIQUE (tenant_id, billing_year, billing_month),

                                  CONSTRAINT chk_invoice_status
                                      CHECK (status IN ('GENERATED', 'PAID', 'PARTIAL', 'OVERDUE'))
);

CREATE INDEX idx_invoice_owner ON monthly_invoices(owner_id);
CREATE INDEX idx_invoice_tenant ON monthly_invoices(tenant_id);
CREATE INDEX idx_invoice_status ON monthly_invoices(status);
CREATE INDEX idx_invoice_branch ON monthly_invoices(branch_id);