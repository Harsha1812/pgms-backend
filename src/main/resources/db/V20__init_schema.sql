CREATE TABLE platform_payments (
                                   id UUID PRIMARY KEY,

                                   owner_id UUID NOT NULL REFERENCES owners(id),
                                   platform_invoice_id UUID NOT NULL REFERENCES platform_invoices(id),

                                   payment_provider VARCHAR(30) NOT NULL, -- STRIPE, RAZORPAY, UPI
                                   provider_payment_id VARCHAR(150),
                                   provider_order_id VARCHAR(150),

                                   amount_paid NUMERIC(12,2) NOT NULL,
                                   payment_status VARCHAR(30) NOT NULL, -- INITIATED, SUCCESS, FAILED

                                   payment_date TIMESTAMP,

                                   created_at TIMESTAMP NOT NULL,
                                   updated_at TIMESTAMP NOT NULL,

                                   CONSTRAINT chk_platform_payment_status
                                       CHECK (payment_status IN ('INITIATED','SUCCESS','FAILED'))
);


ALTER TABLE owner_subscriptions
    ADD COLUMN provider_subscription_id VARCHAR(150);

ALTER TABLE owner_subscriptions
    ADD COLUMN grace_period_end DATE;