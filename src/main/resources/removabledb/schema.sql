CREATE TABLE MEMBER
(
    ID             VARCHAR(100) PRIMARY KEY NOT NULL,
    PASSWORD       CHAR(128)                NULL,
    NAME           VARCHAR(100)             NOT NULL,
    AUTHORITY      INT(3)                   NOT NULL,
    LOGIN_FAIL_CNT INT(1)  DEFAULT 0        NOT NULL,
    AVAILABLE         BOOLEAN                  NOT NULL,
    TIMEOUT        INT(10) DEFAULT 7200     NOT NULL,
    TOKEN          VARCHAR(1000)             NULL,
    EXPIRED        DATETIME                 NOT NULL,
    CREATED_BY     VARCHAR(100)             NOT NULL,
    CREATED        DATETIME                 NOT NULL,
    UPDATED_BY     VARCHAR(100)             NOT NULL,
    UPDATED        DATETIME                 NOT NULL
);
CREATE TABLE MENU
(
    ID            INT(10) PRIMARY KEY AUTO_INCREMENT,
    NAME          VARCHAR(1000) NOT NULL,
    TYPE          CHAR(1)       NOT NULL,
    PARENT_ID     INT(10)       NOT NULL,
    DISPLAY_ORDER INT(10)       NOT NULL,
    ICON          VARCHAR(50)   NULL,
    URL           VARCHAR(4000),
    CREATED_BY    VARCHAR(100)  NOT NULL,
    CREATED       DATETIME      NOT NULL,
    UPDATED_BY    VARCHAR(100)  NOT NULL,
    UPDATED       DATETIME      NOT NULL
);
CREATE TABLE MENU_AUTHORITY
(
    AUTHORITY    INT(3) PRIMARY KEY NOT NULL,
    MENU_ID_LIST VARCHAR(4000)      NOT NULL,
    CREATED_BY   VARCHAR(100)       NOT NULL,
    CREATED      DATETIME           NOT NULL,
    UPDATED_BY   VARCHAR(100)       NOT NULL,
    UPDATED      DATETIME           NOT NULL
);
CREATE TABLE CODE_GROUP
(
    CODE_GROUP VARCHAR(100) PRIMARY KEY NOT NULL,
    NAME       VARCHAR(1000)            NOT NULL,
    CREATED_BY VARCHAR(100)             NOT NULL,
    CREATED    DATETIME                 NOT NULL,
    UPDATED_BY VARCHAR(100)             NOT NULL,
    UPDATED    DATETIME                 NOT NULL
);

CREATE TABLE CODE
(
    CODE_GROUP    VARCHAR(100)          NOT NULL,
    CODE          VARCHAR(100)          NOT NULL,
    NAME          VARCHAR(1000)         NOT NULL,
    AVAILABLE         BOOLEAN DEFAULT FALSE NOT NULL,
    DISPLAY_ORDER INT(10)               NOT NULL,
    AUTHORITY     INT(3)  DEFAULT 999   NOT NULL,
    CREATED_BY    VARCHAR(100)          NOT NULL,
    CREATED       DATETIME              NOT NULL,
    UPDATED_BY    VARCHAR(100)          NOT NULL,
    UPDATED       DATETIME              NOT NULL
);
ALTER TABLE CODE
    ADD PRIMARY KEY (CODE_GROUP, CODE);

create
sequence hibernate_sequence start
with 1 increment by 1;
