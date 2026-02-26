ALTER TABLE bed_allocations
    ADD COLUMN security_deposit NUMERIC(12,2) NOT NULL,
ADD COLUMN deposit_paid BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN deposit_collected_date DATE,
ADD COLUMN deposit_refunded BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN deposit_refunded_amount NUMERIC(12,2),
ADD COLUMN deposit_refunded_date DATE;

ALTER TABLE bed_allocations
    ADD CONSTRAINT chk_deposit_positive
        CHECK (security_deposit >= 0);

ALTER TABLE bed_allocations
    ADD CONSTRAINT chk_refund_amount_valid
        CHECK (
            deposit_refunded_amount IS NULL
                OR deposit_refunded_amount >= 0
            );

ALTER TABLE bed_allocations
    ADD CONSTRAINT chk_deposit_collected_consistency
        CHECK (
            (deposit_paid = FALSE AND deposit_collected_date IS NULL)
                OR
            (deposit_paid = TRUE AND deposit_collected_date IS NOT NULL)
            );

ALTER TABLE bed_allocations
    ADD CONSTRAINT chk_refund_not_exceed_deposit
        CHECK (
            deposit_refunded_amount IS NULL
                OR deposit_refunded_amount <= security_deposit
            );