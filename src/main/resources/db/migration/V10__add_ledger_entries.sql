CREATE TABLE ledger_entries (
                                id UUID PRIMARY KEY,
                                transaction_id UUID,
                                payment_id UUID,
                                invoice_id UUID,
                                account VARCHAR(50),
                                type VARCHAR(20),
                                amount NUMERIC(38,2),
                                currency VARCHAR(10),
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_ledger_payment_id ON ledger_entries(payment_id);
CREATE INDEX idx_ledger_invoice_id ON ledger_entries(invoice_id);
CREATE INDEX idx_ledger_created_at ON ledger_entries(created_at);