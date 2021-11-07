/*
 Navicat MySQL Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : 127.0.0.1
 Source Database       : niua-easy

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : utf-8

 Date: 01/29/2021 21:22:07 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `QRTZ_BLOB_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Table structure for `QRTZ_CALENDARS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Table structure for `QRTZ_CRON_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Records of `QRTZ_CRON_TRIGGERS`
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('quartzScheduler', 'trigger测试任务', '测试分组', '2/5 * * * * ? *', 'Asia/Shanghai');
COMMIT;

-- ----------------------------
--  Table structure for `QRTZ_FIRED_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Table structure for `QRTZ_JOB_DETAILS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务分组',
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务描述',
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行类',
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Records of `QRTZ_JOB_DETAILS`
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('quartzScheduler', '测试任务', '测试分组', '测试描述', 'tech.niua.quartz.jobs.TestJob', '0', '0', '0', '0', 0xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c770800000010000000017400157363686564756c6572496e7374616e63654e616d6574000f71756172747a5363686564756c65727800);
COMMIT;

-- ----------------------------
--  Table structure for `QRTZ_LOCKS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Records of `QRTZ_LOCKS`
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_LOCKS` VALUES ('quartzScheduler', 'TRIGGER_ACCESS');
COMMIT;

-- ----------------------------
--  Table structure for `QRTZ_PAUSED_TRIGGER_GRPS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Table structure for `QRTZ_SCHEDULER_STATE`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Table structure for `QRTZ_SIMPLE_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Table structure for `QRTZ_SIMPROP_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `INT_PROP_1` int DEFAULT NULL,
  `INT_PROP_2` int DEFAULT NULL,
  `LONG_PROP_1` bigint DEFAULT NULL,
  `LONG_PROP_2` bigint DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Table structure for `QRTZ_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint DEFAULT NULL,
  `PREV_FIRE_TIME` bigint DEFAULT NULL,
  `PRIORITY` int DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `MISFIRE_INSTR` smallint DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
--  Records of `QRTZ_TRIGGERS`
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_TRIGGERS` VALUES ('quartzScheduler', 'trigger测试任务', '测试分组', '测试任务', '测试分组', null, '1611912757000', '1611912752000', '5', 'PAUSED', 'CRON', '1611912613000', '0', null, '0', '');
COMMIT;

-- ----------------------------
--  Table structure for `sys_generator`
-- ----------------------------
DROP TABLE IF EXISTS `sys_generator`;
CREATE TABLE `sys_generator` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `module_name` varchar(80) DEFAULT NULL COMMENT '模块名称',
  `table_name` varchar(80) DEFAULT NULL COMMENT '表名称',
  `ignore_flag` int DEFAULT NULL COMMENT '是否忽略前缀1：是',
  `ignore_prefix` varchar(20) DEFAULT NULL COMMENT '前缀',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_generator`
-- ----------------------------
BEGIN;
INSERT INTO `sys_generator` VALUES ('22', 'test', 't_test', '1', 't_', '2021-01-27 22:37:22', '2021-01-21 15:17:39'), ('25', 'operlog', 'sys_oper_log', '0', '', '2021-01-29 20:42:49', '2021-01-29 20:42:49');
COMMIT;

-- ----------------------------
--  Table structure for `sys_oper_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `operation` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '操作',
  `business_type` varchar(20) DEFAULT '0' COMMENT '业务类型',
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '方法名称',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `oper_name` varchar(80) DEFAULT '' COMMENT '操作用户',
  `params` text COMMENT '参数',
  `ip` varchar(80) DEFAULT '' COMMENT '请求的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci CHECKSUM=1 COMMENT='操作日志记录';

-- ----------------------------
--  Records of `sys_oper_log`
-- ----------------------------
BEGIN;
INSERT INTO `sys_oper_log` VALUES ('1', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:09:43', '', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('2', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:13:41', '', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('3', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:14:18', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('4', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:22:38', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('5', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:23:33', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('6', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:26:27', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('7', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:26:28', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('8', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:27:16', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('9', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:27:18', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('10', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:28:17', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('11', '生成代码', 'GENCODE', 'tech.niua.gen.controller.GeneratorController.autoGenerator', '2021-01-29 20:28:21', 'admin', '[22]', '127.0.0.1'), ('12', '批量删除', 'DELETE', 'tech.niua.gen.controller.GeneratorController.batchDelete', '2021-01-29 20:29:07', 'admin', '[[24]]', '127.0.0.1'), ('13', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:29:07', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('14', '批量删除', 'DELETE', 'tech.niua.gen.controller.GeneratorController.batchDelete', '2021-01-29 20:29:10', 'admin', '[[23]]', '127.0.0.1'), ('15', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:29:10', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('16', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:36:39', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('17', '数据导出', 'EXPORT', 'tech.niua.gen.controller.GeneratorController.export', '2021-01-29 20:36:47', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":null,\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null}]', '127.0.0.1'), ('18', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:42:10', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('19', '保存或修改数据', 'INSERTORUPDATE', 'tech.niua.gen.controller.GeneratorController.saveOrUpdate', '2021-01-29 20:42:49', 'admin', '[{\"id\":25,\"moduleName\":\"operlog\",\"tableName\":\"sys_oper_log\",\"ignoreFlag\":0,\"ignorePrefix\":\"\",\"createTime\":{\"dayOfMonth\":29,\"dayOfWeek\":\"FRIDAY\",\"dayOfYear\":29,\"year\":2021,\"month\":\"JANUARY\",\"monthValue\":1,\"hour\":20,\"minute\":42,\"second\":49,\"nano\":110000000,\"chronology\":{\"id\":\"ISO\",\"calendarType\":\"iso8601\"}},\"updateTime\":{\"dayOfMonth\":29,\"dayOfWeek\":\"FRIDAY\",\"dayOfYear\":29,\"year\":2021,\"month\":\"JANUARY\",\"monthValue\":1,\"hour\":20,\"minute\":42,\"second\":49,\"nano\":110000000,\"chronology\":{\"id\":\"ISO\",\"calendarType\":\"iso8601\"}}}]', '127.0.0.1'), ('20', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:42:49', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('21', '生成代码', 'GENCODE', 'tech.niua.gen.controller.GeneratorController.autoGenerator', '2021-01-29 20:43:03', 'admin', '[25]', '127.0.0.1'), ('22', '生成代码', 'GENCODE', 'tech.niua.gen.controller.GeneratorController.autoGenerator', '2021-01-29 20:46:17', 'admin', '[25]', '127.0.0.1'), ('23', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 20:47:33', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('24', '生成代码', 'GENCODE', 'tech.niua.gen.controller.GeneratorController.autoGenerator', '2021-01-29 20:47:38', 'admin', '[25]', '127.0.0.1'), ('25', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:48:57', 'admin', '[1,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('26', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:49:27', 'admin', '[3,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('27', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:49:29', 'admin', '[2,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('28', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:49:32', 'admin', '[1,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('29', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:56:49', 'admin', '[1,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('30', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:57:08', 'admin', '[2,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('31', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:57:11', 'admin', '[3,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('32', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:57:19', 'admin', '[2,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('33', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:57:22', 'admin', '[3,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('34', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:57:22', 'admin', '[4,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('35', '查询列表', 'LIST', 'tech.niua.admin.operlog.controller.SysOperLogController.index', '2021-01-29 20:57:24', 'admin', '[1,10,{\"id\":null,\"operation\":\"\",\"businessType\":\"\",\"method\":\"\",\"createTime\":null,\"createTimeBegin\":null,\"createTimeEnd\":null,\"operName\":\"\",\"params\":\"\",\"ip\":\"\"}]', '127.0.0.1'), ('36', '列表查询', 'LIST', 'tech.niua.auth.controller.UserController.index', '2021-01-29 21:08:48', 'admin', '[{\"id\":null,\"username\":\"\",\"password\":null,\"available\":null,\"sexType\":0,\"email\":null,\"tel\":null,\"createTime\":null,\"updateTime\":null,\"roles\":null},1,10]', '127.0.0.1'), ('37', '查询列表', 'LIST', 'tech.niua.auth.controller.RoleController.index', '2021-01-29 21:09:30', 'admin', '[{\"id\":null,\"available\":null,\"description\":null,\"name\":\"\",\"resources\":null},1,10]', '127.0.0.1'), ('38', '查询列表', 'LIST', 'tech.niua.auth.controller.ResourceController.listResources', '2021-01-29 21:09:31', 'admin', '[{\"id\":null,\"name\":\"\",\"orderNum\":null,\"permission\":null,\"type\":null,\"url\":null,\"parentId\":1,\"available\":null,\"childResources\":null},1,10]', '127.0.0.1'), ('39', '列表查询', 'LIST', 'tech.niua.auth.controller.UserController.index', '2021-01-29 21:09:34', 'admin', '[{\"id\":null,\"username\":\"\",\"password\":null,\"available\":null,\"sexType\":0,\"email\":null,\"tel\":null,\"createTime\":null,\"updateTime\":null,\"roles\":null},1,10]', '127.0.0.1'), ('40', '更新或添加操作', 'INSERTORUPDATE', 'tech.niua.auth.controller.UserController.saveOrUpdate', '2021-01-29 21:10:21', 'admin', '[{\"id\":0,\"username\":\"222\",\"password\":null,\"available\":true,\"sexType\":0,\"email\":\"\",\"tel\":\"\",\"createTime\":{\"year\":2021,\"month\":\"JANUARY\",\"monthValue\":1,\"dayOfYear\":29,\"hour\":21,\"minute\":10,\"second\":21,\"nano\":454000000,\"dayOfMonth\":29,\"dayOfWeek\":\"FRIDAY\",\"chronology\":{\"id\":\"ISO\",\"calendarType\":\"iso8601\"}},\"updateTime\":{\"year\":2021,\"month\":\"JANUARY\",\"monthValue\":1,\"dayOfYear\":29,\"hour\":21,\"minute\":10,\"second\":21,\"nano\":454000000,\"dayOfMonth\":29,\"dayOfWeek\":\"FRIDAY\",\"chronology\":{\"id\":\"ISO\",\"calendarType\":\"iso8601\"}},\"roles\":null}]', '127.0.0.1'), ('41', '列表查询', 'LIST', 'tech.niua.auth.controller.UserController.index', '2021-01-29 21:10:22', 'admin', '[{\"id\":null,\"username\":\"\",\"password\":null,\"available\":null,\"sexType\":0,\"email\":null,\"tel\":null,\"createTime\":null,\"updateTime\":null,\"roles\":null},1,10]', '127.0.0.1'), ('42', '删除操作', 'DELETE', 'tech.niua.auth.controller.UserController.delete', '2021-01-29 21:10:26', 'admin', '[[34]]', '127.0.0.1'), ('43', '列表查询', 'LIST', 'tech.niua.auth.controller.UserController.index', '2021-01-29 21:10:26', 'admin', '[{\"id\":null,\"username\":\"\",\"password\":null,\"available\":null,\"sexType\":0,\"email\":null,\"tel\":null,\"createTime\":null,\"updateTime\":null,\"roles\":null},1,10]', '127.0.0.1'), ('44', '列表查询', 'LIST', 'tech.niua.auth.controller.UserController.index', '2021-01-29 21:10:45', 'admin', '[{\"id\":null,\"username\":\"\",\"password\":null,\"available\":null,\"sexType\":0,\"email\":null,\"tel\":null,\"createTime\":null,\"updateTime\":null,\"roles\":null},1,10]', '127.0.0.1'), ('45', '查询列表', 'LIST', 'tech.niua.auth.controller.ResourceController.listResources', '2021-01-29 21:10:48', 'admin', '[{\"id\":null,\"name\":\"\",\"orderNum\":null,\"permission\":null,\"type\":null,\"url\":null,\"parentId\":1,\"available\":null,\"childResources\":null},1,10]', '127.0.0.1'), ('46', '查询列表', 'LIST', 'tech.niua.auth.controller.RoleController.index', '2021-01-29 21:10:50', 'admin', '[{\"id\":null,\"available\":null,\"description\":null,\"name\":\"\",\"resources\":null},1,10]', '127.0.0.1'), ('47', '代码生成列表', 'LIST', 'tech.niua.gen.controller.GeneratorController.pageListQuery', '2021-01-29 21:11:25', 'admin', '[{\"id\":null,\"moduleName\":null,\"tableName\":\"\",\"ignoreFlag\":0,\"ignorePrefix\":null,\"createTime\":null,\"updateTime\":null},1,10]', '127.0.0.1'), ('48', '查询列表', 'LIST', 'tech.niua.auth.controller.ResourceController.listResources', '2021-01-29 21:14:21', 'admin', '[{\"id\":null,\"name\":\"\",\"orderNum\":null,\"permission\":null,\"type\":null,\"url\":null,\"parentId\":1,\"available\":null,\"childResources\":null},1,10]', '127.0.0.1'), ('49', '列表查询', 'LIST', 'tech.niua.auth.controller.UserController.index', '2021-01-29 21:14:23', 'admin', '[{\"id\":null,\"username\":\"\",\"password\":null,\"available\":null,\"sexType\":0,\"email\":null,\"tel\":null,\"createTime\":null,\"updateTime\":null,\"roles\":null},1,10]', '127.0.0.1'), ('50', '查询列表', 'LIST', 'tech.niua.auth.controller.RoleController.index', '2021-01-29 21:14:27', 'admin', '[{\"id\":null,\"available\":null,\"description\":null,\"name\":\"\",\"resources\":null},1,10]', '127.0.0.1'), ('51', '查询任务列表', 'LIST', 'tech.niua.quartz.controller.JobController.list', '2021-01-29 21:14:30', 'admin', '[{\"jobName\":null,\"jobGroup\":null,\"description\":null,\"jobClassName\":null,\"cronExpression\":null,\"triggerName\":null,\"triggerState\":null,\"oldJobName\":null,\"oldJobGroup\":null,\"jobDataParam\":null},1,10]', '127.0.0.1'), ('52', '查询任务列表', 'LIST', 'tech.niua.quartz.controller.JobController.list', '2021-01-29 21:16:15', 'admin', '[{\"jobName\":null,\"jobGroup\":null,\"description\":null,\"jobClassName\":null,\"cronExpression\":null,\"triggerName\":null,\"triggerState\":null,\"oldJobName\":null,\"oldJobGroup\":null,\"jobDataParam\":null},1,10]', '127.0.0.1'), ('53', '查询任务列表', 'LIST', 'tech.niua.quartz.controller.JobController.list', '2021-01-29 21:17:28', 'admin', '[{\"jobName\":null,\"jobGroup\":null,\"description\":null,\"jobClassName\":null,\"cronExpression\":null,\"triggerName\":null,\"triggerState\":null,\"oldJobName\":null,\"oldJobGroup\":null,\"jobDataParam\":null},1,10]', '127.0.0.1'), ('54', '查询任务列表', 'LIST', 'tech.niua.quartz.controller.JobController.list', '2021-01-29 21:18:34', 'admin', '[{\"jobName\":null,\"jobGroup\":null,\"description\":null,\"jobClassName\":null,\"cronExpression\":null,\"triggerName\":null,\"triggerState\":null,\"oldJobName\":null,\"oldJobGroup\":null,\"jobDataParam\":null},1,10]', '127.0.0.1'), ('55', '触发任务', 'UPDATE', 'tech.niua.quartz.controller.JobController.trigger', '2021-01-29 21:19:44', 'admin', '[{\"jobName\":\"测试任务\",\"jobGroup\":\"测试分组\",\"description\":null,\"jobClassName\":null,\"cronExpression\":null,\"triggerName\":null,\"triggerState\":null,\"oldJobName\":null,\"oldJobGroup\":null,\"jobDataParam\":null}]', '127.0.0.1'), ('56', '查询任务列表', 'LIST', 'tech.niua.quartz.controller.JobController.list', '2021-01-29 21:19:44', 'admin', '[{\"jobName\":null,\"jobGroup\":null,\"description\":null,\"jobClassName\":null,\"cronExpression\":null,\"triggerName\":null,\"triggerState\":null,\"oldJobName\":null,\"oldJobGroup\":null,\"jobDataParam\":null},1,10]', '127.0.0.1'), ('57', '触发任务', 'UPDATE', 'tech.niua.quartz.controller.JobController.trigger', '2021-01-29 21:20:04', 'admin', '[{\"jobName\":\"测试任务\",\"jobGroup\":\"测试分组\",\"description\":null,\"jobClassName\":null,\"cronExpression\":null,\"triggerName\":null,\"triggerState\":null,\"oldJobName\":null,\"oldJobGroup\":null,\"jobDataParam\":null}]', '127.0.0.1'), ('58', '查询任务列表', 'LIST', 'tech.niua.quartz.controller.JobController.list', '2021-01-29 21:20:04', 'admin', '[{\"jobName\":null,\"jobGroup\":null,\"description\":null,\"jobClassName\":null,\"cronExpression\":null,\"triggerName\":null,\"triggerState\":null,\"oldJobName\":null,\"oldJobGroup\":null,\"jobDataParam\":null},1,10]', '127.0.0.1'), ('59', '列表查询', 'LIST', 'tech.niua.auth.controller.UserController.index', '2021-01-29 21:21:57', 'admin', '[{\"id\":null,\"username\":\"\",\"password\":null,\"available\":null,\"sexType\":0,\"email\":null,\"tel\":null,\"createTime\":null,\"updateTime\":null,\"roles\":null},1,10]', '127.0.0.1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_organization`
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `available` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmeds2u6ae5usj0ko0bqj3k0eo` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `available` bit(1) DEFAULT b'1',
  `name` varchar(255) DEFAULT NULL,
  `order_num` int NOT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3fekum3ead5klp7y4lckn5ohi` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=304 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_resource`
-- ----------------------------
BEGIN;
INSERT INTO `sys_resource` VALUES ('1', b'1', '顶级栏目', '100', '/', '0', null, '0'), ('2', b'1', '权限配置', '8', '/', '0', '', '1'), ('3', b'1', '角色管理', '102', '/role', '0', '/role', '2'), ('4', b'1', '权限管理', '103', '/resource', '0', '/resource', '2'), ('5', b'1', '用户管理', '101', '/user', '0', '/user', '2'), ('6', b'1', '编辑', '100', '/role/editor-role', '1', '/role/editor-role', '3'), ('7', b'1', '添加权限节点', '100', '/resource/add-permission', '1', '/resource/add-permission', '4'), ('8', b'1', '添加管理员', '100', '/user/saveOrUpdate', '1', '/user/saveOrUpdate', '5'), ('9', b'1', '添加角色', '100', '/role/add-role', '1', '/role/add-role', '3'), ('10', b'1', '删除角色', '100', '/role/delete', '1', '/role/delete', '3'), ('11', b'1', '删除用户', '100', '/user/delete', '1', '', '5'), ('12', b'1', '删除权限', '100', '/resource/delete', '1', '/resource/delete', '4'), ('13', b'1', '启用', '100', '/user/available-user', '1', '/user/available-user', '3'), ('14', b'1', '修改管理员密码', '100', '/user/modify-password', '1', '/user/modify-password', '5'), ('16', b'1', '权限编辑', '100', '/resource/saveOrUpdate', '1', '/resource/saveOrUpdate', '4'), ('150', b'1', '编辑管理员信息', '100', '/user/edit-user', '1', '/user/edit-user', '5'), ('275', b'1', '测试', '1', '/test', '0', '', '1'), ('276', b'1', '查看', '1', '/test', '1', '', '275'), ('277', b'1', '保存和编辑权限', '1', '/test/saveOrUpdate', '1', '', '275'), ('278', b'1', '删除权限', '1', '/test/delete', '1', '', '275'), ('279', b'1', '导出权限', '1', '/test/export', '1', '', '275'), ('285', b'1', '定时任务管理', '1', '/quartz', '0', '', '1'), ('286', b'1', '查看', '1', '/quartz', '1', '', '285'), ('287', b'1', '保存', '1', '/quartz/add', '1', '', '285'), ('288', b'1', '删除权限', '1', '/quartz/delete', '1', '', '285'), ('289', b'1', '导出权限', '1', '/quartz/export', '1', '', '285'), ('290', b'1', '暂停', '1', '/quartz/pause', '1', '', '285'), ('291', b'1', '恢复任务', '1', '/quartz/resume', '1', '', '285'), ('292', b'1', '执行任务', '1', '/quartz/trigger', '1', '', '285'), ('293', b'1', '操作日志', '1', '/sysOperLog', '0', '', '2'), ('294', b'1', '查看', '1', '/sysOperLog', '1', '', '293'), ('295', b'1', '保存和编辑权限', '1', '/sysOperLog/saveOrUpdate', '1', '', '293'), ('296', b'1', '删除权限', '1', '/sysOperLog/delete', '1', '', '293'), ('297', b'1', '导出权限', '1', '/sysOperLog/export', '1', '', '293'), ('298', b'1', '代码生成', '1', '/generator', '0', '', '1'), ('299', b'1', '查看', '1', '/generator', '1', '', '298'), ('300', b'1', '保存和编辑权限', '1', '/generator/saveOrUpdate', '1', '', '298'), ('301', b'1', '删除权限', '1', '/generator/delete', '1', '', '298'), ('302', b'1', '导出权限', '1', '/generator/export', '1', '', '298'), ('303', b'1', '生成代码', '1', '/generator/autoGenerator', '1', '', '298');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `available` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1', b'1', '管理员', '管理员');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_resources`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resources`;
CREATE TABLE `sys_role_resources` (
  `sys_role_id` bigint NOT NULL,
  `resources_id` bigint NOT NULL,
  KEY `FKog6jj4v6yh9e1ilxk2mwuk75a` (`resources_id`),
  KEY `FKsqkqfd2hpr5cc2kbrtgoced2w` (`sys_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_role_resources`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_resources` VALUES ('1', '275'), ('1', '1'), ('1', '276'), ('1', '277'), ('1', '278'), ('1', '279'), ('1', '285'), ('1', '286'), ('1', '287'), ('1', '288'), ('1', '289'), ('1', '290'), ('1', '291'), ('1', '292'), ('1', '298'), ('1', '299'), ('1', '300'), ('1', '301'), ('1', '302'), ('1', '303'), ('1', '2'), ('1', '293'), ('1', '294'), ('1', '295'), ('1', '296'), ('1', '297'), ('1', '5'), ('1', '8'), ('1', '11'), ('1', '14'), ('1', '150'), ('1', '3'), ('1', '6'), ('1', '9'), ('1', '10'), ('1', '13'), ('1', '4'), ('1', '7'), ('1', '12'), ('1', '16');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `tel` varchar(255) DEFAULT NULL,
  `sex_type` int DEFAULT NULL COMMENT '性别(0.男,1.女)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', '2017-07-11 17:42:18', '$2a$10$SIU57gfkh/TsIVYALXBNAeDnQzkm652FT9cg4h8wtEfC306uliyYa', '2020-12-11 17:03:20', 'admin', b'1', '1191134106@qq.com', '15030103078', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user_roles`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_roles`;
CREATE TABLE `sys_user_roles` (
  `sys_user_id` bigint NOT NULL,
  `roles_id` bigint NOT NULL,
  KEY `FKdpvc6d7xqpqr43dfuk1s27cqh` (`roles_id`),
  KEY `FKd0ut7sloes191bygyf7a3pk52` (`sys_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_user_roles`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_roles` VALUES ('1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `t_test`
-- ----------------------------
DROP TABLE IF EXISTS `t_test`;
CREATE TABLE `t_test` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试表';

-- ----------------------------
--  Records of `t_test`
-- ----------------------------
BEGIN;
INSERT INTO `t_test` VALUES ('43', '的说法的方法的', '2021-01-21 21:34:44'), ('44', 'sdfadfasdf', '2021-01-21 21:38:42');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
