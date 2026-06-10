CREATE TABLE payment_audit (
    id UUID PRIMARY KEY,
    payment_id UUID NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    old_status VARCHAR(100),
    new_status VARCHAR(100),
    reason VARCHAR(1024),
    idempotency_key VARCHAR(255),
    payload TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE INDEX idx_payment_audit_payment_id ON payment_audit(payment_id);
CREATE INDEX idx_payment_audit_idempotency_key ON payment_audit(idempotency_key);
