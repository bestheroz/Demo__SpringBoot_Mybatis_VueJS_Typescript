INSERT
INTO SAMPLE_MEMBER_MST
(ID,
 PASSWORD,
 name,
 MEMBER_TYPE,
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
        '999',
        0,
        FALSE,
        SYSDATE + 365,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_MEMBER_MST
(ID,
 PASSWORD,
 name,
 MEMBER_TYPE,
 LOGIN_FAIL_CNT,
 IS_CLOSED,
 TOKEN,
 EXPIRED,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('1',
        '4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a',
        '1',
        '999',
        0,
        FALSE,
        'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiIxMTIwMTkxMTMwOSJ9.KTzZHn6wkr6DQ8e86VuZkGHCYu1lTAdiVew0kk9GHJoez4fOidG7FhrP1SwWEWMkwXZ-CWWVrutE_QGDR8vqbQ',
        SYSDATE + 365,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_MEMBER_MST
(ID,
 PASSWORD,
 name,
 MEMBER_TYPE,
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
        '999',
        0,
        FALSE,
        SYSDATE + 365,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_MEMBER_MST
(ID,
 PASSWORD,
 name,
 MEMBER_TYPE,
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
        '300',
        0,
        FALSE,
        SYSDATE + 365,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 REMARK1,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('Root',
        'G',
        '0',
        TRUE,
        300,
        1,
        '/',
        'MYSPRING 최상위',
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'JS 가이드',
       'G',
       ID,
       TRUE,
       300,
       10,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'Root';


INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'JSP 가이드',
       'G',
       ID,
       TRUE,
       300,
       20,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'Root';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Github',
       'G',
       ID,
       TRUE,
       300,
       70,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'Root';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '관리자',
       'G',
       ID,
       TRUE,
       700,
       110,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'Root';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
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
       700,
       1,
       '/sample/admin/menu/adminMenu.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = '관리자';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
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
       700,
       3,
       '/sample/admin/valuelabel/adminValueLabel.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = '관리자';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '우편번호검색API',
       'P',
       ID,
       TRUE,
       300,
       30,
       '/sample/guide/postcode/sampleGuidePostcode.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
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
       700,
       5,
       '/sample/admin/member/adminMember.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = '관리자';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '모든 프로젝트 보기',
       'P',
       ID,
       TRUE,
       300,
       10,
       'https://github.com/com.github.bestheroz?tab=repositories',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'Github';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Spring_MyBatis_jQuery_Web_Project',
       'P',
       ID,
       TRUE,
       300,
       20,
       'https://github.com/com.github.bestheroz/My_Spring_MyBatis_jQuery_Web_Project',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'Github';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Spring_Mybatis_Swagger_API_Project',
       'P',
       ID,
       TRUE,
       300,
       30,
       'https://github.com/com.github.bestheroz/My_Spring_Mybatis_Swagger_API_Project',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'Github';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'Spring_MyBatis_Swagger_API_Vue_Project',
       'P',
       ID,
       TRUE,
       300,
       40,
       'https://github.com/com.github.bestheroz/My_Spring_MyBatis_Swagger_API_Vue_Project',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'Github';


INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '모달(modal) 띄우기',
       'P',
       ID,
       TRUE,
       300,
       10,
       '/sample/guide/modal/sampleGuideModal.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'ajax 요청하기',
       'P',
       ID,
       TRUE,
       300,
       1,
       '/sample/guide/ajax/sampleGuideAjax.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '팝업(popup)창 띄우기',
       'P',
       ID,
       TRUE,
       300,
       11,
       '/sample/guide/popup/sampleGuidePopup.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '달력 및 시간: datetimepicker',
       'P',
       ID,
       TRUE,
       300,
       40,
       '/sample/guide/datetimepicker/sampleGuideDatetimepicker.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'ajax - 파일다운로드',
       'P',
       ID,
       TRUE,
       300,
       6,
       '/sample/guide/ajax/sampleGuideAjax.view?#list4',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '날짜 처리',
       'P',
       ID,
       TRUE,
       300,
       20,
       '/sample/guide/format/sampleGuideFormat.view?#list1',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '필드 검증하기(필수값 등)',
       'P',
       ID,
       TRUE,
       300,
       15,
       '/sample/guide/validate/field/sampleGuideValidateField.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '값 검증하기',
       'P',
       ID,
       TRUE,
       300,
       16,
       '/sample/guide/validate/value/sampleGuideValidateValue.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '페이징 처리',
       'P',
       ID,
       TRUE,
       300,
       90,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'ajax - 파일업로드',
       'P',
       ID,
       TRUE,
       300,
       5,
       '/sample/guide/ajax/sampleGuideAjax.view?#list2',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '텍스트 에디터',
       'P',
       ID,
       TRUE,
       300,
       60,
       '/sample/guide/texteditor/sampleGuideTexteditor.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'HTML템플릿 - 핸들바JS',
       'P',
       ID,
       TRUE,
       300,
       70,
       '/sample/guide/handlebarsjs/sampleGuideHandlebarsjs.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '숫자 포맷&#40;콤마찍기 등&#41;',
       'P',
       ID,
       TRUE,
       300,
       25,
       '/sample/guide/format/sampleGuideFormat.view?#list2',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JS 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT 'my:html',
       'P',
       ID,
       TRUE,
       300,
       10,
       '/sample/guide/html/sampleGuideHtml.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JSP 가이드';

INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '표준 API 프로젝트',
       'P',
       ID,
       TRUE,
       300,
       30,
       NULL,
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = 'JSP 가이드';


INSERT
INTO SAMPLE_MENU_MST
(NAME,
 TYPE,
 PARENT_ID,
 IS_USING,
 POWER,
 DISPLAY_ORDER,
 URL,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
SELECT '파일관리',
       'P',
       ID,
       TRUE,
       700,
       40,
       '/sample/admin/file/adminFile.view',
       'bestheroz',
       SYSDATE,
       'bestheroz',
       SYSDATE
FROM SAMPLE_MENU_MST
WHERE NAME = '관리자';

INSERT
INTO SAMPLE_CODE_MST
(GROUP_CODE,
 NAME,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('TYPE',
        '메뉴타입',
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_CODE_MST
(GROUP_CODE,
 NAME,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MEMBER_TYPE',
        '회원구분',
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);


INSERT
INTO SAMPLE_CODE_DET
(GROUP_CODE,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('TYPE',
        'G',
        '그룹',
        TRUE,
        1,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_CODE_DET
(GROUP_CODE,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('TYPE',
        'P',
        '페이지',
        TRUE,
        2,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);


INSERT
INTO SAMPLE_CODE_DET
(GROUP_CODE,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MEMBER_TYPE',
        '300',
        '손님',
        TRUE,
        3,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_CODE_DET
(GROUP_CODE,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MEMBER_TYPE',
        '500',
        '일반유저',
        TRUE,
        5,
        'bestheroz',
        SYSDATE,
        'developer',
        SYSDATE);

INSERT
INTO SAMPLE_CODE_DET
(GROUP_CODE,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MEMBER_TYPE',
        '700',
        '관리자',
        TRUE,
        7,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_CODE_DET
(GROUP_CODE,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MEMBER_TYPE',
        '800',
        'Master 관리자',
        TRUE,
        8,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);

INSERT
INTO SAMPLE_CODE_DET
(GROUP_CODE,
 CODE,
 NAME,
 IS_USING,
 DISPLAY_ORDER,
 CREATED_BY,
 CREATED,
 UPDATED_BY,
 UPDATED)
VALUES ('MEMBER_TYPE',
        '999',
        '개발자',
        TRUE,
        10,
        'bestheroz',
        SYSDATE,
        'bestheroz',
        SYSDATE);
