-- GitHub-friendly SQL export for hospital_bed_reservation
-- Source: latest local project database export
-- Notes:
-- 1. Local Navicat export metadata has been removed.
-- 2. AUTO_INCREMENT values have been normalized to 1 in table definitions.
-- 3. This file still contains sample/test data from the current project database.




SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admission
-- ----------------------------
DROP TABLE IF EXISTS `admission`;
CREATE TABLE `admission`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint NOT NULL,
  `bed_id` bigint NOT NULL,
  `admission_date` datetime NOT NULL,
  `discharge_date` datetime NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_admission_patient_id`(`patient_id` ASC) USING BTREE,
  INDEX `idx_admission_bed_id`(`bed_id` ASC) USING BTREE,
  CONSTRAINT `fk_admission_bed` FOREIGN KEY (`bed_id`) REFERENCES `bed` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_admission_patient` FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admission
-- ----------------------------
INSERT INTO `admission` VALUES (1, 2, 1, '2025-11-18 00:00:00', NULL, 'in_hospital', '111');
INSERT INTO `admission` VALUES (2, 2, 1, '2025-12-28 00:00:00', '2025-12-28 00:00:00', 'discharged', 'q111');
INSERT INTO `admission` VALUES (101, 103, 105, '2026-04-18 08:30:00', NULL, 'in_hospital', '鏈悗鎭㈠涓紝鐢熷懡浣撳緛骞崇ǔ');
INSERT INTO `admission` VALUES (102, 102, 103, '2026-04-10 09:00:00', '2026-04-17 10:00:00', 'discharged', '鎭㈠鑹ソ锛屽凡鍔炵悊鍑洪櫌');
INSERT INTO `admission` VALUES (301, 303, 306, '2026-05-01 08:00:00', NULL, 'in_hospital', '鎵嬫湳鍚庢仮澶嶏紝鎯呭喌绋冲畾');
INSERT INTO `admission` VALUES (302, 305, 309, '2026-04-28 10:00:00', '2026-05-02 09:00:00', 'discharged', '娌荤枟缁撴潫锛屽凡鍔炵悊鍑洪櫌');
INSERT INTO `admission` VALUES (303, 306, 312, '2026-05-02 14:00:00', NULL, 'in_hospital', '楠ㄧ浣忛櫌搴峰涓?);

-- ----------------------------
-- Table structure for allocation_audit
-- ----------------------------
DROP TABLE IF EXISTS `allocation_audit`;
CREATE TABLE `allocation_audit`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reservation_id` bigint NULL DEFAULT NULL,
  `bed_id` bigint NULL DEFAULT NULL,
  `algorithm` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `score` decimal(8, 2) NULL DEFAULT NULL,
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_allocation_reservation_id`(`reservation_id` ASC) USING BTREE,
  INDEX `idx_allocation_bed_id`(`bed_id` ASC) USING BTREE,
  CONSTRAINT `fk_allocation_bed` FOREIGN KEY (`bed_id`) REFERENCES `bed` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_allocation_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `bed_reservation` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of allocation_audit
-- ----------------------------
INSERT INTO `allocation_audit` VALUES (1, 108, 109, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-02 16:30:38');
INSERT INTO `allocation_audit` VALUES (2, 109, 108, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-08 22:06:19');
INSERT INTO `allocation_audit` VALUES (301, 304, 311, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-03 11:05:00');
INSERT INTO `allocation_audit` VALUES (302, 306, 302, 'DEPARTMENT_PRIORITY_V1', NULL, '棰勭害鍙栨秷鍓嶆浘鑷姩鍒嗛厤搴婁綅锛岀敤浜庢祴璇曞巻鍙插璁′繚鐣?, '2026-05-04 08:32:00');
INSERT INTO `allocation_audit` VALUES (303, 307, 400, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-08 22:11:57');
INSERT INTO `allocation_audit` VALUES (304, 308, 102, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-08 22:18:46');
INSERT INTO `allocation_audit` VALUES (305, 309, 399, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-09 18:35:57');
INSERT INTO `allocation_audit` VALUES (306, 310, 380, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-09 18:44:06');
INSERT INTO `allocation_audit` VALUES (307, 311, 340, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-09 19:13:30');
INSERT INTO `allocation_audit` VALUES (308, 312, 381, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-10 09:01:10');
INSERT INTO `allocation_audit` VALUES (309, 313, 400, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-10 10:54:30');
INSERT INTO `allocation_audit` VALUES (310, 314, 380, 'DEPARTMENT_PRIORITY_V1', NULL, '鎸夌瀹ょ瓫閫夊彲鐢ㄥ簥浣嶏紝骞舵寜搴婁綅ID鍊掑簭浼樺厛鍒嗛厤', '2026-05-10 10:56:40');

-- ----------------------------
-- Table structure for bed
-- ----------------------------
DROP TABLE IF EXISTS `bed`;
CREATE TABLE `bed`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bed_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ward_id` bigint NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `specialties` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `priority` int NULL DEFAULT 0,
  `next_available_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_bed_ward_id`(`ward_id` ASC) USING BTREE,
  INDEX `idx_bed_status_priority`(`status` ASC, `priority` ASC) USING BTREE,
  CONSTRAINT `fk_bed_ward` FOREIGN KEY (`ward_id`) REFERENCES `ward` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bed
-- ----------------------------
INSERT INTO `bed` VALUES (1, '001', 1, '1', '111', NULL, 0, NULL);
INSERT INTO `bed` VALUES (2, '002', 2, '2', '111', NULL, 0, NULL);
INSERT INTO `bed` VALUES (3, '003', 1, '1', '', NULL, 0, NULL);
INSERT INTO `bed` VALUES (4, '004', 1, '1', '', NULL, 0, NULL);
INSERT INTO `bed` VALUES (5, '005', 1, '1', '', NULL, 0, NULL);
INSERT INTO `bed` VALUES (6, '003', 2, '2', '', NULL, 0, NULL);
INSERT INTO `bed` VALUES (7, '004', 2, '2', '', NULL, 0, NULL);
INSERT INTO `bed` VALUES (8, '005', 2, '1', '', NULL, 0, NULL);
INSERT INTO `bed` VALUES (101, 'N101', 101, '1', '绐楄竟搴婁綅', '鍐呯', 10, NULL);
INSERT INTO `bed` VALUES (102, 'N102', 101, '1', '闈犺繎鎶ゅ＋绔?, '鍐呯', 8, NULL);
INSERT INTO `bed` VALUES (103, 'N103', 101, '2', '宸插垎閰嶆祴璇曟偅鑰?, '鍐呯', 6, NULL);
INSERT INTO `bed` VALUES (104, 'S201', 102, '2', '鏈悗瑙傚療搴婁綅', '澶栫', 9, NULL);
INSERT INTO `bed` VALUES (105, 'S202', 102, '2', '姝ｅ湪浣跨敤', '澶栫', 7, NULL);
INSERT INTO `bed` VALUES (106, 'P301', 103, '2', '鍎跨搴婁綅', '鍎跨', 10, NULL);
INSERT INTO `bed` VALUES (107, 'B401', 104, '3', '绛夊緟缁翠慨', '楠ㄧ', 5, '2026-04-28 10:00:00');
INSERT INTO `bed` VALUES (108, 'B402', 104, '1', '搴峰搴婁綅', '楠ㄧ', 7, NULL);
INSERT INTO `bed` VALUES (109, '006', 104, '2', '瀹芥暈', NULL, 0, NULL);
INSERT INTO `bed` VALUES (301, 'I301', 301, '1', '闈犵獥搴婁綅', 'Internal', 20, NULL);
INSERT INTO `bed` VALUES (302, 'I302', 301, '1', '鏅€氬簥浣?, 'Internal', 19, NULL);
INSERT INTO `bed` VALUES (303, 'I303', 301, '2', '宸蹭綇闄㈡偅鑰呬娇鐢ㄤ腑', 'Internal', 18, NULL);
INSERT INTO `bed` VALUES (304, 'I304', 301, '3', '绛夊緟璁惧妫€淇?, 'Internal', 17, '2026-05-10 10:00:00');
INSERT INTO `bed` VALUES (305, 'I305', 301, '1', '蹇冪數鐩戞姢鏃佸簥浣?, 'Internal', 16, NULL);
INSERT INTO `bed` VALUES (306, 'I306', 301, '1', '渚夸簬鎶ょ悊宸¤', 'Internal', 15, NULL);
INSERT INTO `bed` VALUES (307, 'I307', 301, '2', '宸插垎閰嶆偅鑰?, 'Internal', 14, NULL);
INSERT INTO `bed` VALUES (308, 'I308', 301, '1', '鏅€氬簥浣?, 'Internal', 13, NULL);
INSERT INTO `bed` VALUES (309, 'I309', 301, '1', '鏅€氬簥浣?, 'Internal', 12, NULL);
INSERT INTO `bed` VALUES (310, 'I310', 301, '1', '闈犺繎绐楁埛', 'Internal', 11, NULL);
INSERT INTO `bed` VALUES (311, 'I311', 301, '2', '姝ｅ湪娌荤枟涓?, 'Internal', 10, NULL);
INSERT INTO `bed` VALUES (312, 'I312', 301, '1', '绌烘皵娴侀€氳緝濂?, 'Internal', 9, NULL);
INSERT INTO `bed` VALUES (313, 'I313', 301, '1', '渚夸簬瀹跺睘闄姢', 'Internal', 8, NULL);
INSERT INTO `bed` VALUES (314, 'I314', 301, '3', '绛夊緟鏇存崲杈撴恫鏋?, 'Internal', 7, '2026-05-11 14:00:00');
INSERT INTO `bed` VALUES (315, 'I315', 301, '1', '鏅€氬簥浣?, 'Internal', 6, NULL);
INSERT INTO `bed` VALUES (316, 'I316', 301, '1', '鏅€氬簥浣?, 'Internal', 5, NULL);
INSERT INTO `bed` VALUES (317, 'I317', 301, '2', '宸插叆浣?, 'Internal', 4, NULL);
INSERT INTO `bed` VALUES (318, 'I318', 301, '1', '鏅€氬簥浣?, 'Internal', 3, NULL);
INSERT INTO `bed` VALUES (319, 'I319', 301, '1', '鏅€氬簥浣?, 'Internal', 2, NULL);
INSERT INTO `bed` VALUES (320, 'I320', 301, '1', '鏅€氬簥浣?, 'Internal', 1, NULL);
INSERT INTO `bed` VALUES (321, 'S301', 302, '1', '鏈悗瑙傚療搴婁綅', 'Surgery', 20, NULL);
INSERT INTO `bed` VALUES (322, 'S302', 302, '2', '宸插垎閰嶆偅鑰?, 'Surgery', 19, NULL);
INSERT INTO `bed` VALUES (323, 'S303', 302, '1', '鏅€氬绉戝簥浣?, 'Surgery', 18, NULL);
INSERT INTO `bed` VALUES (324, 'S304', 302, '1', '闈犺繎鎶ゅ＋绔?, 'Surgery', 17, NULL);
INSERT INTO `bed` VALUES (325, 'S305', 302, '3', '绛夊緟娑堟瘨瀹屾垚', 'Surgery', 16, '2026-05-10 16:00:00');
INSERT INTO `bed` VALUES (326, 'S306', 302, '1', '鏈悗鎭㈠搴婁綅', 'Surgery', 15, NULL);
INSERT INTO `bed` VALUES (327, 'S307', 302, '2', '姝ｅ湪浣跨敤', 'Surgery', 14, NULL);
INSERT INTO `bed` VALUES (328, 'S308', 302, '1', '鏅€氬绉戝簥浣?, 'Surgery', 13, NULL);
INSERT INTO `bed` VALUES (329, 'S309', 302, '1', '鏅€氬绉戝簥浣?, 'Surgery', 12, NULL);
INSERT INTO `bed` VALUES (330, 'S310', 302, '1', '甯︽姢鏍忓簥浣?, 'Surgery', 11, NULL);
INSERT INTO `bed` VALUES (331, 'S311', 302, '2', '宸插叆浣?, 'Surgery', 10, NULL);
INSERT INTO `bed` VALUES (332, 'S312', 302, '1', '鏅€氬绉戝簥浣?, 'Surgery', 9, NULL);
INSERT INTO `bed` VALUES (333, 'S313', 302, '1', '鏅€氬绉戝簥浣?, 'Surgery', 8, NULL);
INSERT INTO `bed` VALUES (334, 'S314', 302, '3', '璁惧妫€淇?, 'Surgery', 7, '2026-05-12 09:30:00');
INSERT INTO `bed` VALUES (335, 'S315', 302, '1', '鏅€氬绉戝簥浣?, 'Surgery', 6, NULL);
INSERT INTO `bed` VALUES (336, 'S316', 302, '1', '鏅€氬绉戝簥浣?, 'Surgery', 5, NULL);
INSERT INTO `bed` VALUES (337, 'S317', 302, '2', '鎵嬫湳鍚庝綇闄?, 'Surgery', 4, NULL);
INSERT INTO `bed` VALUES (338, 'S318', 302, '1', '鏅€氬绉戝簥浣?, 'Surgery', 3, NULL);
INSERT INTO `bed` VALUES (339, 'S319', 302, '1', '鏅€氬绉戝簥浣?, 'Surgery', 2, NULL);
INSERT INTO `bed` VALUES (340, 'S320', 302, '2', '鏅€氬绉戝簥浣?, 'Surgery', 1, NULL);
INSERT INTO `bed` VALUES (341, 'P201', 303, '1', '鍎跨搴婁綅A', 'Pediatrics', 20, NULL);
INSERT INTO `bed` VALUES (342, 'P202', 303, '2', '鍎跨搴婁綅B', 'Pediatrics', 19, NULL);
INSERT INTO `bed` VALUES (343, 'P203', 303, '1', '鍎跨搴婁綅C', 'Pediatrics', 18, NULL);
INSERT INTO `bed` VALUES (344, 'P204', 303, '1', '闈犵獥鍎跨搴婁綅', 'Pediatrics', 17, NULL);
INSERT INTO `bed` VALUES (345, 'P205', 303, '3', '绛夊緟娓呮磥', 'Pediatrics', 16, '2026-05-09 11:00:00');
INSERT INTO `bed` VALUES (346, 'P206', 303, '1', '鍎跨搴婁綅D', 'Pediatrics', 15, NULL);
INSERT INTO `bed` VALUES (347, 'P207', 303, '2', '浣忛櫌娌荤枟涓?, 'Pediatrics', 14, NULL);
INSERT INTO `bed` VALUES (348, 'P208', 303, '1', '鍎跨搴婁綅E', 'Pediatrics', 13, NULL);
INSERT INTO `bed` VALUES (349, 'P209', 303, '1', '鍎跨搴婁綅F', 'Pediatrics', 12, NULL);
INSERT INTO `bed` VALUES (350, 'P210', 303, '1', '鍎跨搴婁綅G', 'Pediatrics', 11, NULL);
INSERT INTO `bed` VALUES (351, 'P211', 303, '2', '鍎跨宸插垎閰嶅簥浣?, 'Pediatrics', 10, NULL);
INSERT INTO `bed` VALUES (352, 'P212', 303, '1', '鍎跨搴婁綅H', 'Pediatrics', 9, NULL);
INSERT INTO `bed` VALUES (353, 'P213', 303, '1', '鍎跨搴婁綅I', 'Pediatrics', 8, NULL);
INSERT INTO `bed` VALUES (354, 'P214', 303, '3', '绛夊緟鏇存崲搴婂崟', 'Pediatrics', 7, '2026-05-11 08:00:00');
INSERT INTO `bed` VALUES (355, 'P215', 303, '1', '鍎跨搴婁綅J', 'Pediatrics', 6, NULL);
INSERT INTO `bed` VALUES (356, 'P216', 303, '1', '鍎跨搴婁綅K', 'Pediatrics', 5, NULL);
INSERT INTO `bed` VALUES (357, 'P217', 303, '2', '鍎跨宸插叆浣?, 'Pediatrics', 4, NULL);
INSERT INTO `bed` VALUES (358, 'P218', 303, '1', '鍎跨搴婁綅L', 'Pediatrics', 3, NULL);
INSERT INTO `bed` VALUES (359, 'P219', 303, '1', '鍎跨搴婁綅M', 'Pediatrics', 2, NULL);
INSERT INTO `bed` VALUES (360, 'P220', 303, '1', '鍎跨搴婁綅N', 'Pediatrics', 1, NULL);
INSERT INTO `bed` VALUES (361, 'O101', 304, '1', '搴峰搴婁綅A', 'Orthopedics', 20, NULL);
INSERT INTO `bed` VALUES (362, 'O102', 304, '2', '搴峰搴婁綅B', 'Orthopedics', 19, NULL);
INSERT INTO `bed` VALUES (363, 'O103', 304, '3', '缁翠慨涓簥浣?, 'Orthopedics', 18, '2026-05-12 09:00:00');
INSERT INTO `bed` VALUES (364, 'O104', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 17, NULL);
INSERT INTO `bed` VALUES (365, 'O105', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 16, NULL);
INSERT INTO `bed` VALUES (366, 'O106', 304, '2', '楠ㄧ鏈悗搴婁綅', 'Orthopedics', 15, NULL);
INSERT INTO `bed` VALUES (367, 'O107', 304, '1', '搴峰璁粌鏃佸簥浣?, 'Orthopedics', 14, NULL);
INSERT INTO `bed` VALUES (368, 'O108', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 13, NULL);
INSERT INTO `bed` VALUES (369, 'O109', 304, '3', '绛夊緟鏇存崲鎶ゆ爮', 'Orthopedics', 12, '2026-05-13 15:30:00');
INSERT INTO `bed` VALUES (370, 'O110', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 11, NULL);
INSERT INTO `bed` VALUES (371, 'O111', 304, '2', '宸插垎閰嶅悍澶嶆偅鑰?, 'Orthopedics', 10, NULL);
INSERT INTO `bed` VALUES (372, 'O112', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 9, NULL);
INSERT INTO `bed` VALUES (373, 'O113', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 8, NULL);
INSERT INTO `bed` VALUES (374, 'O114', 304, '1', '闈犺繎绐楁埛', 'Orthopedics', 7, NULL);
INSERT INTO `bed` VALUES (375, 'O115', 304, '2', '浣忛櫌娌荤枟涓?, 'Orthopedics', 6, NULL);
INSERT INTO `bed` VALUES (376, 'O116', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 5, NULL);
INSERT INTO `bed` VALUES (377, 'O117', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 4, NULL);
INSERT INTO `bed` VALUES (378, 'O118', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 3, NULL);
INSERT INTO `bed` VALUES (379, 'O119', 304, '2', '鏈悗鎭㈠涓?, 'Orthopedics', 2, NULL);
INSERT INTO `bed` VALUES (380, 'O120', 304, '1', '楠ㄧ鏅€氬簥浣?, 'Orthopedics', 1, NULL);
INSERT INTO `bed` VALUES (381, 'R101', 305, '1', '鍛煎惛鍐呯搴婁綅A', 'Respiratory', 20, NULL);
INSERT INTO `bed` VALUES (382, 'R102', 305, '1', '鍛煎惛鍐呯搴婁綅B', 'Respiratory', 19, NULL);
INSERT INTO `bed` VALUES (383, 'R103', 305, '2', '姘х枟瑙傚療搴婁綅', 'Respiratory', 18, NULL);
INSERT INTO `bed` VALUES (384, 'R104', 305, '1', '鍛煎惛鍐呯搴婁綅C', 'Respiratory', 17, NULL);
INSERT INTO `bed` VALUES (385, 'R105', 305, '1', '鍛煎惛鍐呯搴婁綅D', 'Respiratory', 16, NULL);
INSERT INTO `bed` VALUES (386, 'R106', 305, '3', '绛夊緟璁惧缁存姢', 'Respiratory', 15, '2026-05-10 09:00:00');
INSERT INTO `bed` VALUES (387, 'R107', 305, '1', '鍛煎惛鍐呯搴婁綅E', 'Respiratory', 14, NULL);
INSERT INTO `bed` VALUES (388, 'R108', 305, '2', '姝ｅ湪娌荤枟涓?, 'Respiratory', 13, NULL);
INSERT INTO `bed` VALUES (389, 'R109', 305, '1', '鍛煎惛鍐呯搴婁綅F', 'Respiratory', 12, NULL);
INSERT INTO `bed` VALUES (390, 'R110', 305, '1', '鍛煎惛鍐呯搴婁綅G', 'Respiratory', 11, NULL);
INSERT INTO `bed` VALUES (391, 'R111', 305, '1', '鍛煎惛鍐呯搴婁綅H', 'Respiratory', 10, NULL);
INSERT INTO `bed` VALUES (392, 'R112', 305, '2', '宸蹭綇闄㈡偅鑰呬娇鐢ㄤ腑', 'Respiratory', 9, NULL);
INSERT INTO `bed` VALUES (393, 'R113', 305, '1', '鍛煎惛鍐呯搴婁綅I', 'Respiratory', 8, NULL);
INSERT INTO `bed` VALUES (394, 'R114', 305, '1', '鍛煎惛鍐呯搴婁綅J', 'Respiratory', 7, NULL);
INSERT INTO `bed` VALUES (395, 'R115', 305, '3', '绛夊緟绌烘皵鍑€鍖栨淇?, 'Respiratory', 6, '2026-05-11 10:30:00');
INSERT INTO `bed` VALUES (396, 'R116', 305, '1', '鍛煎惛鍐呯搴婁綅K', 'Respiratory', 5, NULL);
INSERT INTO `bed` VALUES (397, 'R117', 305, '1', '鍛煎惛鍐呯搴婁綅L', 'Respiratory', 4, NULL);
INSERT INTO `bed` VALUES (398, 'R118', 305, '2', '浣忛櫌娌荤枟涓?, 'Respiratory', 3, NULL);
INSERT INTO `bed` VALUES (399, 'R119', 305, '1', '鍛煎惛鍐呯搴婁綅M', 'Respiratory', 2, NULL);
INSERT INTO `bed` VALUES (400, 'R120', 305, '2', '鍛煎惛鍐呯搴婁綅N', 'Respiratory', 1, NULL);

-- ----------------------------
-- Table structure for bed_pool_members
-- ----------------------------
DROP TABLE IF EXISTS `bed_pool_members`;
CREATE TABLE `bed_pool_members`  (
  `pool_id` bigint NOT NULL,
  `bed_id` bigint NOT NULL,
  PRIMARY KEY (`pool_id`, `bed_id`) USING BTREE,
  INDEX `idx_bed_pool_members_bed_id`(`bed_id` ASC) USING BTREE,
  CONSTRAINT `fk_bed_pool_members_bed` FOREIGN KEY (`bed_id`) REFERENCES `bed` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_bed_pool_members_pool` FOREIGN KEY (`pool_id`) REFERENCES `bed_pools` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bed_pool_members
-- ----------------------------

-- ----------------------------
-- Table structure for bed_pools
-- ----------------------------
DROP TABLE IF EXISTS `bed_pools`;
CREATE TABLE `bed_pools`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_bed_pools_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bed_pools
-- ----------------------------

-- ----------------------------
-- Table structure for bed_reservation
-- ----------------------------
DROP TABLE IF EXISTS `bed_reservation`;
CREATE TABLE `bed_reservation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint NOT NULL,
  `ward_id` bigint NULL DEFAULT NULL,
  `bed_id` bigint NULL DEFAULT NULL,
  `reservation_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `audit_user_id` bigint NULL DEFAULT NULL,
  `audit_time` datetime NULL DEFAULT NULL,
  `allocation_mode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'MANUAL',
  `priority` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_reservation_patient_id`(`patient_id` ASC) USING BTREE,
  INDEX `idx_reservation_ward_id`(`ward_id` ASC) USING BTREE,
  INDEX `idx_reservation_bed_time_status`(`bed_id` ASC, `reservation_time` ASC, `status` ASC) USING BTREE,
  CONSTRAINT `fk_reservation_bed` FOREIGN KEY (`bed_id`) REFERENCES `bed` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reservation_patient` FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reservation_ward` FOREIGN KEY (`ward_id`) REFERENCES `ward` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bed_reservation
-- ----------------------------
INSERT INTO `bed_reservation` VALUES (1, 2, 1, 1, '2025-11-20 00:00:00', '2', '111', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (3, 2, 1, 1, '2025-11-28 00:00:00', '1', 'test', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (6, 2, 1, 1, '2025-11-28 00:00:00', '1', '111', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (7, 2, 1, 1, '2025-11-29 00:00:00', '3', '123', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (8, 2, 2, 2, '2025-12-28 00:00:00', '2', '111', 1, '2026-01-12 19:27:11', 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (9, 2, 2, 2, '2026-01-14 20:43:57', '3', '111', 1, '2026-01-14 20:50:33', 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (10, 2, 1, NULL, '2026-01-28 00:00:00', '3', '111', 1, '2026-01-14 20:50:31', 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (11, 2, 2, NULL, '2026-01-15 00:00:00', '1', '111', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (101, 102, 101, 101, '2026-04-20 09:00:00', '1', '鎸佺画鍙戠儹锛岄渶瑕佸畨鎺掍綇闄㈣瀵?, NULL, NULL, 'MANUAL', 2);
INSERT INTO `bed_reservation` VALUES (102, 103, 102, 104, '2026-04-20 10:30:00', '2', '闃戝熬鐐庢湳鍓嶅緟搴?, 101, '2026-04-20 11:00:00', 'MANUAL', 3);
INSERT INTO `bed_reservation` VALUES (103, 104, 103, 106, '2026-04-21 14:00:00', '3', '鍎跨鎬ユ€ф敮姘旂鐐庣敵璇蜂綇闄?, 101, '2026-04-21 15:00:00', 'MANUAL', 1);
INSERT INTO `bed_reservation` VALUES (104, 102, 104, 108, '2026-04-22 16:00:00', '1', '楠ㄦ姌鏈悗搴峰娌荤枟', NULL, NULL, 'MANUAL', 2);
INSERT INTO `bed_reservation` VALUES (105, 2, 104, 108, '2026-04-27 00:00:00', '1', '娴嬭瘯', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (106, 2, 102, 104, '2026-04-27 00:00:00', '2', '娴嬭瘯2', 1, '2026-05-02 16:29:23', 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (107, 105, 103, 106, '2026-04-27 00:00:00', '2', 'test', 1, '2026-04-26 17:12:03', 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (108, 2, 104, 109, '2026-05-03 00:00:00', '2', '楠ㄦ姌闇€鐪嬫姢', 1, '2026-05-02 16:31:03', 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (109, 104, 104, 108, '2026-05-30 00:00:00', '1', '111', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (301, 302, 301, 301, '2026-05-03 09:30:00', '1', '楂樿鍘嬩即鑳搁椃锛岄渶瑕佷綇闄㈣瀵?, NULL, NULL, 'MANUAL', 2);
INSERT INTO `bed_reservation` VALUES (302, 303, 302, 305, '2026-05-03 10:00:00', '2', '闃戝熬鐐庢湳鍓嶅緟搴?, 301, '2026-05-03 10:20:00', 'MANUAL', 3);
INSERT INTO `bed_reservation` VALUES (303, 304, 303, 308, '2026-05-03 10:30:00', '3', '鍎跨鍙戠儹鍙嶅锛岄渶瑕佷綇闄㈡不鐤?, 301, '2026-05-03 10:50:00', 'MANUAL', 1);
INSERT INTO `bed_reservation` VALUES (304, 305, 304, NULL, '2026-05-03 11:00:00', '1', '楠ㄦ姌鏈悗鎭㈠棰勭害搴婁綅', NULL, NULL, 'AUTO', 2);
INSERT INTO `bed_reservation` VALUES (305, 306, 305, 314, '2026-05-03 11:20:00', '2', '鑲洪儴鎰熸煋锛岄渶姘х枟瑙傚療', 301, '2026-05-03 11:40:00', 'MANUAL', 2);
INSERT INTO `bed_reservation` VALUES (306, 302, 301, NULL, '2026-05-04 08:30:00', '4', '涓汉鍘熷洜鍙栨秷棰勭害', NULL, NULL, 'AUTO', 1);
INSERT INTO `bed_reservation` VALUES (307, 306, 305, 400, '2026-05-09 00:00:00', '1', '11', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (308, 107, 101, 102, '2026-05-09 22:21:46', '3', '骞跺彂娴嬭瘯棰勭害-3', 1, '2026-05-09 21:53:18', 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (309, 306, 305, 399, '2026-05-16 00:00:00', '1', '1111', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (310, 2, 304, 380, '2026-05-30 00:00:00', '1', '111', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (311, 2, 302, 340, '2026-05-16 00:00:00', '2', '鎵嬫湳鎭㈠', 1, '2026-05-09 19:13:44', 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (312, 102, 305, 381, '2026-05-11 00:00:00', '1', '111', NULL, NULL, 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (313, 2, 305, 400, '2026-05-11 00:00:00', '2', 'twest', 1, '2026-05-10 10:54:47', 'MANUAL', 0);
INSERT INTO `bed_reservation` VALUES (314, 305, 304, 380, '2026-05-11 00:00:00', '1', '111', NULL, NULL, 'MANUAL', 0);

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sender_id` bigint NULL DEFAULT NULL,
  `receiver_id` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `is_read` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_notification_sender_id`(`sender_id` ASC) USING BTREE,
  INDEX `idx_notification_receiver_id`(`receiver_id` ASC) USING BTREE,
  CONSTRAINT `fk_notification_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_notification_sender` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification
-- ----------------------------
INSERT INTO `notification` VALUES (1, 'New reservation request', 'User test requested ward test1 bed 001 at 2025-11-22T00:00, reason: 111', 2, 1, '2025-11-22 14:39:18', 0);
INSERT INTO `notification` VALUES (2, 'New reservation request', 'User test requested ward test1 bed 001 at 2025-11-28T00:00, reason: test', 2, 1, '2025-11-22 14:49:18', 0);
INSERT INTO `notification` VALUES (3, 'New reservation request', 'User test requested ward test1 bed 001 at 2025-11-28T00:00, reason: 1 or 1=1', 2, 1, '2025-11-22 14:51:14', 0);
INSERT INTO `notification` VALUES (4, 'New reservation request', 'User admin requested ward test1 bed 001 at 2025-11-21T00:00, reason: 111', 1, 1, '2025-11-22 14:59:05', 0);
INSERT INTO `notification` VALUES (7, 'New reservation request', 'User test requested ward test2 bed 002 at 2025-12-28T00:00, reason: 111', 2, 1, '2025-12-28 10:28:14', 0);
INSERT INTO `notification` VALUES (8, 'Reservation approved', 'Your reservation has been approved. Ward test2 bed 002 at 2025-12-28T00:00', 1, 2, '2026-01-12 19:27:11', 1);
INSERT INTO `notification` VALUES (9, 'New reservation request', 'User test requested ward test2 bed 002 at 2026-01-14T20:43:57, reason: 111', 2, 1, '2026-01-14 20:44:12', 0);
INSERT INTO `notification` VALUES (10, 'New reservation request', 'User test requested ward test1 with auto allocation at 2026-01-28T00:00, reason: 111', 2, 1, '2026-01-14 20:48:56', 0);
INSERT INTO `notification` VALUES (11, 'Reservation rejected', 'Your reservation request was rejected. Please contact the administrator if needed.', 1, 2, '2026-01-14 20:50:31', 1);
INSERT INTO `notification` VALUES (12, 'Reservation rejected', 'Your reservation request was rejected. Please contact the administrator if needed.', 1, 2, '2026-01-14 20:50:33', 1);
INSERT INTO `notification` VALUES (13, 'New reservation request', 'User test requested ward test2 with auto allocation at 2026-01-15T00:00, reason: 111', 2, 1, '2026-01-15 20:16:51', 0);
INSERT INTO `notification` VALUES (101, '鏂扮殑搴婁綅棰勭害鐢宠', '寮犱笁鎻愪氦浜嗗唴绉戜竴鐥呭尯搴婁綅棰勭害鐢宠锛岃灏藉揩瀹℃牳銆?, 102, 101, '2026-04-20 09:05:00', 0);
INSERT INTO `notification` VALUES (102, '搴婁綅棰勭害瀹℃牳閫氳繃', '鎮ㄧ殑澶栫浜岀梾鍖哄簥浣嶉绾﹀凡瀹℃牳閫氳繃锛岃鎸夋椂鍔炵悊浣忛櫌鎵嬬画銆?, 101, 103, '2026-04-20 11:05:00', 1);
INSERT INTO `notification` VALUES (103, '搴婁綅棰勭害瀹℃牳鏈€氳繃', '鎮ㄧ殑鍎跨鐥呭尯搴婁綅棰勭害鏈€氳繃瀹℃牳锛屽鏈夌枒闂鑱旂郴绠＄悊鍛樸€?, 101, 104, '2026-04-21 15:05:00', 0);
INSERT INTO `notification` VALUES (104, '搴婁綅閲婃斁鎻愰啋', '鍐呯涓€鐥呭尯 N103 搴婁綅宸查噴鏀撅紝鍙噸鏂板畨鎺掓柊鐨勯绾︺€?, 101, 101, '2026-04-22 08:00:00', 0);
INSERT INTO `notification` VALUES (105, '绯荤粺缁存姢閫氱煡', '浠婃櫄22:00鑷?3:00杩涜绯荤粺缁存姢锛屾湡闂村簥浣嶉绾﹀姛鑳藉彲鑳界煭鏆備笉鍙敤銆?, 101, 102, '2026-04-22 12:00:00', 0);
INSERT INTO `notification` VALUES (106, '鍏ヤ綇鍔炵悊鎻愰啋', '鎮ㄧ殑搴婁綅宸查鐣欐垚鍔燂紝璇蜂簬鏄庢棩涓婂崍涔濈偣鍓嶅埌浣忛櫌澶勫姙鐞嗗叆浣忋€?, 101, 102, '2026-04-22 18:30:00', 0);
INSERT INTO `notification` VALUES (107, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 楠ㄧ搴峰鐥呭尯 鐨勫簥浣?B402锛岄绾︽椂闂达細2026-04-27T00:00锛屽師鍥狅細娴嬭瘯', 2, 1, '2026-04-26 17:00:24', 0);
INSERT INTO `notification` VALUES (108, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 楠ㄧ搴峰鐥呭尯 鐨勫簥浣?B402锛岄绾︽椂闂达細2026-04-27T00:00锛屽師鍥狅細娴嬭瘯', 2, 101, '2026-04-26 17:00:24', 0);
INSERT INTO `notification` VALUES (109, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 澶栫浜岀梾鍖?鐨勫簥浣?S201锛岄绾︽椂闂达細2026-04-27T00:00锛屽師鍥狅細娴嬭瘯2', 2, 1, '2026-04-26 17:01:22', 0);
INSERT INTO `notification` VALUES (110, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 澶栫浜岀梾鍖?鐨勫簥浣?S201锛岄绾︽椂闂达細2026-04-27T00:00锛屽師鍥狅細娴嬭瘯2', 2, 101, '2026-04-26 17:01:22', 0);
INSERT INTO `notification` VALUES (111, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 涓佷竴 鐢宠棰勭害 鍎跨鐥呭尯 鐨勫簥浣?P301锛岄绾︽椂闂达細2026-04-27T00:00锛屽師鍥狅細test', 105, 1, '2026-04-26 17:08:38', 0);
INSERT INTO `notification` VALUES (112, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 涓佷竴 鐢宠棰勭害 鍎跨鐥呭尯 鐨勫簥浣?P301锛岄绾︽椂闂达細2026-04-27T00:00锛屽師鍥狅細test', 105, 101, '2026-04-26 17:08:38', 0);
INSERT INTO `notification` VALUES (113, '搴婁綅棰勭害瀹℃牳閫氳繃', '鎮ㄧ殑搴婁綅棰勭害鐢宠宸查€氳繃瀹℃牳銆傞绾︿俊鎭細鍎跨鐥呭尯 鐨勫簥浣?P301锛岄绾︽椂闂达細2026-04-27T00:00', 1, 105, '2026-04-26 17:12:03', 0);
INSERT INTO `notification` VALUES (114, '搴婁綅棰勭害瀹℃牳閫氳繃', '鎮ㄧ殑搴婁綅棰勭害鐢宠宸查€氳繃瀹℃牳銆傞绾︿俊鎭細澶栫浜岀梾鍖?鐨勫簥浣?S201锛岄绾︽椂闂达細2026-04-27T00:00', 1, 2, '2026-05-02 16:29:23', 1);
INSERT INTO `notification` VALUES (115, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 楠ㄧ搴峰鐥呭尯 鐨勫簥浣?006锛岄绾︽椂闂达細2026-05-03T00:00锛屽師鍥狅細楠ㄦ姌闇€鐪嬫姢', 2, 1, '2026-05-02 16:30:38', 0);
INSERT INTO `notification` VALUES (116, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 楠ㄧ搴峰鐥呭尯 鐨勫簥浣?006锛岄绾︽椂闂达細2026-05-03T00:00锛屽師鍥狅細楠ㄦ姌闇€鐪嬫姢', 2, 101, '2026-05-02 16:30:38', 0);
INSERT INTO `notification` VALUES (117, '搴婁綅棰勭害瀹℃牳閫氳繃', '鎮ㄧ殑搴婁綅棰勭害鐢宠宸查€氳繃瀹℃牳銆傞绾︿俊鎭細楠ㄧ搴峰鐥呭尯 鐨勫簥浣?006锛岄绾︽椂闂达細2026-05-03T00:00', 1, 2, '2026-05-02 16:31:03', 1);
INSERT INTO `notification` VALUES (118, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 楠ㄧ搴峰鐥呭尯 鐨勫簥浣?B402锛岄绾︽椂闂达細2026-05-30T00:00锛屽師鍥狅細111', 1, 1, '2026-05-08 22:06:19', 0);
INSERT INTO `notification` VALUES (119, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 楠ㄧ搴峰鐥呭尯 鐨勫簥浣?B402锛岄绾︽椂闂达細2026-05-30T00:00锛屽師鍥狅細111', 1, 101, '2026-05-08 22:06:19', 0);
INSERT INTO `notification` VALUES (301, '鏂扮殑搴婁綅棰勭害鐢宠', '璧靛叚鎻愪氦浜嗗唴绉戜笁鐥呭尯搴婁綅棰勭害鐢宠锛岃鍙婃椂瀹℃牳銆?, 302, 301, '2026-05-03 09:35:00', 0);
INSERT INTO `notification` VALUES (302, '搴婁綅棰勭害瀹℃牳閫氳繃', '鎮ㄧ殑澶栫涓夌梾鍖哄簥浣嶉绾﹀凡閫氳繃瀹℃牳锛岃鎸夋椂鍔炵悊浣忛櫌鎵嬬画銆?, 301, 303, '2026-05-03 10:25:00', 1);
INSERT INTO `notification` VALUES (303, '搴婁綅棰勭害瀹℃牳鏈€氳繃', '鎮ㄧ殑鍎跨浜岀梾鍖哄簥浣嶉绾︽湭閫氳繃瀹℃牳锛岃鑱旂郴绠＄悊鍛樹簡瑙ｈ鎯呫€?, 301, 304, '2026-05-03 10:55:00', 0);
INSERT INTO `notification` VALUES (304, '鑷姩鍒嗛厤鎴愬姛鎻愰啋', '绯荤粺宸蹭负鎮ㄨ嚜鍔ㄥ垎閰嶉绉戜竴鐥呭尯搴婁綅锛岃鏌ョ湅棰勭害璇︽儏銆?, 301, 305, '2026-05-03 11:10:00', 0);
INSERT INTO `notification` VALUES (305, '搴婁綅璧勬簮鏇存柊閫氱煡', '鍛煎惛鍐呯鐥呭尯鏂板鍙敤搴婁綅锛岃绠＄悊鍛樺叧娉ㄥ簥浣嶅畨鎺掋€?, 301, 301, '2026-05-03 12:00:00', 0);
INSERT INTO `notification` VALUES (306, '棰勭害鍙栨秷閫氱煡', '鎮ㄧ殑棰勭害宸叉垚鍔熷彇娑堬紝濡傞渶鍐嶆浣忛櫌璇烽噸鏂版彁浜ら绾︾敵璇枫€?, 301, 302, '2026-05-04 08:40:00', 1);
INSERT INTO `notification` VALUES (307, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R120锛岄绾︽椂闂达細2026-05-09T00:00锛屽師鍥狅細11', 1, 1, '2026-05-08 22:11:57', 0);
INSERT INTO `notification` VALUES (308, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R120锛岄绾︽椂闂达細2026-05-09T00:00锛屽師鍥狅細11', 1, 101, '2026-05-08 22:11:57', 0);
INSERT INTO `notification` VALUES (309, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R120锛岄绾︽椂闂达細2026-05-09T00:00锛屽師鍥狅細11', 1, 301, '2026-05-08 22:11:57', 0);
INSERT INTO `notification` VALUES (310, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 骞跺彂娴嬭瘯 鐢宠棰勭害 鍐呯涓€鐥呭尯 鐨勫簥浣?N102锛岄绾︽椂闂达細2026-05-09T22:21:46锛屽師鍥狅細骞跺彂娴嬭瘯棰勭害-3', 107, 1, '2026-05-08 22:18:47', 0);
INSERT INTO `notification` VALUES (311, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 骞跺彂娴嬭瘯 鐢宠棰勭害 鍐呯涓€鐥呭尯 鐨勫簥浣?N102锛岄绾︽椂闂达細2026-05-09T22:21:46锛屽師鍥狅細骞跺彂娴嬭瘯棰勭害-3', 107, 101, '2026-05-08 22:18:47', 0);
INSERT INTO `notification` VALUES (312, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 骞跺彂娴嬭瘯 鐢宠棰勭害 鍐呯涓€鐥呭尯 鐨勫簥浣?N102锛岄绾︽椂闂达細2026-05-09T22:21:46锛屽師鍥狅細骞跺彂娴嬭瘯棰勭害-3', 107, 301, '2026-05-08 22:18:47', 0);
INSERT INTO `notification` VALUES (313, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R119锛岄绾︽椂闂达細2026-05-16T00:00锛屽師鍥狅細1111', 1, 1, '2026-05-09 18:35:57', 0);
INSERT INTO `notification` VALUES (314, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R119锛岄绾︽椂闂达細2026-05-16T00:00锛屽師鍥狅細1111', 1, 101, '2026-05-09 18:35:57', 0);
INSERT INTO `notification` VALUES (315, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R119锛岄绾︽椂闂达細2026-05-16T00:00锛屽師鍥狅細1111', 1, 301, '2026-05-09 18:35:57', 0);
INSERT INTO `notification` VALUES (316, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 楠ㄧ涓€鐥呭尯 鐨勫簥浣?O120锛岄绾︽椂闂达細2026-05-30T00:00锛屽師鍥狅細111', 2, 1, '2026-05-09 18:44:06', 0);
INSERT INTO `notification` VALUES (317, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 楠ㄧ涓€鐥呭尯 鐨勫簥浣?O120锛岄绾︽椂闂达細2026-05-30T00:00锛屽師鍥狅細111', 2, 101, '2026-05-09 18:44:06', 0);
INSERT INTO `notification` VALUES (318, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 楠ㄧ涓€鐥呭尯 鐨勫簥浣?O120锛岄绾︽椂闂达細2026-05-30T00:00锛屽師鍥狅細111', 2, 301, '2026-05-09 18:44:06', 0);
INSERT INTO `notification` VALUES (319, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 澶栫涓夌梾鍖?鐨勫簥浣?S320锛岄绾︽椂闂达細2026-05-16T00:00锛屽師鍥狅細鎵嬫湳鎭㈠', 2, 1, '2026-05-09 19:13:30', 0);
INSERT INTO `notification` VALUES (320, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 澶栫涓夌梾鍖?鐨勫簥浣?S320锛岄绾︽椂闂达細2026-05-16T00:00锛屽師鍥狅細鎵嬫湳鎭㈠', 2, 101, '2026-05-09 19:13:30', 0);
INSERT INTO `notification` VALUES (321, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 澶栫涓夌梾鍖?鐨勫簥浣?S320锛岄绾︽椂闂达細2026-05-16T00:00锛屽師鍥狅細鎵嬫湳鎭㈠', 2, 301, '2026-05-09 19:13:30', 0);
INSERT INTO `notification` VALUES (322, '搴婁綅棰勭害瀹℃牳閫氳繃', '鎮ㄧ殑搴婁綅棰勭害鐢宠宸查€氳繃瀹℃牳銆傞绾︿俊鎭細澶栫涓夌梾鍖?鐨勫簥浣?S320锛岄绾︽椂闂达細2026-05-16T00:00', 1, 2, '2026-05-09 19:13:44', 0);
INSERT INTO `notification` VALUES (323, '搴婁綅棰勭害瀹℃牳鏈€氳繃', '寰堟姳姝夛紝鎮ㄧ殑搴婁綅棰勭害鐢宠鏈€氳繃瀹℃牳銆傚鏈夌枒闂紝璇疯仈绯荤鐞嗗憳銆?, 1, 107, '2026-05-09 21:53:18', 0);
INSERT INTO `notification` VALUES (324, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R101锛岄绾︽椂闂达細2026-05-11T00:00锛屽師鍥狅細111', 1, 1, '2026-05-10 09:01:11', 0);
INSERT INTO `notification` VALUES (325, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R101锛岄绾︽椂闂达細2026-05-11T00:00锛屽師鍥狅細111', 1, 101, '2026-05-10 09:01:11', 0);
INSERT INTO `notification` VALUES (326, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R101锛岄绾︽椂闂达細2026-05-11T00:00锛屽師鍥狅細111', 1, 301, '2026-05-10 09:01:11', 0);
INSERT INTO `notification` VALUES (327, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R120锛岄绾︽椂闂达細2026-05-11T00:00锛屽師鍥狅細twest', 2, 1, '2026-05-10 10:54:30', 0);
INSERT INTO `notification` VALUES (328, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R120锛岄绾︽椂闂达細2026-05-11T00:00锛屽師鍥狅細twest', 2, 101, '2026-05-10 10:54:30', 0);
INSERT INTO `notification` VALUES (329, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 test 鐢宠棰勭害 鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R120锛岄绾︽椂闂达細2026-05-11T00:00锛屽師鍥狅細twest', 2, 301, '2026-05-10 10:54:30', 0);
INSERT INTO `notification` VALUES (330, '搴婁綅棰勭害瀹℃牳閫氳繃', '鎮ㄧ殑搴婁綅棰勭害鐢宠宸查€氳繃瀹℃牳銆傞绾︿俊鎭細鍛煎惛鍐呯鐥呭尯 鐨勫簥浣?R120锛岄绾︽椂闂达細2026-05-11T00:00', 1, 2, '2026-05-10 10:54:47', 1);
INSERT INTO `notification` VALUES (331, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 楠ㄧ涓€鐥呭尯 鐨勫簥浣?O120锛岄绾︽椂闂达細2026-05-11T00:00锛屽師鍥狅細111', 1, 1, '2026-05-10 10:56:40', 0);
INSERT INTO `notification` VALUES (332, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 楠ㄧ涓€鐥呭尯 鐨勫簥浣?O120锛岄绾︽椂闂达細2026-05-11T00:00锛屽師鍥狅細111', 1, 101, '2026-05-10 10:56:40', 0);
INSERT INTO `notification` VALUES (333, '鏂扮殑搴婁綅棰勭害鐢宠', '鐢ㄦ埛 admin 鐢宠棰勭害 楠ㄧ涓€鐥呭尯 鐨勫簥浣?O120锛岄绾︽椂闂达細2026-05-11T00:00锛屽師鍥狅細111', 1, 301, '2026-05-10 10:56:40', 0);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `permission_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_permission_code`(`permission_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------

-- ----------------------------
-- Table structure for reservation_waitlist
-- ----------------------------
DROP TABLE IF EXISTS `reservation_waitlist`;
CREATE TABLE `reservation_waitlist`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reservation_id` bigint NOT NULL,
  `wanted_department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `priority` int NULL DEFAULT 0,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_waitlist_reservation_id`(`reservation_id` ASC) USING BTREE,
  CONSTRAINT `fk_waitlist_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `bed_reservation` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservation_waitlist
-- ----------------------------

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `permission_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_permission_permission_id`(`permission_id` ASC) USING BTREE,
  CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `image_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `birth` date NULL DEFAULT NULL,
  `role_type` int NULL DEFAULT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'MD50192023a7bbd73250516f069df18b500', 'admin', NULL, '18888888888', NULL, NULL, NULL, 1, 'doctor', 1, '2025-11-16 13:52:00', '2025-11-16 14:02:09');
INSERT INTO `user` VALUES (2, 'test1', 'MD525d55ad283aa400af464c76d713c07ad', 'test1', NULL, '18888888889', 'test@qq.com', 'http://localhost:7245/202194918/0.jpg', '2000-01-15', 2, 'patient', 1, '2025-11-18 14:12:28', '2026-05-10 10:55:37');
INSERT INTO `user` VALUES (101, 'admin_test', 'MD5e10adc3949ba59abbe56e057f20f883e', '娴嬭瘯绠＄悊鍛?, '鐢?, '13900000001', 'admin_test@example.com', NULL, '1988-05-12', 1, 'admin', 1, '2026-04-01 09:00:00', '2026-04-01 09:00:00');
INSERT INTO `user` VALUES (102, 'patient_zhang', 'MD5e10adc3949ba59abbe56e057f20f883e', '寮犱笁', '鐢?, '13900000002', 'zhangsan@example.com', NULL, '1998-03-15', 2, 'patient', 1, '2026-04-01 09:10:00', '2026-04-01 09:10:00');
INSERT INTO `user` VALUES (103, 'patient_li', 'MD5e10adc3949ba59abbe56e057f20f883e', '鏉庡洓', '濂?, '13900000003', 'lisi@example.com', NULL, '2000-08-21', 2, 'patient', 1, '2026-04-01 09:20:00', '2026-04-01 09:20:00');
INSERT INTO `user` VALUES (104, 'patient_wang', 'MD5e10adc3949ba59abbe56e057f20f883e', '鐜嬩簲', '鐢?, '13900000004', 'wangwu@example.com', NULL, '1996-11-03', 2, 'patient', 1, '2026-04-01 09:30:00', '2026-04-01 09:30:00');
INSERT INTO `user` VALUES (105, 'test3', 'MD516d7a4fca7442dda3ad93c9a726597e4', '涓佷竴', NULL, '19000000000', '112@qq.com', NULL, NULL, 2, NULL, 1, '2026-04-26 17:07:40', '2026-04-26 17:07:40');
INSERT INTO `user` VALUES (106, 'paitent_zhao', 'MD5e10adc3949ba59abbe56e057f20f883e', '123456', NULL, '', '1100@qq.com', NULL, NULL, 2, NULL, 1, '2026-05-08 21:14:48', '2026-05-08 21:14:48');
INSERT INTO `user` VALUES (107, 'pool_patient', 'MD5e10adc3949ba59abbe56e057f20f883e', '骞跺彂娴嬭瘯', NULL, '', '', NULL, NULL, 2, NULL, 1, '2026-05-08 21:45:49', '2026-05-08 21:45:49');
INSERT INTO `user` VALUES (301, 'admin_more_01', 'MD5e10adc3949ba59abbe56e057f20f883e', '鎵╁睍绠＄悊鍛?, '鐢?, '13900000301', 'admin_more_01@example.com', NULL, '1987-06-15', 1, 'admin', 1, '2026-05-08 22:10:35', '2026-05-08 22:10:35');
INSERT INTO `user` VALUES (302, 'patient_zhao', 'MD5e10adc3949ba59abbe56e057f20f883e', '璧靛叚', '鐢?, '13900000302', 'zhaoliu@example.com', NULL, '1997-02-10', 2, 'patient', 1, '2026-05-08 22:10:35', '2026-05-08 22:10:35');
INSERT INTO `user` VALUES (303, 'patient_sun', 'MD5e10adc3949ba59abbe56e057f20f883e', '瀛欎竷', '濂?, '13900000303', 'sunqi@example.com', NULL, '1995-12-03', 2, 'patient', 1, '2026-05-08 22:10:35', '2026-05-08 22:10:35');
INSERT INTO `user` VALUES (304, 'patient_zhou', 'MD5e10adc3949ba59abbe56e057f20f883e', '鍛ㄥ叓', '鐢?, '13900000304', 'zhouba@example.com', NULL, '2001-07-21', 2, 'patient', 1, '2026-05-08 22:10:35', '2026-05-08 22:10:35');
INSERT INTO `user` VALUES (305, 'patient_wu', 'MD5e10adc3949ba59abbe56e057f20f883e', '鍚翠節', '濂?, '13900000305', 'wujiu@example.com', NULL, '1999-09-09', 2, 'patient', 1, '2026-05-08 22:10:35', '2026-05-08 22:10:35');
INSERT INTO `user` VALUES (306, 'patient_zheng', 'MD5e10adc3949ba59abbe56e057f20f883e', '閮戝崄', '鐢?, '13900000306', 'zhengshi@example.com', NULL, '1994-04-18', 2, 'patient', 1, '2026-05-08 22:10:35', '2026-05-08 22:10:35');

-- ----------------------------
-- Table structure for ward
-- ----------------------------
DROP TABLE IF EXISTS `ward`;
CREATE TABLE `ward`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ward_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `total_beds` int NOT NULL,
  `available_beds` int NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ward
-- ----------------------------
INSERT INTO `ward` VALUES (1, 'test1', 'Internal', 5, 5, 'test');
INSERT INTO `ward` VALUES (2, 'test2', 'Surgery', 20, 20, '111');
INSERT INTO `ward` VALUES (101, '鍐呯涓€鐥呭尯', '鍐呯', 6, 4, '闈犺繎鎶ゅ＋绔欙紝閫傚悎鏅€氬唴绉戞偅鑰?);
INSERT INTO `ward` VALUES (102, '澶栫浜岀梾鍖?, '澶栫', 6, 3, '鏈悗鎮ｈ€呴泦涓鐞?);
INSERT INTO `ward` VALUES (103, '鍎跨鐥呭尯', '鍎跨', 4, 3, '閰嶆湁鍎跨闄姢璁炬柦');
INSERT INTO `ward` VALUES (104, '楠ㄧ搴峰鐥呭尯', '楠ㄧ', 4, 2, '閫傜敤浜庨鎶樹笌鏈悗搴峰鎮ｈ€?);
INSERT INTO `ward` VALUES (301, '鍐呯涓夌梾鍖?, 'Internal', 20, 12, '蹇冭绠″強鏅€氬唴绉戞偅鑰?);
INSERT INTO `ward` VALUES (302, '澶栫涓夌梾鍖?, 'Surgery', 20, 11, '鏈悗鎭㈠鐥呭尯');
INSERT INTO `ward` VALUES (303, '鍎跨浜岀梾鍖?, 'Pediatrics', 20, 12, '鍎跨鎶ょ悊鐥呭尯');
INSERT INTO `ward` VALUES (304, '楠ㄧ涓€鐥呭尯', 'Orthopedics', 20, 10, '楠ㄦ姌涓庢湳鍚庡悍澶?);
INSERT INTO `ward` VALUES (305, '鍛煎惛鍐呯鐥呭尯', 'Respiratory', 20, 13, '鍛煎惛绯荤粺鐤剧梾鎮ｈ€?);

SET FOREIGN_KEY_CHECKS = 1;
