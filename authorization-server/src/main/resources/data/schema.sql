START TRANSACTION;

CREATE TABLE IF NOT EXISTS oauth2_registered_client
(
    id                             varchar(100)  NOT NULL,
    client_id                      varchar(100)  NOT NULL,
    client_id_issued_at            timestamp     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret                  varchar(200)  DEFAULT NULL,
    client_secret_expires_at       timestamp     DEFAULT NULL,
    client_name                    varchar(200)  NOT NULL,
    client_authentication_methods  varchar(1000) NOT NULL,
    authorization_grant_types      varchar(1000) NOT NULL,
    redirect_uris                  varchar(1000) DEFAULT NULL,
    post_logout_redirect_uris      varchar(1000) DEFAULT NULL,
    scopes                         varchar(1000) NOT NULL,
    client_settings                varchar(2000) NOT NULL,
    token_settings                 varchar(2000) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id                   BIGINT       AUTO_INCREMENT PRIMARY KEY,
    user_uuid            VARCHAR(255) NOT NULL UNIQUE,
    first_name           VARCHAR(50)  NOT NULL,
    last_name            VARCHAR(50)  NOT NULL,
    email                VARCHAR(100) NOT NULL UNIQUE,
    phone                VARCHAR(30)  DEFAULT NULL,
    bio                  VARCHAR(255) DEFAULT NULL,
    address              VARCHAR(255) DEFAULT NULL,
    reference_id         VARCHAR(255) NOT NULL,
    image_url            VARCHAR(255) DEFAULT 'https://cdn-icons-png.flaticon.com/512/149/149071.png',
    qr_code_secret       VARCHAR(255) DEFAULT NULL,
    qr_code_image_uri    TEXT         DEFAULT NULL,
    last_login           TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    login_attempts       INT          DEFAULT 0,
    mfa                  BOOLEAN      NOT NULL DEFAULT FALSE,
    enabled              BOOLEAN      NOT NULL DEFAULT FALSE,
    account_non_expired  BOOLEAN      NOT NULL DEFAULT FALSE,
    account_non_locked   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at           TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS credentials
(
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    password      VARCHAR(255) NOT NULL,
    reference_id  VARCHAR(255) NOT NULL,
    user_id       BIGINT       NOT NULL UNIQUE,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_credentials_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS confirmations
(
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    token         VARCHAR(255) NOT NULL UNIQUE,
    user_id       BIGINT       NOT NULL UNIQUE,
    reference_id  VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_confirmations_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles
(
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    authorities   VARCHAR(255) NOT NULL,
    reference_id  VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  BIGINT NOT NULL,
    role_id  BIGINT NOT NULL,
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX index_users_email           ON users         (email);
CREATE INDEX index_users_user_uuid       ON users         (user_uuid);
CREATE INDEX index_confirmations_user_id ON confirmations (user_id);
CREATE INDEX index_credentials_user_id   ON credentials   (user_id);
CREATE INDEX index_user_roles_user_id    ON user_roles    (user_id);

COMMIT;
