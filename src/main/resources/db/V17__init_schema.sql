CREATE TABLE referral_rewards (
                                  id UUID PRIMARY KEY,

                                  owner_id UUID NOT NULL REFERENCES owners(id),
                                  branch_id UUID NOT NULL REFERENCES branches(id),

                                  referrer_tenant_id UUID NOT NULL REFERENCES tenants(id),
                                  referred_tenant_id UUID NOT NULL REFERENCES tenants(id),

                                  beneficiary_tenant_id UUID NOT NULL REFERENCES tenants(id),

                                  reward_amount NUMERIC(12,2) NOT NULL,
                                  reward_status VARCHAR(30) NOT NULL, -- ELIGIBLE, APPLIED

                                  eligible_on DATE NOT NULL,
                                  applied_on DATE,

                                  created_at TIMESTAMP NOT NULL,
                                  updated_at TIMESTAMP NOT NULL,

                                  CONSTRAINT chk_reward_positive CHECK (reward_amount > 0),
                                  CONSTRAINT chk_reward_status
                                      CHECK (reward_status IN ('ELIGIBLE','APPLIED'))
);

ALTER TABLE branches
    ADD COLUMN referral_reward_referrer NUMERIC(12,2) DEFAULT 0 NOT NULL,
ADD COLUMN referral_reward_referred NUMERIC(12,2) DEFAULT 0 NOT NULL;

ALTER TABLE branches
    ADD CONSTRAINT chk_referral_rewards_positive
        CHECK (
            referral_reward_referrer >= 0
                AND referral_reward_referred >= 0
            );
