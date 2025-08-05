CREATE TABLE tb_users
(
    id                      SERIAL PRIMARY KEY,
    user_name               VARCHAR(255) UNIQUE NOT NULL,
    full_name               VARCHAR(255)        NOT NULL,
    email                   VARCHAR(255) UNIQUE NOT NULL,
    phone                   VARCHAR(20)  UNIQUE NOT NULL,
    password                VARCHAR(255)        NOT NULL,
    account_non_expired     BOOLEAN             NOT NULL DEFAULT true,
    account_non_locked      BOOLEAN             NOT NULL DEFAULT true,
    credentials_non_expired BOOLEAN             NOT NULL DEFAULT true,
    enabled                 BOOLEAN             NOT NULL DEFAULT true
);