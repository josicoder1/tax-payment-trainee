CREATE TABLE invoices (
                          id UUID PRIMARY KEY,

                          taxpayer_tin VARCHAR(255),
                          tax_type VARCHAR(255),

                          tax_period_start DATE,
                          tax_period_end DATE,
                          tax_period_frequency VARCHAR(255),

                          currency VARCHAR(255),

                          principal_amount NUMERIC(38,2),
                          interest_amount NUMERIC(38,2),
                          penalty_amount NUMERIC(38,2),

                          total_paid_principal NUMERIC(38,2),
                          total_paid_interest NUMERIC(38,2),
                          total_paid_penalty NUMERIC(38,2),

                          status VARCHAR(255)
);

CREATE TABLE payments (
                          id UUID PRIMARY KEY,

                          amount NUMERIC(38,2),
                          currency VARCHAR(255),

                          taxpayer_id VARCHAR(255),
                          tax_type VARCHAR(255),
                          tax_period VARCHAR(255),

                          status VARCHAR(255),

                          created_at TIMESTAMP WITH TIME ZONE,

                          reference_number VARCHAR(255),
                          failure_reason VARCHAR(255)
);

CREATE TABLE outbox_events (
                               id UUID PRIMARY KEY,

                               aggregate_id VARCHAR(255),
                               created_at TIMESTAMP WITH TIME ZONE,

                               payload TEXT,

                               status VARCHAR(255),
                               type VARCHAR(255)
);