INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (1, '시스템설정', 'GROUP', null, 1, 'mdi-account-cog', null, 1, NOW(), 1, NOW());
INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (2, '코드관리', 'PAGE', 1, 1, null, '/management/code', 1, NOW(), 1, NOW());
INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (3, '관리자관리', 'PAGE', 1, 2, null, '/management/admin', 1, NOW(), 1, NOW());
INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (4, '메뉴관리', 'PAGE', 1, 3, null, '/management/menu', 1, NOW(), 1, NOW());
INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (5, '역할관리', 'PAGE', 1, 4, null, '/management/role', 1, NOW(), 1, NOW());
INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (6, '역할별메뉴관리', 'PAGE', 1, 5, null, '/management/role-menu', 1, NOW(), 1, NOW());
INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (7, '개발자', 'GROUP', null, 2, 'mdi-developer-board', null, 1, NOW(), 1, NOW());
INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (8, 'Picker', 'PAGE', 7, 1, null, '/developer/picker', 1, NOW(), 1, NOW());
INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (9, '파일 업로드', 'PAGE', 7, 2, null, '/developer/file-uploader', 1, NOW(), 1, NOW());
INSERT INTO menu (id, name, type, parent_id, display_order, icon, url, created_by, created,
                  updated_by, updated)
VALUES (10, '텍스트 에디터', 'PAGE', 7, 3, null, '/developer/text-editor', 1, NOW(), 1, NOW());
