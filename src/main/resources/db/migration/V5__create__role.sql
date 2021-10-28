CREATE TABLE role
(
    id            BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    parent_id     BIGINT(20) NULL,
    name          VARCHAR(100) NOT NULL,
    available     BOOLEAN      NOT NULL,
    display_order INT(3) NOT NULL,
    created_by    BIGINT(20) NOT NULL,
    created       DATETIME     NOT NULL,
    updated_by    BIGINT(20) NOT NULL,
    updated       DATETIME     NOT NULL
);
