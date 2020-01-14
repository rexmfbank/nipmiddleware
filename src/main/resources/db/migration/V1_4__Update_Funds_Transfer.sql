
ALTER TABLE funds_transfer_entity ADD COLUMN client_id varchar(255)  not null;

ALTER TABLE funds_transfer_entity ADD CONSTRAINT unique_sessionId UNIQUE (session_id);

ALTER TABLE funds_transfer_entity ADD CONSTRAINT unique_clientId_paymentRef UNIQUE (client_id,payment_reference);

