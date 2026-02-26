DROP INDEX IF EXISTS idx_allocation_active;
DROP INDEX IF EXISTS idx_invoice_status;
CREATE INDEX idx_allocation_active
    ON bed_allocations(owner_id, is_active);

CREATE INDEX idx_invoice_status
    ON monthly_invoices(owner_id, status);