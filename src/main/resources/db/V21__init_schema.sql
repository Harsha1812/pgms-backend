CREATE INDEX idx_invoice_owner_month
    ON monthly_invoices(owner_id, billing_year, billing_month);

CREATE INDEX idx_referral_beneficiary_status
    ON referral_rewards(beneficiary_tenant_id, reward_status);