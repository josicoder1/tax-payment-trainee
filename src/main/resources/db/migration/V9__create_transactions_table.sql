CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    invoice_id UUID REFERENCES invoices(id),
    payment_id UUID REFERENCES payments(id),
    type VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    amount NUMERIC(38,2),
    currency VARCHAR(10),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_transactions_invoice_id ON transactions(invoice_id);
CREATE INDEX idx_transactions_payment_id ON transactions(payment_id);
CREATE INDEX idx_transactions_type ON transactions(type);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);
