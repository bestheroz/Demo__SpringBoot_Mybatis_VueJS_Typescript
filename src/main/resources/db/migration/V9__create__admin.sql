CREATE TABLE admin
(
    id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    admin_id         VARCHAR(100) NOT NULL,
    password         CHAR(128) NULL,
    name             VARCHAR(100) NOT NULL,
    role_id          BIGINT(20) NOT NULL,
    sign_in_fail_cnt INT(1) DEFAULT 0 NOT NULL,
    available        BOOLEAN      NOT NULL,
    token            VARCHAR(4000) NULL,
    expired          DATETIME     NOT NULL,
    created_by       BIGINT(20) NOT NULL,
    created          DATETIME     NOT NULL,
    updated_by       BIGINT(20) NOT NULL,
    updated          DATETIME     NOT NULL
);
