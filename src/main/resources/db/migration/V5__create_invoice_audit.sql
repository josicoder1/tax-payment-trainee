CREATE TABLE invoice_audit (
                               id UUID PRIMARY KEY,
                               invoice_id UUID NOT NULL,
                               old_status VARCHAR(50),
                               new_status VARCHAR(50),
                               changed_at TIMESTAMP WITH TIME ZONE NOT NULL
);