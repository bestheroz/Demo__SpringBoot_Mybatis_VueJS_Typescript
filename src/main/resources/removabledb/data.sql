INSERT
INTO MEMBER
(ID,
 NAME,
 AUTHORITY,
 LOGIN_FAIL_CNT,
 AVAILABLE,
 EXPIRED,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('developer',
        '개발자',
        999,
        0,
        TRUE,
        SYSDATE + 365,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO MEMBER
(ID,
 PASSWORD,
 NAME,
 AUTHORITY,
 LOGIN_FAIL_CNT,
 AVAILABLE,
 EXPIRED,
 TOKEN,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('1',
        '�ޤ�o%PO�8�.�9�+�Xڹ1����n�ח',
        '1',
        900,
        0,
        TRUE,
        SYSDATE + 365,
        'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiIxMTIwMTkxMTMwOSJ9.KTzZHn6wkr6DQ8e86VuZkGHCYu1lTAdiVew0kk9GHJoez4fOidG7FhrP1SwWEWMkwXZ-CWWVrutE_QGDR8vqbQ',
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO MEMBER
(ID,
 NAME,
 AUTHORITY,
 LOGIN_FAIL_CNT,
 AVAILABLE,
 EXPIRED,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('guest',
        '게스트',
        100,
        0,
        TRUE,
        SYSDATE + 365,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 URL,
 REMARK1,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('///',
        'G',
        0,
        1,
        '/',
        '최상위(삭제하지마세요.)',
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '관리자',
       'G',
       ID,
       10,
       'mdi-account-cog',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '///';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴관리',
       'P',
       ID,
       100,
       '/admin/menu',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '관리자';


INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴권한관리',
       'P',
       ID,
       120,
       '/admin/menuAuthority',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '관리자';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '코드관리',
       'P',
       ID,
       300,
       '/admin/code',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '관리자';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '회원관리',
       'P',
       ID,
       500,
       '/admin/member',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '관리자';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '만든이',
       'G',
       ID,
       90,
       'mdi-account-hard-hat',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '///';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Github',
       'W',
       ID,
       100,
       'https://github.com/bestheroz/',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '만든이';


INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Blog',
       'W',
       ID,
       200,
       'https://bestheroz.blog.me',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '만든이';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴그룹1',
       'G',
       ID,
       30,
       'mdi-numeric-1-box-outline',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '///';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴1',
       'P',
       ID,
       100,
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹1';


INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴2',
       'P',
       ID,
       200,
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹1';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴그룹2',
       'G',
       ID,
       50,
       'mdi-numeric-2-box-outline',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '///';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴1',
       'P',
       ID,
       100,
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹2';


INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴2',
       'P',
       ID,
       200,
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹2';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴그룹3',
       'G',
       ID,
       70,
       'mdi-numeric-3-box-outline',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '///';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴1',
       'P',
       ID,
       100,
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹3';


INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴2',
       'P',
       ID,
       200,
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹3';

INSERT INTO MENU_AUTHORITY
(AUTHORITY, MENU_ID_LIST,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 900,
       (SELECT GROUP_CONCAT(CONCAT('^|', ID, ',') separator '') FROM MENU),
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM DUAL;


INSERT
INTO CODE_GROUP
(CODE_GROUP,
 NAME,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MENU_TYPE',
        '메뉴타입',
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO CODE_GROUP
(CODE_GROUP,
 NAME,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '회원권한',
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);


INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 AVAILABLE,
 DISPLAY_ORDER,
 AUTHORITY,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MENU_TYPE',
        'G',
        '그룹',
        TRUE,
        1,
        100,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 AVAILABLE,
 DISPLAY_ORDER,
 AUTHORITY,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MENU_TYPE',
        'P',
        '페이지',
        TRUE,
        2,
        100,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);


INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 AVAILABLE,
 DISPLAY_ORDER,
 AUTHORITY,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MENU_TYPE',
        'W',
        '새창',
        TRUE,
        3,
        100,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);


INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 AVAILABLE,
 DISPLAY_ORDER,
 AUTHORITY,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '100',
        '손님',
        TRUE,
        3,
        100,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 AVAILABLE,
 DISPLAY_ORDER,
 AUTHORITY,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '500',
        '일반유저',
        TRUE,
        5,
        100,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 AVAILABLE,
 DISPLAY_ORDER,
 AUTHORITY,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '700',
        '관리자',
        TRUE,
        7,
        100,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 AVAILABLE,
 DISPLAY_ORDER,
 AUTHORITY,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '900',
        '마스터 관리자',
        TRUE,
        8,
        100,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 AVAILABLE,
 DISPLAY_ORDER,
 AUTHORITY,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '999',
        '마스터 개발자',
        TRUE,
        10,
        100,
        'developer',
        SYSDATE,
        'developer',
        SYSDATE);
