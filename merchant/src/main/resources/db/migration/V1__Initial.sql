CREATE TABLE merchant (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255),
                          description VARCHAR(255),
                          email VARCHAR(255),
                          status VARCHAR(255) NOT NULL,
                          total_transaction_sum DOUBLE PRECISION
);