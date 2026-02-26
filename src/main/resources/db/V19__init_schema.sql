CREATE TABLE billing_plans (
                               id UUID PRIMARY KEY,

                               name VARCHAR(100) NOT NULL,
                               description TEXT,

                               base_price NUMERIC(12,2) NOT NULL, -- monthly subscription fee
                               price_per_active_tenant NUMERIC(12,2) DEFAULT 0 NOT NULL,

                               max_branches INTEGER,
                               max_tenants INTEGER,

                               trial_days INTEGER DEFAULT 0 NOT NULL,

                               is_active BOOLEAN NOT NULL DEFAULT TRUE,

                               created_at TIMESTAMP NOT NULL,
                               updated_at TIMESTAMP NOT NULL
);

CREATE TABLE owner_subscriptions (
                                     id UUID PRIMARY KEY,

                                     owner_id UUID NOT NULL REFERENCES owners(id),
                                     billing_plan_id UUID NOT NULL REFERENCES billing_plans(id),

                                     subscription_status VARCHAR(30) NOT NULL,
    -- TRIAL, ACTIVE, SUSPENDED, CANCELLED

                                     trial_start_date DATE,
                                     trial_end_date DATE,

                                     current_period_start DATE,
                                     current_period_end DATE,

                                     created_at TIMESTAMP NOT NULL,
                                     updated_at TIMESTAMP NOT NULL,

                                     CONSTRAINT chk_subscription_status
                                         CHECK (subscription_status IN ('TRIAL','ACTIVE','SUSPENDED','CANCELLED'))
);

CREATE TABLE owner_usage_snapshots (
                                       id UUID PRIMARY KEY,

                                       owner_id UUID NOT NULL REFERENCES owners(id),
                                       billing_year INTEGER NOT NULL,
                                       billing_month INTEGER NOT NULL,

                                       active_tenant_count INTEGER NOT NULL,
                                       branch_count INTEGER NOT NULL,

                                       created_at TIMESTAMP NOT NULL,

                                       CONSTRAINT uk_owner_usage_month
                                           UNIQUE (owner_id, billing_year, billing_month)
);

CREATE TABLE platform_invoices (
                                   id UUID PRIMARY KEY,

                                   owner_id UUID NOT NULL REFERENCES owners(id),
                                   subscription_id UUID NOT NULL REFERENCES owner_subscriptions(id),

                                   billing_year INTEGER NOT NULL,
                                   billing_month INTEGER NOT NULL,

                                   base_plan_amount NUMERIC(12,2) NOT NULL,
                                   usage_amount NUMERIC(12,2) NOT NULL,
                                   total_amount NUMERIC(12,2) NOT NULL,

                                   status VARCHAR(30) NOT NULL, -- GENERATED, PAID, OVERDUE

                                   due_date DATE NOT NULL,

                                   created_at TIMESTAMP NOT NULL,
                                   updated_at TIMESTAMP NOT NULL
);