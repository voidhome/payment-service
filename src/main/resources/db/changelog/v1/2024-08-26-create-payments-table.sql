--liquibase formatted sql

--changeset 2024-08-26-create-payments-table:1
CREATE TABLE IF NOT EXISTS payments
(
    id             UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    amount         NUMERIC                  NOT NULL,
    currency       VARCHAR(3)               NOT NULL,
    payment_method VARCHAR(50)              NOT NULL,
    description    VARCHAR(255),
    status         VARCHAR(20)              NOT NULL,
    version        BIGINT                   NOT NULL DEFAULT 0,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
