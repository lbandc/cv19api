CREATE TABLE trusts (
    code VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL,
    region VARCHAR NOT NULL
);

CREATE TABLE ingests (
    id VARCHAR PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    url VARCHAR UNIQUE NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INT NOT NULL
);

CREATE TABLE death_records_by_trust (
    id VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    recorded_on DATE NOT NULL,
    day_of_death DATE NOT NULL,
    deaths INT NOT NULL,
    trust_id VARCHAR NOT NULL,
    source_id VARCHAR NOT NULL,
    FOREIGN KEY (trust_id) REFERENCES trusts(code),
    FOREIGN KEY (source_id) REFERENCES ingests(id)
);

