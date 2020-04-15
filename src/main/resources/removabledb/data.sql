INSERT
INTO MEMBER
(ID,
 PASSWORD,
 NAME,
 AUTHORITY,
 LOGIN_FAIL_CNT,
 IS_CLOSED,
 EXPIRED,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('developer',
        '4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a',
        '개발자',
        999,
        0,
        FALSE,
        SYSDATE + 365,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO MEMBER
(ID,
 PASSWORD,
 NAME,
 AUTHORITY,
 LOGIN_FAIL_CNT,
 IS_CLOSED,
 EXPIRED,
 TOKEN,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('1',
        '4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a',
        '1',
        900,
        0,
        FALSE,
        SYSDATE + 365,
        'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiIxMTIwMTkxMTMwOSJ9.KTzZHn6wkr6DQ8e86VuZkGHCYu1lTAdiVew0kk9GHJoez4fOidG7FhrP1SwWEWMkwXZ-CWWVrutE_QGDR8vqbQ',
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO MEMBER
(ID,
 PASSWORD,
 NAME,
 AUTHORITY,
 LOGIN_FAIL_CNT,
 IS_CLOSED,
 EXPIRED,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('bestheroz',
        '4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a',
        '개발자/김동준M',
        999,
        0,
        FALSE,
        SYSDATE + 365,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO MEMBER
(ID,
 PASSWORD,
 NAME,
 AUTHORITY,
 LOGIN_FAIL_CNT,
 IS_CLOSED,
 EXPIRED,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('guest',
        '4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a',
        '게스트',
        100,
        0,
        FALSE,
        SYSDATE + 365,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 URL,
 REMARK1,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('ROOT',
        'G',
        0,
        TRUE,
        1,
        '/',
        '최상위(삭제하지마세요.)',
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '관리자',
       'G',
       ID,
       TRUE,
       10,
       'mdi-account-cog',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = 'ROOT';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴관리',
       'P',
       ID,
       TRUE,
       100,
       '/admin/menu',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '관리자';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '코드관리',
       'P',
       ID,
       TRUE,
       300,
       '/admin/code',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '관리자';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '회원관리',
       'P',
       ID,
       TRUE,
       500,
       '/admin/member',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '관리자';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '만든이',
       'G',
       ID,
       TRUE,
       90,
       'mdi-account-hard-hat',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = 'ROOT';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Github',
       'P',
       ID,
       TRUE,
       100,
       'https://github.com/com.github.bestheroz?tab=repositories',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '만든이';


INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Blog',
       'P',
       ID,
       TRUE,
       200,
       'https://bestheroz.blog.me',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '만든이';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴그룹1',
       'G',
       ID,
       TRUE,
       30,
       'mdi-numeric-1-box-outline',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = 'ROOT';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴1',
       'P',
       ID,
       TRUE,
       100,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹1';


INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴2',
       'P',
       ID,
       TRUE,
       200,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹1';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴그룹2',
       'G',
       ID,
       TRUE,
       50,
       'mdi-numeric-2-box-outline',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = 'ROOT';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴1',
       'P',
       ID,
       TRUE,
       100,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹2';


INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴2',
       'P',
       ID,
       TRUE,
       200,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹2';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 ICON,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴그룹3',
       'G',
       ID,
       TRUE,
       70,
       'mdi-numeric-3-box-outline',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = 'ROOT';

INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴1',
       'P',
       ID,
       TRUE,
       100,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM MENU
WHERE NAME = '메뉴그룹3';


INSERT
INTO MENU
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '메뉴2',
       'P',
       ID,
       TRUE,
       200,
       'bestheroz',
       SYSDATE,
       'bestheroz',
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
       (SELECT GROUP_CONCAT(CONCAT('^|', ID) separator ',') FROM MENU),
       'bestheroz',
       SYSDATE,
       'bestheroz',
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
        'bestheroz',
        SYSDATE,
        'bestheroz',
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
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);


INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MENU_TYPE',
        'G',
        '그룹',
        TRUE,
        1,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MENU_TYPE',
        'P',
        '페이지',
        TRUE,
        2,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);


INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '100',
        '손님',
        TRUE,
        3,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '500',
        '일반유저',
        TRUE,
        5,
        'bestheroz',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '700',
        '관리자',
        TRUE,
        7,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '900',
        '마스터 관리자',
        TRUE,
        8,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO CODE
(CODE_GROUP,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('AUTHORITY',
        '999',
        '마스터개발자',
        TRUE,
        10,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);
