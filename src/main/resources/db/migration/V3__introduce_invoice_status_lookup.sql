CREATE TABLE invoice_status (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

INSERT INTO invoice_status (code, description)
VALUES
('OPEN', 'Open invoice'),
('PARTIALLY_PAID', 'Partially paid invoice'),
('PAID', 'Fully paid invoice'),
('VOIDED', 'Voided invoice');

ALTER TABLE invoices
ADD COLUMN status_id BIGINT;

UPDATE invoices SET status_id =
    (SELECT id FROM invoice_status WHERE code = invoices.status);

ALTER TABLE invoices
DROP COLUMN status;

ALTER TABLE invoices
ADD CONSTRAINT fk_invoice_status
FOREIGN KEY (status_id)
REFERENCES invoice_status(id);
