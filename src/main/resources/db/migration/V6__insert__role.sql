INSERT INTO `role` (id, parent_id, name, available, display_order, created_by, created,
                    updated_by, updated)
VALUES (1, null, '마스터', 1, 1, 1, NOW(), 1, NOW());

INSERT INTO `role` (id, parent_id, name, available, display_order, created_by, created,
                    updated_by, updated)
VALUES (2, null, '관리자', 1, 2, 1, NOW(), 1, NOW());

INSERT INTO `role` (id, parent_id, name, available, display_order, created_by, created,
                    updated_by, updated)
VALUES (3, 2, '쓰기만', 1, 1, 1, NOW(), 1, NOW());

INSERT INTO `role` (id, parent_id, name, available, display_order, created_by, created,
                    updated_by, updated)
VALUES (4, 2, '읽기만', 1, 2, 1, NOW(), 1, NOW());

INSERT INTO `role` (id, parent_id, name, available, display_order, created_by, created,
                    updated_by, updated)
VALUES (5, 2, '엑셀만', 1, 3, 1, NOW(), 1, NOW());

