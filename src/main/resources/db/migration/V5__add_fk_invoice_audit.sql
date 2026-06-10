ALTER TABLE invoice_audit
    ADD CONSTRAINT fk_invoice_audit_invoice
        FOREIGN KEY (invoice_id)
            REFERENCES invoices(id)
            ON DELETE CASCADE;