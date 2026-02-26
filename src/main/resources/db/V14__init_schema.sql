ALTER TABLE monthly_invoices
    ADD COLUMN invoice_number VARCHAR(50) UNIQUE,
    ADD COLUMN pdf_generated BOOLEAN DEFAULT FALSE NOT NULL,
    ADD COLUMN pdf_file_path VARCHAR(255);

