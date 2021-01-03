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
        '개발자1',
        999,
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
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('관리자',
        'G',
        0,
        1,
        'mdi-account-cog',
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
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴관리',
       'P',
       ID,
       2,
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
SELECT '회원메뉴관리',
       'P',
       ID,
       3,
       '/admin/member-menu',
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
       4,
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
       5,
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
VALUES ('만든이',
        'G',
        0,
        6,
        'mdi-account-hard-hat',
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
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Github',
       'W',
       ID,
       7,
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
       8,
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
VALUES ('개발자',
        'G',
        0,
        9,
        'mdi-developer-board',
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
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Picker',
       'P',
       ID,
       10,
       '/developer/picker',
       'developer',
       SYSDATE,
       'developer',
       SYSDATE
FROM MENU
WHERE NAME = '개발자';


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
VALUES ('메뉴그룹1',
        'G',
        0,
        50,
        'mdi-numeric-1-box-outline',
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
VALUES ('메뉴그룹2',
        'G',
        0,
        70,
        'mdi-numeric-2-box-outline',
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
