CREATE DATABASE cv19api;
CREATE TABLE trusts (
    code VARCHAR PRIMARY KEY,
    last_updated_utc TIMESTAMP NOT NULL,
    source VARCHAR,
    name VARCHAR NOT NULL,
    region VARCHAR NOT NULL
);

CREATE TABLE trust_deaths (
    date DATE NOT NULL,
    trust_code VARCHAR NOT NULL,
    deaths INT NOT NULL,
    FOREIGN KEY (trust_code) REFERENCES trusts(trust_code)
);