ALTER TABLE branches ADD COLUMN version BIGINT DEFAULT 0 NOT NULL;
ALTER TABLE tenants ADD COLUMN version BIGINT DEFAULT 0 NOT NULL;
ALTER TABLE bed_allocations ADD COLUMN version BIGINT DEFAULT 0 NOT NULL;
ALTER TABLE monthly_invoices ADD COLUMN version BIGINT DEFAULT 0 NOT NULL;
ALTER TABLE owner_subscriptions ADD COLUMN version BIGINT DEFAULT 0 NOT NULL;

CREATE UNIQUE INDEX uk_active_bed_allocation
    ON bed_allocations(bed_id)
    WHERE is_active = TRUE;

ALTER TABLE bed_allocations
    ADD CONSTRAINT chk_deposit_refund_once
        CHECK (
            NOT (deposit_refunded = TRUE AND deposit_refunded_date IS NULL)
            );