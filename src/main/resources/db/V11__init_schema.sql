CREATE TABLE payments (
                          id UUID PRIMARY KEY,

                          owner_id UUID NOT NULL REFERENCES owners(id),
                          branch_id UUID NOT NULL REFERENCES branches(id),
                          tenant_id UUID NOT NULL REFERENCES tenants(id),
                          invoice_id UUID NOT NULL REFERENCES monthly_invoices(id),

                          amount_paid NUMERIC(12,2) NOT NULL,

                          payment_date DATE NOT NULL,
                          payment_method VARCHAR(30) NOT NULL, -- CASH, UPI, BANK_TRANSFER

                          transaction_reference VARCHAR(150),

                          created_by UUID NOT NULL REFERENCES owners(id),
                          updated_by UUID NOT NULL REFERENCES owners(id),
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,

                          CONSTRAINT uk_payment_per_invoice UNIQUE (invoice_id),

                          CONSTRAINT chk_payment_method
                              CHECK (payment_method IN ('CASH','UPI','BANK_TRANSFER')),

                          CONSTRAINT chk_payment_amount_positive
                              CHECK (amount_paid > 0)
);

CREATE INDEX idx_payment_owner ON payments(owner_id);
CREATE INDEX idx_payment_tenant ON payments(tenant_id);
CREATE INDEX idx_payment_branch ON payments(branch_id);
CREATE INDEX idx_payment_date ON payments(payment_date);