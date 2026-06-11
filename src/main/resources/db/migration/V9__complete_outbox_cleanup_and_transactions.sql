-- Finish outbox alignment when old columns remain from a partial migration
UPDATE outbox_events SET occurred_at = created_at WHERE occurred_at IS NULL;
UPDATE outbox_events SET published = false WHERE published IS NULL;
UPDATE outbox_events SET created_at = now() WHERE created_at IS NULL;

ALTER TABLE outbox_events DROP COLUMN IF EXISTS aggregate_id;
ALTER TABLE outbox_events DROP COLUMN IF EXISTS status;
ALTER TABLE outbox_events DROP COLUMN IF EXISTS type;
ALTER TABLE outbox_events DROP COLUMN IF EXISTS payload;

ALTER TABLE outbox_events ALTER COLUMN event_type SET NOT NULL;
ALTER TABLE outbox_events ALTER COLUMN event_data SET NOT NULL;
ALTER TABLE outbox_events ALTER COLUMN occurred_at SET NOT NULL;
ALTER TABLE outbox_events ALTER COLUMN created_at SET NOT NULL;
ALTER TABLE outbox_events ALTER COLUMN published SET NOT NULL;
ALTER TABLE outbox_events ALTER COLUMN published SET DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_outbox_events_unpublished ON outbox_events (published, created_at)
    WHERE published = false;

CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY,
    invoice_id UUID REFERENCES invoices(id),
    payment_id UUID REFERENCES payments(id),
    type VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    amount NUMERIC(38, 2),
    currency VARCHAR(10),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_transactions_invoice_id ON transactions (invoice_id);
CREATE INDEX IF NOT EXISTS idx_transactions_payment_id ON transactions (payment_id);
CREATE INDEX IF NOT EXISTS idx_transactions_type ON transactions (type);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions (created_at);
