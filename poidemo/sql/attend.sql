/*
Navicat MySQL Data Transfer

Source Server         : nishuai
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : attend

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2018-07-10 09:07:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for attend_classes
-- ----------------------------
DROP TABLE IF EXISTS `attend_classes`;
CREATE TABLE `attend_classes` (
  `classes_id` int(11) NOT NULL AUTO_INCREMENT,
  `classes_name` varchar(50) NOT NULL DEFAULT '' COMMENT '班次名称',
  `cruser` varchar(20) DEFAULT NULL COMMENT '创建人',
  `crtime` datetime DEFAULT NULL COMMENT '创建时间',
  `upuser` varchar(20) DEFAULT NULL COMMENT '更新人',
  `uptime` datetime DEFAULT NULL COMMENT '更新时间',
  `is_one_or_more` int(255) DEFAULT NULL COMMENT '0 : 一天一次打卡  1：一天多次打卡',
  PRIMARY KEY (`classes_id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8mb4 COMMENT='考勤班次表';

-- ----------------------------
-- Records of attend_classes
-- ----------------------------
INSERT INTO `attend_classes` VALUES ('100', 'A', null, null, null, '2018-06-25 18:40:59', '1');
INSERT INTO `attend_classes` VALUES ('116', 'C', null, '2018-06-23 00:00:00', null, '2018-06-25 18:43:36', '1');
INSERT INTO `attend_classes` VALUES ('161', 'B', null, '2018-06-26 14:41:38', null, '2018-06-26 17:38:11', '1');

-- ----------------------------
-- Table structure for attend_classes_detail
-- ----------------------------
DROP TABLE IF EXISTS `attend_classes_detail`;
CREATE TABLE `attend_classes_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `classes_id` int(11) NOT NULL COMMENT '班次ID',
  `classes_on_time` time NOT NULL DEFAULT '00:00:00' COMMENT '上班打卡时间',
  `classes_off_time` time NOT NULL DEFAULT '00:00:00' COMMENT '下班打卡时间',
  `on__timeframe` int(10) NOT NULL COMMENT '上班打卡时段',
  `on_end_timeframe` int(11) DEFAULT '0' COMMENT '上班打卡截止时间',
  `off__timeframe` int(10) NOT NULL COMMENT '下班打卡时段',
  `rest_start_time` time DEFAULT NULL COMMENT '休息开始时间',
  `rest_end_time` time DEFAULT NULL COMMENT '休息结束时间',
  `is_rest` int(10) DEFAULT '0' COMMENT '0：无休息时间段 1：有休息时间段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of attend_classes_detail
-- ----------------------------

-- ----------------------------
-- Table structure for attend_record
-- ----------------------------
DROP TABLE IF EXISTS `attend_record`;
CREATE TABLE `attend_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dev_id` int(11) DEFAULT NULL COMMENT '设备ID',
  `dev_address` varchar(255) DEFAULT NULL COMMENT '设备地址',
  `pic_url` varchar(255) DEFAULT NULL COMMENT '通行图片',
  `p_id` int(11) DEFAULT NULL COMMENT '人员ID',
  `p_name` varchar(255) DEFAULT NULL COMMENT '人员姓名',
  `card_no` varchar(255) DEFAULT NULL COMMENT '工号',
  `org_id` int(11) DEFAULT NULL COMMENT '机构ID',
  `org_name` varchar(255) DEFAULT NULL COMMENT '机构名称',
  `classes_id` int(11) DEFAULT NULL COMMENT '班次ID',
  `classes_name` varchar(255) DEFAULT NULL COMMENT '班次名称',
  `classes_time` varchar(255) DEFAULT NULL COMMENT '班次考勤时间',
  `attend_time` datetime DEFAULT NULL COMMENT '考勤时间',
  `attend_flag` int(11) DEFAULT NULL COMMENT '识别标识：0上班 下班1',
  `attend_outcome` int(1) DEFAULT '0' COMMENT '考勤结果：0正常，1迟到，2早退，3加班，4缺卡，5旷工',
  `attend_flag_time` int(11) DEFAULT '0' COMMENT '异常持续时间，迟到、早退、加班时长（以分钟为单位）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `over_time_flag` varchar(255) DEFAULT NULL COMMENT '加班标识：0工作日加班，1休息日加班，2节假日加班',
  `work_time` double(7,2) DEFAULT '0.00' COMMENT '工作时间',
  PRIMARY KEY (`id`),
  KEY `pk_pid` (`p_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1733 DEFAULT CHARSET=utf8mb4 COMMENT='考勤记录表';

-- ----------------------------
-- Records of attend_record
-- ----------------------------

-- ----------------------------
-- Table structure for s_person
-- ----------------------------
DROP TABLE IF EXISTS `s_person`;
CREATE TABLE `s_person` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `person_name` varchar(64) NOT NULL COMMENT '姓名',
  `org_id` int(11) DEFAULT NULL COMMENT '组织结构',
  `dev_id` varchar(20) DEFAULT NULL COMMENT '设备ID',
  `card_no` varchar(255) DEFAULT NULL COMMENT '工号',
  `edit_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `photo` varchar(255) DEFAULT NULL COMMENT '图片映射路径',
  `state` int(1) DEFAULT '0' COMMENT '0：启用  1：停用  2：离职 3 :删除',
  `address` varchar(255) DEFAULT NULL COMMENT '位置',
  `feature_array` longtext COMMENT '特征数组',
  `person_type` varchar(2) DEFAULT NULL COMMENT '人员类型',
  `delete_flag` varchar(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='人员';

-- ----------------------------
-- Records of s_person
-- ----------------------------
INSERT INTO `s_person` VALUES ('1', '吴涛', null, null, '0028', null, '2018-07-10 09:05:17', null, '0', '安徽时旭智能科技有限公司经纬度:117.148427,31.828039', null, null, null);
INSERT INTO `s_person` VALUES ('2', '卞苗苗', null, null, '0099', null, '2018-07-10 09:05:19', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('3', '程涛', null, null, '', null, '2018-07-10 09:05:22', null, '0', '安徽时旭智能科技有限公司经纬度:117.148351,31.827191', null, null, null);
INSERT INTO `s_person` VALUES ('4', '程月', null, null, '0026', null, '2018-07-10 09:05:24', null, '0', '安徽时旭智能科技有限公司经纬度:117.148698,31.827842', null, null, null);
INSERT INTO `s_person` VALUES ('5', '高婷婷', null, null, '0002', null, '2018-07-10 09:05:26', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('6', '黄唤', null, null, '0030', null, '2018-07-10 09:05:28', null, '0', '安徽时旭智能科技有限公司经纬度:117.148282,31.828149', null, null, null);
INSERT INTO `s_person` VALUES ('7', '李蓬', null, null, '0021', null, '2018-07-10 09:05:31', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('8', '李晓光', null, null, '', null, '2018-07-10 09:05:33', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('9', '吴荟栋', null, null, '', null, '2018-07-10 09:05:34', null, '0', '安徽时旭智能科技有限公司经纬度:117.148496,31.827584', null, null, null);
INSERT INTO `s_person` VALUES ('10', '尹晓芳', null, null, '0019', null, '2018-07-10 09:05:37', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('11', '周晨', null, null, '', null, '2018-07-10 09:05:40', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('12', '邹强强', null, null, '0012', null, '2018-07-10 09:05:41', null, '0', '安徽时旭智能科技有限公司经纬度:117.148305,31.828319', null, null, null);
INSERT INTO `s_person` VALUES ('13', '马见红（离职）', null, null, '', null, '2018-07-10 09:05:43', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('14', '杜方红', null, null, '', null, '2018-07-10 09:05:43', null, '0', '安徽时旭智能科技有限公司经纬度:117.148279,31.827119', null, null, null);
INSERT INTO `s_person` VALUES ('15', '胡双双', null, null, '', null, '2018-07-10 09:05:45', null, '0', '安徽时旭智能科技有限公司经纬度:117.148463,31.82766', null, null, null);
INSERT INTO `s_person` VALUES ('16', '刘娜', null, null, '0010', null, '2018-07-10 09:05:48', null, '0', '安徽时旭智能科技有限公司经纬度:117.148195,31.82779', null, null, null);
INSERT INTO `s_person` VALUES ('17', '张宜嘉', null, null, '', null, '2018-07-10 09:05:50', null, '0', '安徽时旭智能科技有限公司经纬度:117.148375,31.827631', null, null, null);
INSERT INTO `s_person` VALUES ('18', '曹春艳', null, null, '0027', null, '2018-07-10 09:05:51', null, '0', '安徽时旭智能科技有限公司经纬度:117.148295,31.827175', null, null, null);
INSERT INTO `s_person` VALUES ('19', '李欢', null, null, '0078', null, '2018-07-10 09:05:52', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('20', '李子', null, null, '', null, '2018-07-10 09:05:55', null, '0', '安徽时旭智能科技有限公司经纬度:117.148371,31.827174', null, null, null);
INSERT INTO `s_person` VALUES ('21', '刘启朋', null, null, 'null', null, '2018-07-10 09:05:57', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('22', '倪帅', null, null, '', null, '2018-07-10 09:05:58', null, '0', '', null, null, null);
INSERT INTO `s_person` VALUES ('23', '倪修宽', null, null, '0017', null, '2018-07-10 09:06:00', null, '0', '安徽时旭智能科技有限公司经纬度:117.15111,31.830608', null, null, null);
INSERT INTO `s_person` VALUES ('24', '桑财政', null, null, '', null, '2018-07-10 09:06:02', null, '0', '安徽省合肥市蜀山区高新技术产业开发区华佗巷', null, null, null);
INSERT INTO `s_person` VALUES ('25', '圣路（离职）', null, null, '0015', null, '2018-07-10 09:06:02', null, '0', '安徽时旭智能科技有限公司经纬度:117.14783,31.827346', null, null, null);
INSERT INTO `s_person` VALUES ('26', '孙巧巧', null, null, '0083', null, '2018-07-10 09:06:05', null, '0', '安徽时旭智能科技有限公司经纬度:117.148219,31.827862', null, null, null);
INSERT INTO `s_person` VALUES ('27', '汤久伟', null, null, '0018', null, '2018-07-10 09:06:07', null, '0', '安徽时旭智能科技有限公司经纬度:117.148472,31.827623', null, null, null);
INSERT INTO `s_person` VALUES ('28', '汤军林', null, null, '', null, '2018-07-10 09:06:08', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('29', '汪文红', null, null, '0016', null, '2018-07-10 09:06:10', null, '0', '安徽时旭智能科技有限公司经纬度:117.148456,31.827926', null, null, null);
INSERT INTO `s_person` VALUES ('30', '吴雨', null, null, '0087', null, '2018-07-10 09:06:12', null, '0', '安徽时旭智能科技有限公司经纬度:117.14875,31.827935', null, null, null);
INSERT INTO `s_person` VALUES ('31', '谢清', null, null, '0023', null, '2018-07-10 09:06:13', null, '0', '安徽时旭智能科技有限公司经纬度:117.149279,31.82749', null, null, null);
INSERT INTO `s_person` VALUES ('32', '杨星', null, null, '0088', null, '2018-07-10 09:06:15', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('33', '张都', null, null, '0009', null, '2018-07-10 09:06:17', null, '0', '安徽时旭智能科技有限公司经纬度:117.148132,31.827612', null, null, null);
INSERT INTO `s_person` VALUES ('34', '张志伟', null, null, '0102', null, '2018-07-10 09:06:19', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('35', '郑飞', null, null, '0092', null, '2018-07-10 09:06:22', null, '0', '安徽时旭智能科技有限公司经纬度:117.148153,31.828104', null, null, null);
INSERT INTO `s_person` VALUES ('36', '范贤荣', null, null, '', null, '2018-07-10 09:06:23', null, '0', '安徽时旭智能科技有限公司经纬度:117.148323,31.827165', null, null, null);
INSERT INTO `s_person` VALUES ('37', '李宁', null, null, '', null, '2018-07-10 09:06:26', null, '0', '安徽时旭智能科技有限公司经纬度:117.148325,31.827138', null, null, null);
INSERT INTO `s_person` VALUES ('38', '李宗新（离职）', null, null, '', null, '2018-07-10 09:06:28', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('39', '刘合众', null, null, '', null, '2018-07-10 09:06:28', null, '0', '安徽时旭智能科技有限公司经纬度:117.148326,31.827674', null, null, null);
INSERT INTO `s_person` VALUES ('40', '司益群', null, null, '', null, '2018-07-10 09:06:30', null, '0', '安徽时旭智能科技有限公司经纬度:117.148314,31.827646', null, null, null);
INSERT INTO `s_person` VALUES ('41', '余正其', null, null, '', null, '2018-07-10 09:06:32', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('42', '赵乾坤', null, null, '', null, '2018-07-10 09:06:33', null, '0', '安徽时旭智能科技有限公司经纬度:117.148244,31.82772', null, null, null);
INSERT INTO `s_person` VALUES ('43', '朱翔（离职）', null, null, '', null, '2018-07-10 09:06:35', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('44', '鲍红平（离职）', null, null, '', null, '2018-07-10 09:06:37', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('45', '谢加佳', null, null, '0001', null, '2018-07-10 09:06:37', null, '0', '安徽时旭智能科技有限公司', null, null, null);
INSERT INTO `s_person` VALUES ('46', '徐新新', null, null, '', null, '2018-07-10 09:06:39', null, '0', '安徽时旭智能科技有限公司经纬度:117.148072,31.827915', null, null, null);
INSERT INTO `s_person` VALUES ('47', '王琳', null, null, '', null, '2018-07-10 09:06:41', null, '0', '安徽时旭智能科技有限公司经纬度:117.148195,31.827651', null, null, null);
