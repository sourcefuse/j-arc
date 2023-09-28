CREATE SCHEMA main;

SET search_path TO main,public;
GRANT ALL ON SCHEMA main TO public;

-- FUNCTION: main.moddatetime()

-- DROP FUNCTION IF EXISTS main.moddatetime();

CREATE OR REPLACE FUNCTION main.moddatetime()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    NEW.modified_on = now();
    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION main.moddatetime()
    OWNER TO "user";

-- Table: main.auth_clients

-- DROP TABLE IF EXISTS main.auth_clients;

CREATE TABLE IF NOT EXISTS main.auth_clients
(
    id uuid NOT NULL,
    deleted boolean DEFAULT false,
    deleted_by uuid,
    deleted_on timestamp(6) without time zone,
    created_by uuid,
    created_on timestamp(6) without time zone,
    modified_by uuid,
    modified_on timestamp(6) without time zone,
    access_token_expiration bigint NOT NULL,
    auth_code_expiration bigint NOT NULL,
    client_id character varying(255) COLLATE pg_catalog."default",
    client_secret character varying(255) COLLATE pg_catalog."default",
    redirect_url character varying(255) COLLATE pg_catalog."default",
    refresh_token_expiration bigint NOT NULL,
    secret character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT auth_clients_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.auth_clients
    OWNER to "user";

-- Table: main.groups

-- DROP TABLE IF EXISTS main.groups;

CREATE TABLE IF NOT EXISTS main.groups
(
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    description character varying(500) COLLATE pg_catalog."default",
    photo_url character varying(500) COLLATE pg_catalog."default",
    created_by uuid,
    modified_by uuid,
    created_on timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    deleted boolean DEFAULT false,
    deleted_on timestamp with time zone,
    deleted_by uuid,
    tenant_id uuid NOT NULL,
    CONSTRAINT pk_groups_id PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.groups
    OWNER to "user";

    -- Table: main.roles

    -- DROP TABLE IF EXISTS main.roles;

    CREATE TABLE IF NOT EXISTS main.roles
    (
        id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
        name character varying(100) COLLATE pg_catalog."default" NOT NULL,
        created_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
        modified_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
        created_by uuid,
        modified_by uuid,
        deleted boolean NOT NULL DEFAULT false,
        permissions text[] COLLATE pg_catalog."default",
        role_type character varying(100) COLLATE pg_catalog."default" NOT NULL DEFAULT 'ADMIN'::character varying,
        deleted_by uuid,
        deleted_on timestamp with time zone,
        allowed_clients text[] COLLATE pg_catalog."default",
        CONSTRAINT pk_roles_id PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

    ALTER TABLE IF EXISTS main.roles
        OWNER to "user";

    -- Trigger: mdt_roles

    -- DROP TRIGGER IF EXISTS mdt_roles ON main.roles;

    CREATE TRIGGER mdt_roles
        BEFORE UPDATE
        ON main.roles
        FOR EACH ROW
        EXECUTE FUNCTION main.moddatetime('modified_on');


-- Table: main.tenants

-- DROP TABLE IF EXISTS main.tenants;

CREATE TABLE IF NOT EXISTS main.tenants
(
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    status character varying(100) COLLATE pg_catalog."default" NOT NULL DEFAULT 'INACTIVE'::character varying,
    created_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid,
    modified_by uuid,
    deleted boolean NOT NULL DEFAULT false,
    key character varying(20) COLLATE pg_catalog."default" NOT NULL,
    address character varying(500) COLLATE pg_catalog."default",
    city character varying(100) COLLATE pg_catalog."default",
    state character varying(100) COLLATE pg_catalog."default",
    zip character varying(25) COLLATE pg_catalog."default",
    country character varying(25) COLLATE pg_catalog."default",
    deleted_on timestamp with time zone,
    deleted_by uuid,
    primary_contact_email character varying(100) COLLATE pg_catalog."default",
    allowed_domain character varying(100) COLLATE pg_catalog."default",
    tenant_type character varying(100) COLLATE pg_catalog."default",
    website character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT pk_tenants_id PRIMARY KEY (id),
    CONSTRAINT idx_tenants UNIQUE (key)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.tenants
    OWNER to "user";

-- Trigger: mdt_tenants

-- DROP TRIGGER IF EXISTS mdt_tenants ON main.tenants;

CREATE TRIGGER mdt_tenants
    BEFORE UPDATE
    ON main.tenants
    FOR EACH ROW
    EXECUTE FUNCTION main.moddatetime('modified_on');


-- Table: main.tenant_configs

-- DROP TABLE IF EXISTS main.tenant_configs;

CREATE TABLE IF NOT EXISTS main.tenant_configs
(
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    config_key character varying(100) COLLATE pg_catalog."default" NOT NULL,
    config_value jsonb NOT NULL,
    created_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid,
    modified_by uuid,
    deleted boolean NOT NULL DEFAULT false,
    tenant_id uuid NOT NULL,
    deleted_by uuid,
    deleted_on timestamp with time zone,
    CONSTRAINT pk_tenant_configs_id PRIMARY KEY (id),
    CONSTRAINT fk_tenant_configs_tenants FOREIGN KEY (tenant_id)
        REFERENCES main.tenants (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.tenant_configs
    OWNER to "user";

-- Trigger: mdt_tenant_configs

-- DROP TRIGGER IF EXISTS mdt_tenant_configs ON main.tenant_configs;

CREATE TRIGGER mdt_tenant_configs
    BEFORE UPDATE
    ON main.tenant_configs
    FOR EACH ROW
    EXECUTE FUNCTION main.moddatetime('modified_on');

-- Table: main.users

-- DROP TABLE IF EXISTS main.users;

CREATE TABLE IF NOT EXISTS main.users
(
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    first_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    middle_name character varying(50) COLLATE pg_catalog."default",
    last_name character varying(50) COLLATE pg_catalog."default",
    username character varying(150) COLLATE pg_catalog."default" NOT NULL,
    email character varying(150) COLLATE pg_catalog."default",
    phone character varying(15) COLLATE pg_catalog."default",
    created_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid,
    modified_by uuid,
    deleted boolean NOT NULL DEFAULT false,
    last_login timestamp with time zone,
    auth_client_ids uuid[],
    gender character(10) COLLATE pg_catalog."default",
    dob date,
    default_tenant_id uuid,
    deleted_by uuid,
    deleted_on timestamp with time zone,
    designation character varying(50) COLLATE pg_catalog."default",
    photo_url character varying(250) COLLATE pg_catalog."default",
    CONSTRAINT pk_users_id PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.users
    OWNER to "user";


-- Table: main.user_credentials

-- DROP TABLE IF EXISTS main.user_credentials;

CREATE TABLE IF NOT EXISTS main.user_credentials
(
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    user_id uuid NOT NULL,
    auth_provider character varying(50) COLLATE pg_catalog."default" NOT NULL DEFAULT 'internal'::character varying,
    auth_id character varying(100) COLLATE pg_catalog."default",
    auth_token character varying(100) COLLATE pg_catalog."default",
    password character varying(60) COLLATE pg_catalog."default",
    created_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted boolean NOT NULL DEFAULT false,
    deleted_on timestamp with time zone,
    deleted_by uuid,
    secret_key character varying(100) COLLATE pg_catalog."default",
    created_by uuid,
    modified_by uuid,
    CONSTRAINT pk_user_credentials_id PRIMARY KEY (id),
    CONSTRAINT idx_user_credentials_user_id UNIQUE (user_id),
    CONSTRAINT fk_user_credentials_users FOREIGN KEY (user_id)
        REFERENCES main.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.user_credentials
    OWNER to "user";

-- Trigger: mdt_user_credentials

-- DROP TRIGGER IF EXISTS mdt_user_credentials ON main.user_credentials;

CREATE TRIGGER mdt_user_credentials
    BEFORE UPDATE
    ON main.user_credentials
    FOR EACH ROW
    EXECUTE FUNCTION main.moddatetime('modified_on');


-- Table: main.user_tenants

-- DROP TABLE IF EXISTS main.user_tenants;

CREATE TABLE IF NOT EXISTS main.user_tenants
(
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    user_id uuid NOT NULL,
    tenant_id uuid NOT NULL,
    role_id uuid NOT NULL,
    created_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted boolean NOT NULL DEFAULT false,
    status character varying(100) COLLATE pg_catalog."default" NOT NULL DEFAULT 'REGISTERED'::character varying,
    locale character varying(5) COLLATE pg_catalog."default",
    deleted_by uuid,
    deleted_on timestamp with time zone,
    created_by uuid,
    modified_by uuid,
    CONSTRAINT pk_user_tenants_id PRIMARY KEY (id),
    CONSTRAINT fk_user_tenants_roles FOREIGN KEY (role_id)
        REFERENCES main.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_user_tenants_tenants FOREIGN KEY (tenant_id)
        REFERENCES main.tenants (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_user_tenants_users FOREIGN KEY (user_id)
        REFERENCES main.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.user_tenants
    OWNER to "user";


-- Table: main.user_groups

-- DROP TABLE IF EXISTS main.user_groups;

CREATE TABLE IF NOT EXISTS main.user_groups
(
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    user_tenant_id uuid NOT NULL,
    group_id uuid NOT NULL,
    created_on timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    deleted boolean DEFAULT false,
    created_by uuid,
    modified_by uuid,
    deleted_on timestamp with time zone,
    deleted_by uuid,
    is_owner boolean DEFAULT false,
    tenant_id uuid NOT NULL,
    CONSTRAINT pk_user_groups_id PRIMARY KEY (id),
    CONSTRAINT fk_groups FOREIGN KEY (group_id)
        REFERENCES main.groups (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_user_tenant FOREIGN KEY (user_tenant_id)
        REFERENCES main.user_tenants (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.user_groups
    OWNER to "user";

-- Table: main.user_permissions

-- DROP TABLE IF EXISTS main.user_permissions;

CREATE TABLE IF NOT EXISTS main.user_permissions
(
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    user_tenant_id uuid NOT NULL,
    permission character varying(50) COLLATE pg_catalog."default" NOT NULL,
    allowed boolean NOT NULL,
    created_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid,
    modified_by uuid,
    deleted boolean NOT NULL DEFAULT false,
    deleted_on timestamp with time zone,
    deleted_by uuid,
    CONSTRAINT pk_user_permissions_id PRIMARY KEY (id),
    CONSTRAINT fk_user_permissions FOREIGN KEY (user_tenant_id)
        REFERENCES main.user_tenants (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.user_permissions
    OWNER to "user";

-- Trigger: mdt_user_permissions

-- DROP TRIGGER IF EXISTS mdt_user_permissions ON main.user_permissions;

CREATE TRIGGER mdt_user_permissions
    BEFORE UPDATE
    ON main.user_permissions
    FOR EACH ROW
    EXECUTE FUNCTION main.moddatetime('modified_on');

-- Table: main.user_resources

-- DROP TABLE IF EXISTS main.user_resources;

CREATE TABLE IF NOT EXISTS main.user_resources
(
    deleted boolean NOT NULL DEFAULT false,
    deleted_on timestamp with time zone,
    deleted_by uuid,
    created_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid,
    modified_by uuid,
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    user_tenant_id uuid,
    resource_name character varying(50) COLLATE pg_catalog."default",
    resource_value character varying(100) COLLATE pg_catalog."default",
    allowed boolean NOT NULL DEFAULT true,
    CONSTRAINT user_resources_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user_resources FOREIGN KEY (user_tenant_id)
        REFERENCES main.user_tenants (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.user_resources
    OWNER to "user";

-- Table: main.user_tenant_prefs

-- DROP TABLE IF EXISTS main.user_tenant_prefs;

CREATE TABLE IF NOT EXISTS main.user_tenant_prefs
(
    id uuid NOT NULL DEFAULT (md5(((random())::text || (clock_timestamp())::text)))::uuid,
    config_key character varying(100) COLLATE pg_catalog."default" NOT NULL,
    config_value jsonb NOT NULL,
    created_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid,
    modified_by uuid,
    deleted boolean NOT NULL DEFAULT false,
    user_tenant_id uuid NOT NULL,
    deleted_by uuid,
    deleted_on timestamp with time zone,
    CONSTRAINT pk_user_tenant_prefs_id PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS main.user_tenant_prefs
    OWNER to "user";

-- Trigger: mdt_user_tenants

-- DROP TRIGGER IF EXISTS mdt_user_tenants ON main.user_tenants;

CREATE TRIGGER mdt_user_tenants
    BEFORE UPDATE
    ON main.user_tenants
    FOR EACH ROW
    EXECUTE FUNCTION main.moddatetime('modified_on');

-- Trigger: mdt_users

-- DROP TRIGGER IF EXISTS mdt_users ON main.users;

CREATE TRIGGER mdt_users
    BEFORE UPDATE
    ON main.users
    FOR EACH ROW
    EXECUTE FUNCTION main.moddatetime('modified_on');

-- creating views



-- View: main.v_users

-- DROP VIEW main.v_users;

CREATE OR REPLACE VIEW main.v_users
 AS
 SELECT u.id,
    u.first_name,
    u.middle_name,
    u.last_name,
    concat_ws(' '::text, u.first_name,
        CASE
            WHEN u.middle_name::text = ''::text THEN NULL::character varying
            ELSE u.middle_name
        END,
        CASE
            WHEN u.last_name::text = ''::text THEN NULL::character varying
            ELSE u.last_name
        END) AS full_name,
    u.username,
    u.email,
    u.phone,
    ut.created_on,
    ut.modified_on,
    u.created_by,
    u.modified_by,
    ut.deleted,
    ut.deleted_by,
    ut.deleted_on,
    u.last_login,
    u.photo_url,
    u.auth_client_ids,
    u.gender,
    u.dob,
    u.designation,
    u.default_tenant_id,
    ut.tenant_id,
    ut.id AS user_tenant_id,
    ut.role_id,
    ut.status,
    t.name,
    t.key,
    r.name AS rolename,
    r.role_type AS roletype
   FROM main.users u
     JOIN main.user_tenants ut ON u.id = ut.user_id AND u.deleted = false
     JOIN main.tenants t ON t.id = ut.tenant_id
     JOIN main.roles r ON r.id = ut.role_id;

ALTER TABLE main.v_users
    OWNER TO "user";

