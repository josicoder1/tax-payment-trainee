ALTER TABLE outbox_events RENAME COLUMN type TO event_type;
ALTER TABLE outbox_events RENAME COLUMN payload TO event_data;

ALTER TABLE outbox_events ADD COLUMN occurred_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE outbox_events ADD COLUMN published BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE outbox_events ADD COLUMN published_at TIMESTAMP WITH TIME ZONE;

UPDATE outbox_events
SET published = (status = 'PUBLISHED')
WHERE status IS NOT NULL;

UPDATE outbox_events
SET occurred_at = created_at
WHERE occurred_at IS NULL;

UPDATE outbox_events
SET created_at = now()
WHERE created_at IS NULL;

ALTER TABLE outbox_events DROP COLUMN aggregate_id;
ALTER TABLE outbox_events DROP COLUMN status;

ALTER TABLE outbox_events ALTER COLUMN event_type SET NOT NULL;
ALTER TABLE outbox_events ALTER COLUMN event_data SET NOT NULL;
ALTER TABLE outbox_events ALTER COLUMN occurred_at SET NOT NULL;
ALTER TABLE outbox_events ALTER COLUMN created_at SET NOT NULL;

CREATE INDEX idx_outbox_events_unpublished ON outbox_events (published, created_at)
    WHERE published = false;
