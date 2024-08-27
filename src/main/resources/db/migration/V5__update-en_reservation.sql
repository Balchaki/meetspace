CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE en_reservation
    ADD CONSTRAINT no_overlapping_reservations
        EXCLUDE USING gist (
        seq_room WITH =,
        tsrange(date_time_start, date_time_end) WITH &&
        );

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_reservation_room') THEN
            CREATE INDEX idx_reservation_room ON en_reservation USING btree (seq_room);
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_reservation_user') THEN
            CREATE INDEX idx_reservation_user ON en_reservation USING btree (seq_user);
        END IF;
    END $$;

SELECT
    column_name,
    data_type,
    is_nullable
FROM
    information_schema.columns
WHERE
    table_name = 'en_reservation';

SELECT
    conname AS constraint_name,
    pg_get_constraintdef(oid) AS constraint_definition
FROM
    pg_constraint
WHERE
    conrelid = 'en_reservation'::regclass;

SELECT
    indexname,
    indexdef
FROM
    pg_indexes
WHERE
    tablename = 'en_reservation';
