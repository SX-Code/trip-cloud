/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : trip-user

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 05/11/2023 13:29:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_favor_strategies
-- ----------------------------
DROP TABLE IF EXISTS `user_favor_strategies`;
CREATE TABLE `user_favor_strategies` (
  `user_id` bigint NOT NULL,
  `strategy_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of user_favor_strategies
-- ----------------------------
BEGIN;
INSERT INTO `user_favor_strategies` (`user_id`, `strategy_id`) VALUES (2, 5);
COMMIT;

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `gender` int DEFAULT NULL,
  `level` int DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `head_img_url` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `state` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `phone` (`phone`,`state`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
BEGIN;
INSERT INTO `userinfo` (`id`, `nickname`, `phone`, `email`, `password`, `gender`, `level`, `city`, `head_img_url`, `info`, `state`) VALUES (1, 'xiaoliu', '18888888888', 'xiaoliu@wolfcode.cn', '3DE4F66F7A10E60059E0A697BB9B703A', 1, 1, '成都', '/images/default.jpg', '成都最靓的仔', 0);
INSERT INTO `userinfo` (`id`, `nickname`, `phone`, `email`, `password`, `gender`, `level`, `city`, `head_img_url`, `info`, `state`) VALUES (2, 'swcode', '15765231846', 'swcode@outlook.com', 'C864BA6557C4546F4E968976123A592F', 0, 99, '武汉', '/images/default.jpg', '这个人很懒, 什么都没写.', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
