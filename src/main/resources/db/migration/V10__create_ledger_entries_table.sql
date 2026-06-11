CREATE TABLE ledger_entries (
    id UUID PRIMARY KEY,
    invoice_id UUID REFERENCES invoices(id),
    payment_id UUID REFERENCES payments(id),
    transaction_id UUID REFERENCES transactions(id),
    account VARCHAR(50) NOT NULL,
    entry_side VARCHAR(10) NOT NULL,
    amount NUMERIC(38,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_ledger_entries_invoice_id ON ledger_entries(invoice_id);
CREATE INDEX idx_ledger_entries_payment_id ON ledger_entries(payment_id);
CREATE INDEX idx_ledger_entries_transaction_id ON ledger_entries(transaction_id);
CREATE INDEX idx_ledger_entries_account ON ledger_entries(account);
CREATE INDEX idx_ledger_entries_created_at ON ledger_entries(created_at);
