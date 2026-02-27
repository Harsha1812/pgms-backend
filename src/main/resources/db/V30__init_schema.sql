CREATE TABLE branch_documents (
                                  id UUID PRIMARY KEY,

                                  owner_id UUID NOT NULL REFERENCES owners(id),
                                  branch_id UUID NOT NULL REFERENCES branches(id),

                                  document_type VARCHAR(100) NOT NULL,
                                  document_name VARCHAR(150) NOT NULL,

                                  document_number VARCHAR(100),
                                  issuing_authority VARCHAR(150),

                                  issue_date DATE,
                                  expiry_date DATE,

                                  reminder_days_before INTEGER DEFAULT 30,

                                  file_url VARCHAR(500),

                                  status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',

                                  is_active BOOLEAN NOT NULL DEFAULT TRUE,

                                  created_by UUID NOT NULL REFERENCES users(id),
                                  updated_by UUID NOT NULL REFERENCES users(id),

                                  created_at TIMESTAMP NOT NULL,
                                  updated_at TIMESTAMP NOT NULL,

                                  CONSTRAINT chk_branch_document_status CHECK (
                                      status IN ('ACTIVE', 'EXPIRED', 'RENEWED')
                                      )
);

CREATE TABLE branch_document_renewals (
                                          id UUID PRIMARY KEY,

                                          document_id UUID NOT NULL REFERENCES branch_documents(id),

                                          previous_expiry_date DATE,
                                          new_expiry_date DATE NOT NULL,

                                          renewal_date DATE NOT NULL,
                                          renewal_amount NUMERIC(12,2),

                                          notes TEXT,

                                          created_by UUID NOT NULL REFERENCES users(id),
                                          created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_branch_documents_owner
    ON branch_documents(owner_id);

CREATE INDEX idx_branch_documents_branch
    ON branch_documents(branch_id);

CREATE INDEX idx_branch_documents_expiry
    ON branch_documents(owner_id, expiry_date);

CREATE INDEX idx_branch_document_renewals_doc
    ON branch_document_renewals(document_id);