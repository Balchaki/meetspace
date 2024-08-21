CREATE TABLE en_reservation (
                                seq_reservation SERIAL PRIMARY KEY,
                                seq_user INTEGER REFERENCES en_user(seq_user),
                                seq_room INTEGER REFERENCES en_room(seq_room),
                                date_time_start TIMESTAMP NOT NULL,
                                date_time_end TIMESTAMP NOT NULL
);

CREATE INDEX idx_reservation_user ON en_reservation(seq_user);
CREATE INDEX idx_reservation_room ON en_reservation(seq_room);

ALTER TABLE en_reservation ADD CONSTRAINT check_date_time
    CHECK (date_time_end > date_time_start);
