CREATE TABLE menu
(
    id            BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(1000) NOT NULL,
    type          VARCHAR(10)   NOT NULL,
    parent_id     BIGINT(20),
    display_order INT(3) NOT NULL,
    icon          VARCHAR(50),
    url           VARCHAR(4000),
    created_by    BIGINT(20) NOT NULL,
    created       DATETIME      NOT NULL,
    updated_by    BIGINT(20) NOT NULL,
    updated       DATETIME      NOT NULL
);
