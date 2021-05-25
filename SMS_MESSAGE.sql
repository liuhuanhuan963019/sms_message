/*
 Navicat Premium Data Transfer

 Source Server         : 139.159.147.237
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 139.159.147.237:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 12/11/2020 09:45:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for SMS_MESSAGE
-- ----------------------------
DROP TABLE IF EXISTS `SMS_MESSAGE`;
CREATE TABLE `SMS_MESSAGE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL,
  `sms_code` varchar(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of SMS_MESSAGE
-- ----------------------------
BEGIN;
INSERT INTO `SMS_MESSAGE` VALUES (1, '18428300670', '833431');
INSERT INTO `SMS_MESSAGE` VALUES (2, '13535154153', '520177');
INSERT INTO `SMS_MESSAGE` VALUES (3, '13100671550', '762442');
INSERT INTO `SMS_MESSAGE` VALUES (4, '15901246209', '420686');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
