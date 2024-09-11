--liquibase formatted sql

--changeset 2024-09-04-create-outbox-table:1
CREATE TABLE IF NOT EXISTS outbox_table
(
    id           UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    type         VARCHAR(250)             NOT NULL,
    aggregate_id UUID                     NOT NULL,
    data         BYTEA,
    status       VARCHAR(20)              NOT NULL,
    version      SERIAL                   NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);