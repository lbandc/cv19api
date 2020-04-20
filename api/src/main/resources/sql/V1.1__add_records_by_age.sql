CREATE TABLE death_records_by_age (
    id VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    recorded_on DATE NOT NULL,
    day_of_death DATE NOT NULL,
    deaths INT NOT NULL,
    age_range VARCHAR NOT NULL,
    source_id VARCHAR NOT NULL,
    UNIQUE (age_range, recorded_on, day_of_death),
    FOREIGN KEY (source_id) REFERENCES ingests(id)
);