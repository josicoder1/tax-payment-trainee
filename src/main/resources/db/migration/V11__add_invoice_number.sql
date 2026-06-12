CREATE SEQUENCE invoice_number_seq START WITH 1 INCREMENT BY 1;

ALTER TABLE invoices ADD COLUMN invoice_number VARCHAR(50);

UPDATE invoices i
SET invoice_number = 'INV-LEGACY-' || LPAD(n.rn::text, 6, '0')
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS rn
    FROM invoices
) n
WHERE i.id = n.id
  AND i.invoice_number IS NULL;

ALTER TABLE invoices ALTER COLUMN invoice_number SET NOT NULL;

CREATE UNIQUE INDEX idx_invoices_invoice_number ON invoices (invoice_number);
