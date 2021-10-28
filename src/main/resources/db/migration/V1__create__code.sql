CREATE TABLE code
(
    id            BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    type          VARCHAR(100)          NOT NULL,
    value         VARCHAR(100)          NOT NULL,
    text          VARCHAR(1000)         NOT NULL,
    available     BOOLEAN DEFAULT FALSE NOT NULL,
    display_order INT(3) NOT NULL,
    created_by    BIGINT(20) NOT NULL,
    created       DATETIME              NOT NULL,
    updated_by    BIGINT(20) NOT NULL,
    updated       DATETIME              NOT NULL
);
