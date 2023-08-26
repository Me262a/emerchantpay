ALTER TABLE merchant ADD COLUMN auth_id VARCHAR(255);

CREATE UNIQUE INDEX idx_merchant_auth_id ON merchant(auth_id);
CREATE UNIQUE INDEX idx_merchant_email ON merchant(email);