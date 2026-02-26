ALTER TABLE branches
    ADD COLUMN upfront_discount_percentage NUMERIC(5,2) DEFAULT 0 NOT NULL,
ADD COLUMN upfront_discount_min_months INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE branches
    ADD CONSTRAINT chk_upfront_discount_percentage
        CHECK (upfront_discount_percentage >= 0 AND upfront_discount_percentage <= 100);

ALTER TABLE branches
    ADD CONSTRAINT chk_upfront_discount_min_months
        CHECK (upfront_discount_min_months >= 0);

ALTER TABLE bed_allocations
    ADD COLUMN upfront_months_committed INTEGER DEFAULT 0 NOT NULL,
ADD COLUMN upfront_discount_percentage_snapshot NUMERIC(5,2) DEFAULT 0 NOT NULL;

ALTER TABLE bed_allocations
    ADD CONSTRAINT chk_upfront_months_non_negative
        CHECK (upfront_months_committed >= 0);


ALTER TABLE monthly_invoices
    ADD COLUMN discount_percentage_snapshot NUMERIC(5,2) DEFAULT 0 NOT NULL,
ADD COLUMN discount_amount NUMERIC(12,2) DEFAULT 0 NOT NULL;