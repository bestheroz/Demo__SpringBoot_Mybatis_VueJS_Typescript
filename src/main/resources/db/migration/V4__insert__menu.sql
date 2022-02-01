INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (1, '개발자', 'GROUP', null, 5, 'mdi-developer-board', null, 1, NOW(), 1, NOW()),
       (2, 'Picker', 'PAGE', 1, 1, null, '/developer/picker', 1, NOW(), 1, NOW()),
       (3, '파일 업로드', 'PAGE', 1, 2, null, '/developer/file-uploader', 1, NOW(), 1, NOW()),
       (4, '텍스트 에디터', 'PAGE', 1, 3, null, '/developer/text-editor', 1, NOW(), 1, NOW()),

       (5, '관리자 및 역할 관리', 'GROUP', null, 3, 'mdi-account-lock-open', null, 1, NOW(), 1, NOW()),
       (6, '관리자', 'PAGE', 5, 1, null, '/management/admin', 1, NOW(), 1, NOW()),
       (7, '역할', 'PAGE', 5, 2, null, '/management/role', 1, NOW(), 1, NOW()),
       (8, '역할별 메뉴', 'PAGE', 5, 3, null, '/management/role-menu', 1, NOW(), 1, NOW()),

       (9, '시스템 관리', 'GROUP', null, 4, 'mdi-cog', null, 1, NOW(), 1, NOW()),
       (10, '코드', 'PAGE', 9, 1, null, '/management/code', 1, NOW(), 1, NOW()),
       (11, '메뉴', 'PAGE', 9, 2, null, '/management/menu', 1, NOW(), 1, NOW());
