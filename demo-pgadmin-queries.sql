-- Tax Payment Demo — pgAdmin Query Cheat Sheet
-- Replace {{invoiceId}} and {{paymentId}} with values from Postman responses

-- 1. List all tables
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public' ORDER BY table_name;

-- 2. All invoices with status and outstanding
SELECT i.id, i.taxpayer_tin, ist.code AS status,
       i.principal_amount, i.interest_amount, i.penalty_amount,
       i.total_paid_principal, i.total_paid_interest, i.total_paid_penalty,
       (i.principal_amount - COALESCE(i.total_paid_principal, 0)
        + i.interest_amount - COALESCE(i.total_paid_interest, 0)
        + i.penalty_amount - COALESCE(i.total_paid_penalty, 0)) AS outstanding
FROM invoices i
JOIN invoice_status ist ON i.status_id = ist.id
ORDER BY i.id DESC;

-- 3. Invoice by ID
SELECT i.*, ist.code AS status_code
FROM invoices i
JOIN invoice_status ist ON i.status_id = ist.id
WHERE i.id = '{{invoiceId}}';

-- 4. Recent payments
SELECT id, amount, currency, status, idempotency_key, reference_number, created_at
FROM payments
ORDER BY created_at DESC
LIMIT 10;

-- 5. Payment audit for a payment
SELECT event_type, old_status, new_status, reason, idempotency_key, payload, created_at
FROM payment_audit
WHERE payment_id = '{{paymentId}}'
ORDER BY created_at;

-- 6. Transaction ledger (all)
SELECT id, invoice_id, payment_id, type, description, amount, currency, created_at
FROM transactions
ORDER BY created_at DESC;

-- 7. Transactions for one invoice
SELECT * FROM transactions
WHERE invoice_id = '{{invoiceId}}'
ORDER BY created_at;

-- 8. Outbox events (domain events)
SELECT id, event_type, event_data, published, occurred_at, created_at
FROM outbox_events
ORDER BY created_at DESC
LIMIT 10;

-- 9. Idempotency check
SELECT COUNT(*) AS payment_count
FROM payments
WHERE idempotency_key = 'paste-key-here';
