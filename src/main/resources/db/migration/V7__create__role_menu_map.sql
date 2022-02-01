CREATE TABLE role_menu_map
(
    id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    parent_id        BIGINT(20) NULL,
    role_id          BIGINT(20) NOT NULL,
    menu_id          BIGINT(20) NOT NULL,
    display_order    INT(3) NOT NULL,
    authorities_json VARCHAR(100) NOT NULL,
    created_by       BIGINT(20) NOT NULL,
    created          DATETIME     NOT NULL,
    updated_by       BIGINT(20) NOT NULL,
    updated          DATETIME     NOT NULL
);
