CREATE TABLE trusts (
    code VARCHAR PRIMARY KEY,
    last_updated TIMESTAMP NOT NULL,
    source VARCHAR,
    name VARCHAR NOT NULL,
    region VARCHAR NOT NULL
);

CREATE TABLE ingests (
    id VARCHAR PRIMARY KEY,
    url VARCHAR UNIQUE,
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE trust_ingests (
    trust_code VARCHAR NOT NULL,
    id VARCHAR NOT NULL,
    FOREIGN KEY (trust_code) REFERENCES trusts(code),
    FOREIGN KEY (id) REFERENCES ingests(id)
);

CREATE TABLE trust_deaths (
    date DATE NOT NULL,
    trust_code VARCHAR NOT NULL,
    deaths INT NOT NULL,
    FOREIGN KEY (trust_code) REFERENCES trusts(code)
);
