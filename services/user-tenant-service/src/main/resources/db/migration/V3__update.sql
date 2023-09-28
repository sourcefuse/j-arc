-- SCHEMA: logs

-- DROP SCHEMA IF EXISTS logs ;

CREATE SCHEMA IF NOT EXISTS logs
    AUTHORIZATION "user";

-- Table: logs.audit_logs

-- DROP TABLE IF EXISTS logs.audit_logs;

CREATE TABLE IF NOT EXISTS logs.audit_logs
(
    id uuid NOT NULL,
    acted_at timestamp(6) without time zone,
    acted_on character varying(255) COLLATE pg_catalog."default",
    action character varying(255) COLLATE pg_catalog."default",
    action_key character varying(255) COLLATE pg_catalog."default",
    actor uuid,
    after jsonb,
    before jsonb,
    entity_id uuid,
    CONSTRAINT audit_logs_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS logs.audit_logs
    OWNER to "user";