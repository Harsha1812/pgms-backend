CREATE TABLE notification_logs (
                                   id UUID PRIMARY KEY,
                                   owner_id UUID NOT NULL,
                                   channel VARCHAR(30) NOT NULL,
                                   reference_id UUID NOT NULL,
                                   status VARCHAR(30) NOT NULL,
                                   error_message TEXT,
                                   created_at TIMESTAMP NOT NULL
);