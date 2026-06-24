/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3308
 Source Server Type    : MySQL
 Source Server Version : 100316 (10.3.16-MariaDB)
 Source Host           : localhost:3308
 Source Schema         : cakeshop

 Target Server Type    : MySQL
 Target Server Version : 100316 (10.3.16-MariaDB)
 File Encoding         : 65001

 Date: 17/06/2026 15:49:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `good_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `amount` int NULL DEFAULT NULL,
  `price` float(10, 2) NULL DEFAULT NULL,
  `total_price` float(10, 2) NULL DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (63, '9', 'admin', '甜郁草莓配合冰淇淋的丝滑口感,让清爽与浪漫在爱情果园激情碰撞,恋上草莓,这份心情,美妙非凡.\r\n主口味:草莓口味 主要原料:草莓果溶 草莓  甜度:三星（满五星） 最佳食用温度：-12至-15摄氏度', 5, 299.00, 1495.00, '/picture/9-1.jpg');
INSERT INTO `cart` VALUES (64, '13', 'admin', '蛋黄与蜂蜜,淡奶油共同演绎的曼妙之旅.口感Q糯浓郁,回味绵软柔长.皱巴巴的造型,甜蜜蜜的感受.', 1, 36.00, 36.00, '/picture/13-1.jpg');
INSERT INTO `cart` VALUES (95, '2', 'vili', '采用帕玛森芝士为主要原材料制作的意大利芝士饼,奶香浓郁,鲜香可口.', 2, 39.00, 78.00, '/picture/14-1.jpg');
INSERT INTO `cart` VALUES (98, '15', 'vili', '走进小熊乐园,与可爱的小熊一起准备过冬的食物吧,摘颗草莓藏放在巧克力做的房子里,好朋友一起分享劳动的果实.\r\n主口味:草莓奶油味 主要原料:乳脂奶油,纯巧克力,朗姆酒,幼砂糖,鲜草莓 甜度:二星（满五星） 最佳食用温度：5-7摄氏度', 1, 299.00, 299.00, '/picture/8-1.jpg');
INSERT INTO `cart` VALUES (99, '18', 'vili', '为了保证芝士的香醇,半熟芝士借鉴日本温泉煮鸡蛋的做法,把芝士,牛奶,鸡蛋,天然奶油,砂糖,小麦粉拌成面糊,通过水浴蒸烤,保证芝士蛋糕细嫩,柔软,留住芝士的香醇细滑.', 1, 38.00, 38.00, '/picture/11-1.jpg');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cover` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image1` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image2` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` float NULL DEFAULT NULL,
  `intro` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stock` int NULL DEFAULT NULL,
  `type_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_type_id_idx`(`type_id` ASC) USING BTREE,
  CONSTRAINT `fk_type_id` FOREIGN KEY (`type_id`) REFERENCES `type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (2, '意大利芝士饼干', '/picture/14-1.jpg', '/picture/14-1.jpg', '/picture/14-1.jpg', 38, '采用帕玛森芝士为主要原材料制作的意大利芝士饼,奶香浓郁,鲜香可口.', 89, 1);
INSERT INTO `goods` VALUES (9, '草莓冰淇淋', '/picture/9-1.jpg', '/picture/9-2.jpg', '/picture/9-3.jpg', 299, '甜郁草莓配合冰淇淋的丝滑口感,让清爽与浪漫在爱情果园激情碰撞,恋上草莓,这份心情,美妙非凡.\r\n主口味:草莓口味 主要原料:草莓果溶 草莓  甜度:三星（满五星） 最佳食用温度：-12至-15摄氏度', 39, 1);
INSERT INTO `goods` VALUES (10, '玫瑰舒芙蕾', '/picture/10-1.jpg', '/picture/10-2.jpg', '/picture/10-3.jpg', 28, '优选法国芝士,奶香浓郁,质地柔滑,口感细腻.法国芝士有助于提升糕点的整体口感,完美平衡酸度与甜味,制作出的糕点更加洁白纯美.', 87, 3);
INSERT INTO `goods` VALUES (11, '半熟芝士', '/picture/11-1.jpg', '/picture/11-1.jpg', '/picture/11-1.jpg', 38, '为了保证芝士的香醇,半熟芝士借鉴日本温泉煮鸡蛋的做法,把芝士,牛奶,鸡蛋,天然奶油,砂糖,小麦粉拌成面糊,通过水浴蒸烤,保证芝士蛋糕细嫩,柔软,留住芝士的香醇细滑.', 95, 3);
INSERT INTO `goods` VALUES (12, '青森芝士条', '/picture/12-1.jpg', '/picture/1-2.jpg', '/picture/12-1.jpg', 36, '青森芝士和风轻拂,,奶香浓郁,质地柔滑,口感细腻.', 90, 2);
INSERT INTO `goods` VALUES (13, '蜂蜜蛋糕', '/picture/13-1.jpg', '/picture/13-1.jpg', '/picture/13-1.jpg', 36, '蛋黄与蜂蜜,淡奶油共同演绎的曼妙之旅.口感Q糯浓郁,回味绵软柔长.皱巴巴的造型,甜蜜蜜的感受.', 94, 2);
INSERT INTO `goods` VALUES (14, '意大利芝士饼干', '/picture/14-1.jpg', '/picture/14-1.jpg', '/picture/14-1.jpg', 39, '采用帕玛森芝士为主要原材料制作的意大利芝士饼,奶香浓郁,鲜香可口.', 97, 2);
INSERT INTO `goods` VALUES (15, '小熊乐园', '/picture/8-1.jpg', '/picture/8-2.jpg', '/picture/8-3.jpg', 299, '走进小熊乐园,与可爱的小熊一起准备过冬的食物吧,摘颗草莓藏放在巧克力做的房子里,好朋友一起分享劳动的果实.\r\n主口味:草莓奶油味 主要原料:乳脂奶油,纯巧克力,朗姆酒,幼砂糖,鲜草莓 甜度:二星（满五星） 最佳食用温度：5-7摄氏度', 87, 3);
INSERT INTO `goods` VALUES (16, '草莓冰淇淋', '/picture/9-1.jpg', '/picture/9-2.jpg', '/picture/9-3.jpg', 299, '甜郁草莓配合冰淇淋的丝滑口感,让清爽与浪漫在爱情果园激情碰撞,恋上草莓,这份心情,美妙非凡.\r\n主口味:草莓口味 主要原料:草莓果溶 草莓  甜度:三星（满五星） 最佳食用温度：-12至-15摄氏度', 98, 1);
INSERT INTO `goods` VALUES (18, '半熟芝士', '/picture/11-1.jpg', '/picture/11-1.jpg', '/picture/11-1.jpg', 38, '为了保证芝士的香醇,半熟芝士借鉴日本温泉煮鸡蛋的做法,把芝士,牛奶,鸡蛋,天然奶油,砂糖,小麦粉拌成面糊,通过水浴蒸烤,保证芝士蛋糕细嫩,柔软,留住芝士的香醇细滑.', 97, 2);
INSERT INTO `goods` VALUES (19, '青森芝士条', '/picture/12-1.jpg', '/picture/1-2.jpg', '/picture/12-1.jpg', 36, '青森芝士和风轻拂,,奶香浓郁,质地柔滑,口感细腻.', 96, 4);
INSERT INTO `goods` VALUES (20, '蜂蜜蛋糕', '/picture/13-1.jpg', '/picture/13-1.jpg', '/picture/13-1.jpg', 36, '蛋黄与蜂蜜,淡奶油共同演绎的曼妙之旅.口感Q糯浓郁,回味绵软柔长.皱巴巴的造型,甜蜜蜜的感受.', 99, 4);
INSERT INTO `goods` VALUES (21, '意大利芝士饼干', '/picture/14-1.jpg', '/picture/14-1.jpg', '/picture/14-1.jpg', 39, '采用帕玛森芝士为主要原材料制作的意大利芝士饼,奶香浓郁,鲜香可口.', 107, 2);

-- ----------------------------
-- Table structure for knight
-- ----------------------------
DROP TABLE IF EXISTS `knight`;
CREATE TABLE `knight`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '骑士ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码（加密）',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像URL',
  `status` int NULL DEFAULT 1 COMMENT '状态（1-在线，0-离线）',
  `create_time` datetime NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '骑士表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of knight
-- ----------------------------
INSERT INTO `knight` VALUES ('K001', 'knight1', '123456', '张三', '13800138001', NULL, 1, '2026-06-17 13:43:09', '2026-06-17 13:43:09');
INSERT INTO `knight` VALUES ('K002', 'knight2', '123456', '李四', '13800138002', NULL, 1, '2026-06-17 13:43:09', '2026-06-17 13:43:09');

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_isadmin` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `operation` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `module` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `details` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of log
-- ----------------------------
INSERT INTO `log` VALUES (28, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (29, 'rider', '张三', '登录', '骑士端', '骑士登录系统，账号: knight1');
INSERT INTO `log` VALUES (30, 'rider', '张三', '退出', '骑士端', '骑士退出系统');
INSERT INTO `log` VALUES (31, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (32, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (33, 'rider', '张三', '登录', '骑士端', '骑士登录系统，账号: knight1');
INSERT INTO `log` VALUES (34, 'rider', '张三', '退出', '骑士端', '骑士退出系统');
INSERT INTO `log` VALUES (35, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (36, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (37, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (38, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (39, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (40, 'rider', '张三', '登录', '骑士端', '骑士登录系统，账号: knight1');
INSERT INTO `log` VALUES (41, 'rider', '张三', '接单', '骑士端', '骑士接单，订单号: 1774709963411');
INSERT INTO `log` VALUES (42, 'rider', '张三', '取货', '骑士端', '骑士取货，订单号: 1774710431836');
INSERT INTO `log` VALUES (43, 'rider', '张三', '送达', '骑士端', '骑士送达，订单号: 1774710431836');
INSERT INTO `log` VALUES (44, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (45, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (46, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (47, 'rider', '张三', '登录', '骑士端', '骑士登录系统，账号: knight1');
INSERT INTO `log` VALUES (48, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (49, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (50, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (51, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (52, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (53, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (54, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (55, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (56, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (57, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (58, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (59, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (60, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (61, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (62, '1', 'admin', '登录', '系统管理', '管理员登录: admin');
INSERT INTO `log` VALUES (63, 'rider', '张三', '登录', '骑士端', '骑士登录系统，账号: knight1');

-- ----------------------------
-- Table structure for month_table
-- ----------------------------
DROP TABLE IF EXISTS `month_table`;
CREATE TABLE `month_table`  (
  `MONTH_NUM` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of month_table
-- ----------------------------
INSERT INTO `month_table` VALUES (1);
INSERT INTO `month_table` VALUES (2);
INSERT INTO `month_table` VALUES (3);
INSERT INTO `month_table` VALUES (4);
INSERT INTO `month_table` VALUES (5);
INSERT INTO `month_table` VALUES (6);
INSERT INTO `month_table` VALUES (7);
INSERT INTO `month_table` VALUES (8);
INSERT INTO `month_table` VALUES (9);
INSERT INTO `month_table` VALUES (10);
INSERT INTO `month_table` VALUES (11);
INSERT INTO `month_table` VALUES (12);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `total` float(20, 0) NULL DEFAULT NULL,
  `amount` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL COMMENT '2:已付款 3:已发货 4:已完成',
  `paytype` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `datetime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `knight_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配送骑士ID',
  `knight_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '骑士姓名',
  `knight_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '骑士电话',
  `pickup_time` datetime NULL DEFAULT NULL COMMENT '取货时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '送达时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_id_idx`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('1690440734978', 1196, 4, 4, 1, '管理员', '1333333333', '中华人民共和国', '2023-07-27 14:52:14', 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1690441025870', 355, 3, 4, 2, '管理员', '1333333333', '中华人民共和国', '2023-07-27 14:57:05', 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1699936968220', 5522, 23, 4, 1, 'vili', '1344444444', '中华人民共和国', '2023-8-14 12:42:48', 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1712835005129', 1495, 5, 2, 1, 'vili', '1344444444', '中华人民共和国', '2024-04-11', 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1712839550540', 1495, 5, 2, 1, 'vili', '1344444444', '中华人民共和国', '2024-04-11 20:45:50', 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1713147394614', 299, 1, 2, 1, 'vili', '1344444444', '中华人民共和国', '2024-04-15 10:16:34', 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1713147598081', 28, 1, 2, 2, 'vili', '1344444444', '中华人民共和国', '2024-04-15 10:19:58', 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1713147745528', 299, 1, 2, 1, 'vili', '1344444444', '中华人民共和国', '2024-04-15 10:22:25', 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1713159946825', 299, 1, 2, 1, 'vili', '1344444444', '中华人民共和国', '2024-04-15 13:45:46', 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1713165647839', 897, 3, 2, 1, 'vili', '1344444444', '中华人民共和国', '2024-04-15 15:20:47', 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1716531956580', 4158, 28, 4, 1, '123', '123', '123', '2024-05-24 14:25:56', 62, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1716535756003', 3915, 7, 3, 1, '管理员', '1333333333', '中华人民共和国', '2024-05-24 15:29:16', 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1717145845959', 4242, 9, 2, 2, '管理员', '1333333333', '中华人民共和国', '2024-05-31 16:57:25', 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1717145852401', 2121, 1, 2, 1, '管理员', '1333333333', '中华人民共和国', '2024-05-31 16:57:32', 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1717145860936', 1858, 1, 2, 1, '管理员', '1333333333', '中华人民共和国', '2024-05-31 16:57:40', 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1730035967488', 327, 2, 4, 1, '123', '123', '123', '2024-10-27 21:32:47', 62, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1732501072252', 3137, 14, 3, 1, '123', '123', '123', '2024-11-25 10:17:52', 62, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1732501143472', 299, 1, 2, 1, '123', '123', '123', '2024-11-25 10:19:03', 62, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES ('1774709963411', 2813, 13, 3, 1, '管理员', '1333333333', '中华人民共和国', '2026-03-28 22:59:23', 1, 'K001', '张三', '13800138001', NULL, NULL);
INSERT INTO `order` VALUES ('1774710077367', 1587, 1, 3, 1, '管理员', '1333333333', '中华人民共和国', '2026-03-28 23:01:17', 1, 'K001', '张三', '13800138001', NULL, NULL);
INSERT INTO `order` VALUES ('1774710431836', 1587, 1, 5, 1, '管理员', '1333333333', '中华人民共和国', '2026-03-28 23:07:11', 1, 'K001', '张三', '13800138001', '2026-06-17 14:56:24', '2026-06-17 14:56:26');
INSERT INTO `order` VALUES ('1776352559452', 1645, 3, 5, 1, '123', '123', '123', '2026-04-16 23:15:59', 62, 'K001', '张三', '13800138001', '2026-06-17 14:21:54', '2026-06-17 14:21:59');
INSERT INTO `order` VALUES ('1776352643383', 1609, 2, 5, 1, '123', '123', '123', '2026-04-16 23:17:23', 62, 'K001', '张三', '13800138001', '2026-06-17 13:48:30', '2026-06-17 13:55:45');

-- ----------------------------
-- Table structure for orderitem
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` float NULL DEFAULT NULL,
  `amount` int NULL DEFAULT NULL,
  `goods_id` int NULL DEFAULT NULL,
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_order_id_idx`(`order_id` ASC) USING BTREE,
  INDEX `fk_orderitem_goods_id_idx`(`goods_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_id` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_orderitem_goods_id` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 174 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orderitem
-- ----------------------------
INSERT INTO `orderitem` VALUES (108, 299, 4, 9, '1690440734978');
INSERT INTO `orderitem` VALUES (109, 299, 1, 9, '1690441025870');
INSERT INTO `orderitem` VALUES (110, 28, 2, 10, '1690441025870');
INSERT INTO `orderitem` VALUES (111, 299, 14, 9, '1699936968220');
INSERT INTO `orderitem` VALUES (112, 299, 4, 16, '1699936968220');
INSERT INTO `orderitem` VALUES (113, 28, 5, 10, '1699936968220');
INSERT INTO `orderitem` VALUES (114, 299, 4, 9, '1712839550540');
INSERT INTO `orderitem` VALUES (115, 299, 1, 15, '1712839550540');
INSERT INTO `orderitem` VALUES (116, 299, 1, 9, '1713147394614');
INSERT INTO `orderitem` VALUES (117, 28, 1, 10, '1713147598081');
INSERT INTO `orderitem` VALUES (118, 299, 1, 9, '1713147745528');
INSERT INTO `orderitem` VALUES (119, 299, 1, 9, '1713159946825');
INSERT INTO `orderitem` VALUES (120, 299, 3, 9, '1713165647839');
INSERT INTO `orderitem` VALUES (121, 36, 4, 12, '1716531956580');
INSERT INTO `orderitem` VALUES (122, 299, 4, 15, '1716531956580');
INSERT INTO `orderitem` VALUES (123, 28, 3, 10, '1716531956580');
INSERT INTO `orderitem` VALUES (124, 38, 2, 11, '1716531956580');
INSERT INTO `orderitem` VALUES (125, 39, 3, 21, '1716531956580');
INSERT INTO `orderitem` VALUES (126, 36, 1, 13, '1716531956580');
INSERT INTO `orderitem` VALUES (127, 39, 1, 14, '1716531956580');
INSERT INTO `orderitem` VALUES (128, 299, 1, 16, '1716531956580');
INSERT INTO `orderitem` VALUES (129, 36, 1, 19, '1716531956580');
INSERT INTO `orderitem` VALUES (130, 38, 1, 18, '1716531956580');
INSERT INTO `orderitem` VALUES (131, 299, 7, 9, '1716531956580');
INSERT INTO `orderitem` VALUES (132, 299, 7, 9, '1716535756003');
INSERT INTO `orderitem` VALUES (133, 299, 3, 15, '1717145845959');
INSERT INTO `orderitem` VALUES (134, 299, 5, 9, '1717145845959');
INSERT INTO `orderitem` VALUES (135, 28, 1, 10, '1717145845959');
INSERT INTO `orderitem` VALUES (136, 299, 1, 9, '1717145852401');
INSERT INTO `orderitem` VALUES (137, 36, 1, 12, '1717145860936');
INSERT INTO `orderitem` VALUES (138, 299, 1, 9, '1730035967488');
INSERT INTO `orderitem` VALUES (139, 28, 1, 10, '1730035967488');
INSERT INTO `orderitem` VALUES (140, 299, 9, 9, '1732501072252');
INSERT INTO `orderitem` VALUES (141, 36, 1, 13, '1732501072252');
INSERT INTO `orderitem` VALUES (142, 299, 1, 16, '1732501072252');
INSERT INTO `orderitem` VALUES (143, 36, 2, 19, '1732501072252');
INSERT INTO `orderitem` VALUES (144, 39, 1, 21, '1732501072252');
INSERT INTO `orderitem` VALUES (145, 299, 1, 9, '1732501143472');
INSERT INTO `orderitem` VALUES (146, 28, 1, 10, '1774709963411');
INSERT INTO `orderitem` VALUES (147, 39, 2, 14, '1774709963411');
INSERT INTO `orderitem` VALUES (148, 38, 1, 11, '1774709963411');
INSERT INTO `orderitem` VALUES (149, 36, 1, 13, '1774709963411');
INSERT INTO `orderitem` VALUES (150, 299, 2, 15, '1774709963411');
INSERT INTO `orderitem` VALUES (151, 39, 1, 21, '1774709963411');
INSERT INTO `orderitem` VALUES (152, 36, 1, 20, '1774709963411');
INSERT INTO `orderitem` VALUES (153, 36, 1, 19, '1774709963411');
INSERT INTO `orderitem` VALUES (154, 38, 1, 18, '1774709963411');
INSERT INTO `orderitem` VALUES (155, 299, 2, 9, '1774709963411');
INSERT INTO `orderitem` VALUES (156, 299, 1, 9, '1774710077367');
INSERT INTO `orderitem` VALUES (157, 299, 1, 9, '1774710431836');
INSERT INTO `orderitem` VALUES (158, 36, 1, 12, '1776352559452');
INSERT INTO `orderitem` VALUES (159, 39, 2, 2, '1776352559452');
INSERT INTO `orderitem` VALUES (160, 39, 2, 2, '1776352643383');

-- ----------------------------
-- Table structure for recommend
-- ----------------------------
DROP TABLE IF EXISTS `recommend`;
CREATE TABLE `recommend`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NULL DEFAULT NULL,
  `goods_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_goods_id_idx`(`goods_id` ASC) USING BTREE,
  CONSTRAINT `fk_goods_id` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of recommend
-- ----------------------------
INSERT INTO `recommend` VALUES (9, 2, 9);
INSERT INTO `recommend` VALUES (10, 3, 10);
INSERT INTO `recommend` VALUES (11, 3, 12);
INSERT INTO `recommend` VALUES (12, 3, 13);
INSERT INTO `recommend` VALUES (13, 3, 14);
INSERT INTO `recommend` VALUES (14, 3, 15);
INSERT INTO `recommend` VALUES (15, 3, 16);
INSERT INTO `recommend` VALUES (17, 3, 18);
INSERT INTO `recommend` VALUES (18, 3, 19);
INSERT INTO `recommend` VALUES (33, 2, 10);
INSERT INTO `recommend` VALUES (34, 2, 11);
INSERT INTO `recommend` VALUES (35, 2, 12);
INSERT INTO `recommend` VALUES (36, 2, 13);
INSERT INTO `recommend` VALUES (37, 2, 14);
INSERT INTO `recommend` VALUES (38, 2, 15);
INSERT INTO `recommend` VALUES (39, 2, 16);
INSERT INTO `recommend` VALUES (40, 2, 18);

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES (1, '冰淇淋系列');
INSERT INTO `type` VALUES (2, '零食系列');
INSERT INTO `type` VALUES (3, '儿童系列');
INSERT INTO `type` VALUES (4, '法式系列');
INSERT INTO `type` VALUES (5, '经典系列');
INSERT INTO `type` VALUES (8, '节日系列');
INSERT INTO `type` VALUES (11, '买不起系列');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `isadmin` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `isvalidate` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_UNIQUE`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email_UNIQUE`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin@vilicode.com', 'admin', '管理员', '1333333333', '中华人民共和国', '1', '1');
INSERT INTO `user` VALUES (2, '1', 'vili@vilicode.com', '1', 'vili', '1344444444', '中华人民共和国', '0', '1');
INSERT INTO `user` VALUES (62, 'vili', '4115@qq.con', 'vili', '123', '123', '123', '0', '1');
INSERT INTO `user` VALUES (63, '小明', '123/299@qq.com', '199', 'aaaa', '9', 'a', '1', '1');
INSERT INTO `user` VALUES (64, '小蓝', '1635048270@qq.com', 'admin5', '12', '123456789', '福建省福州市长乐区滨海路168号', '1', '1');
INSERT INTO `user` VALUES (66, '小红', '1635048270111@qq.com', 'admin7', '56565656', '14235246356768', '福建省福州市长乐区滨海路168号', '1', '1');
INSERT INTO `user` VALUES (68, '小绿', '163504827110@qq.com', 'admin', '11', '111', '福建省福州市长乐区滨海路168号', '1', '1');
INSERT INTO `user` VALUES (70, '柯涛123', '321@qq.com', '123', '123', '123456', '123456789', '0', '1');
INSERT INTO `user` VALUES (71, '谢帛言321', '123@qq.com', '321', '3210', '654321', '987654321', '0', '1');

SET FOREIGN_KEY_CHECKS = 1;
