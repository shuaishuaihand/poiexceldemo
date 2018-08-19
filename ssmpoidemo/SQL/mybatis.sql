/*
Navicat MySQL Data Transfer

Source Server         : nishuai
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : mybatis

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2018-08-05 17:09:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lastName` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `deptid` int(11) DEFAULT NULL,
  `empStatus` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38242 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('38222', '张三', '2714763867@qq.com', '男', null, null);
INSERT INTO `employee` VALUES ('38223', '李四', '213414', '男', null, null);
INSERT INTO `employee` VALUES ('38224', '丽丽', '213541345', '女', null, null);
INSERT INTO `employee` VALUES ('38225', 'jim', '21341234', '男', null, null);
INSERT INTO `employee` VALUES ('38226', 'Tom', '32441345', '男', null, null);
INSERT INTO `employee` VALUES ('38227', 'jack', '52462476567', 'man', null, null);
INSERT INTO `employee` VALUES ('38228', 'rose', '345315415', 'woman', null, null);
INSERT INTO `employee` VALUES ('38229', '杨明', '24·234', '男', null, null);
INSERT INTO `employee` VALUES ('38230', '张丽', '233434', '女', null, null);
INSERT INTO `employee` VALUES ('38231', '503cd', 'b', '1', null, null);
INSERT INTO `employee` VALUES ('38232', '张三', '2714763867@qq.com', '男', null, null);
INSERT INTO `employee` VALUES ('38233', '李四', '213414', '男', null, null);
INSERT INTO `employee` VALUES ('38234', '丽丽', '213541345', '女', null, null);
INSERT INTO `employee` VALUES ('38235', 'jim', '21341234', '男', null, null);
INSERT INTO `employee` VALUES ('38236', 'Tom', '32441345', '男', null, null);
INSERT INTO `employee` VALUES ('38237', 'jack', '52462476567', 'man', null, null);
INSERT INTO `employee` VALUES ('38238', 'rose', '345315415', 'woman', null, null);
INSERT INTO `employee` VALUES ('38239', '杨明', '24·234', '男', null, null);
INSERT INTO `employee` VALUES ('38240', '张丽', '233434', '女', null, null);
INSERT INTO `employee` VALUES ('38241', '503cd', 'b', '1', null, null);

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `id` int(11) NOT NULL,
  `111` varchar(255) DEFAULT NULL,
  `22` varchar(255) DEFAULT NULL,
  `333` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO `test` VALUES ('1', '111', '22', '333');
