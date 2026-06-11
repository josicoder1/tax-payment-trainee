ALTER TABLE payments
    ALTER COLUMN idempotency_key SET NOT NULL;

CREATE UNIQUE INDEX ux_payments_idempotency_key
    ON payments(idempotency_key);