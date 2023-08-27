CREATE TABLE transactions (
                              uuid UUID PRIMARY KEY,
                              belongs_to VARCHAR(255) NOT NULL,
                              status VARCHAR(50) NOT NULL, -- assuming TransactionStatusEnum has less than 50 characters
                              reference_id UUID,
                              customer_email VARCHAR(255),
                              customer_phone VARCHAR(20), -- assuming phone numbers won't be longer than 20 characters
                              transaction_type VARCHAR(50) NOT NULL, -- this is used for the discriminator column
                              amount DECIMAL(20, 2) -- assuming a scale of 2 decimal places and up to 20 digits in total
);

CREATE INDEX idx_transactions_reference_id ON transactions (reference_id);
CREATE INDEX idx_transactions_belongs_to ON transactions (belongs_to);