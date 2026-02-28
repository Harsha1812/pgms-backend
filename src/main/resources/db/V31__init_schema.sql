CREATE TABLE idempotency_keys (
                                  id UUID PRIMARY KEY,
                                  idempotency_key VARCHAR(100) NOT NULL,
                                  owner_id UUID NULL,
                                  endpoint VARCHAR(255) NOT NULL,
                                  request_hash VARCHAR(255) NOT NULL,
                                  response_body TEXT,
                                  status_code INTEGER,
                                  created_at TIMESTAMP NOT NULL,

                                  CONSTRAINT uk_idempotency UNIQUE (idempotency_key, endpoint)
);

CREATE INDEX idx_idempotency_owner
    ON idempotency_keys(owner_id);