ALTER TABLE monthly_invoices
    ADD COLUMN late_fee_amount NUMERIC(12,2) DEFAULT 0 NOT NULL,
ADD COLUMN late_fee_applied BOOLEAN DEFAULT FALSE NOT NULL,
ADD COLUMN late_fee_exempted BOOLEAN DEFAULT FALSE NOT NULL,
ADD COLUMN late_fee_exemption_reason VARCHAR(255);

ALTER TABLE monthly_invoices
    ADD CONSTRAINT chk_late_fee_positive
        CHECK (late_fee_amount >= 0);

ALTER TABLE monthly_invoices
    ADD CONSTRAINT chk_late_fee_exemption_logic
        CHECK (
            (late_fee_exempted = FALSE)
                OR
            (late_fee_exempted = TRUE AND late_fee_applied = TRUE)
            );

ALTER TABLE branches
    ADD COLUMN fixed_late_fee NUMERIC(12,2) DEFAULT 0 NOT NULL;

ALTER TABLE branches
    ADD CONSTRAINT chk_fixed_late_fee_positive
        CHECK (fixed_late_fee >= 0);