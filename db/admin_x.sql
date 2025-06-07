-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: admin_x
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept`
(
    `dept_id`     bigint      NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    `parent_id`   bigint      NOT NULL DEFAULT '0' COMMENT '父部门ID',
    `dept_name`   varchar(50) NOT NULL COMMENT '部门名称',
    `sort`        int                  DEFAULT '0' COMMENT '显示顺序',
    `leader`      varchar(50)          DEFAULT NULL COMMENT '负责人',
    `phone`       varchar(20)          DEFAULT NULL COMMENT '联系电话',
    `email`       varchar(50)          DEFAULT NULL COMMENT '邮箱',
    `status`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `is_deleted`  tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    `create_time` datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`dept_id`),
    KEY           `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK
TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type`
(
    `dict_id`     bigint       NOT NULL AUTO_INCREMENT COMMENT '字典ID',
    `dict_name`   varchar(100) NOT NULL COMMENT '字典名称',
    `dict_type`   varchar(100) NOT NULL COMMENT '字典类型',
    `status`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`dict_id`),
    UNIQUE KEY `uniq_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK
TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `parent_id`   bigint      NOT NULL DEFAULT '0' COMMENT '父菜单ID',
    `name`        varchar(64) NOT NULL COMMENT '菜单英文名（路由名称）',
    `title`       varchar(64) NOT NULL COMMENT '菜单中文名（显示名称）',
    `path`        varchar(255)         DEFAULT NULL COMMENT '路由路径',
    `component`   varchar(255)         DEFAULT NULL COMMENT '组件路径',
    `icon`        varchar(64)          DEFAULT NULL COMMENT '图标类名',
    `is_cache`    tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否缓存(0:否 1:是)',
    `is_visible`  tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否显示(0:否 1:是)',
    `redirect`    varchar(255)         DEFAULT NULL COMMENT '重定向路径',
    `menu_type`   tinyint     NOT NULL DEFAULT '0' COMMENT '菜单类型(0:目录 1:菜单 2:按钮)',
    `permission`  varchar(100)         DEFAULT NULL COMMENT '权限标识',
    `sort_order`  int         NOT NULL DEFAULT '0' COMMENT '排序',
    `status`      tinyint     NOT NULL DEFAULT '1' COMMENT '状态(0:停用 1:正常)',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY           `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK
TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log`
(
    `log_id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `title`          varchar(50)  DEFAULT '' COMMENT '模块标题',
    `business_type`  tinyint      DEFAULT '0' COMMENT '业务类型（1新增 2修改 3删除...）',
    `method`         varchar(100) NOT NULL COMMENT '方法名称',
    `request_method` varchar(10)  DEFAULT NULL COMMENT '请求方式',
    `oper_ip`        varchar(50)  DEFAULT NULL COMMENT '操作IP',
    `oper_location`  varchar(255) DEFAULT NULL COMMENT '操作地点',
    `oper_param`     text COMMENT '请求参数',
    `status`         tinyint(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
    `error_msg`      text COMMENT '错误信息',
    `cost_time`      bigint       DEFAULT '0' COMMENT '耗时（毫秒）',
    `oper_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `user_id`        bigint       DEFAULT NULL COMMENT '用户ID',
    PRIMARY KEY (`log_id`),
    KEY              `idx_user_id` (`user_id`),
    KEY              `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK
TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role`
(
    `role_id`     bigint       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name`   varchar(50)  NOT NULL COMMENT '角色名称',
    `role_key`    varchar(100) NOT NULL COMMENT '角色权限字符串',
    `sort`        int          DEFAULT '0' COMMENT '显示顺序',
    `data_scope`  tinyint      DEFAULT '1' COMMENT '数据范围（1：全部数据 2：自定义数据）',
    `status`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
    `is_deleted`  tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`role_id`),
    UNIQUE KEY `uniq_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK
TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user`
(
    `user_id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `dept_id`         bigint       DEFAULT NULL COMMENT '部门ID',
    `username`        varchar(50)  NOT NULL COMMENT '用户名',
    `password`        varchar(100) NOT NULL COMMENT '密码',
    `nickname`        varchar(50)  NOT NULL COMMENT '用户昵称',
    `email`           varchar(50)  DEFAULT NULL COMMENT '用户邮箱',
    `phone`           varchar(20)  DEFAULT NULL COMMENT '手机号码',
    `avatar`          varchar(200) DEFAULT NULL COMMENT '头像地址',
    `gender`          tinyint(1) DEFAULT '0' COMMENT '性别（0未知 1男 2女）',
    `status`          tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `last_login_ip`   varchar(50)  DEFAULT NULL COMMENT '最后登录IP',
    `last_login_time` datetime     DEFAULT NULL COMMENT '最后登录时间',
    `is_deleted`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time`     datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uniq_username` (`username`),
    KEY               `idx_dept_id` (`dept_id`),
    CONSTRAINT `fk_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`dept_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK
TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user`
VALUES (1, NULL, '格子大暑版', '$2a$10$RnlQ2qTMNwT.J1nM9iAuW.5H81k9ByPEPY6dBRcmxO2eoIMZ/E0oG', '格子大暑版',
        'fl9420@qq.com', '18888888888', NULL, 0, 0, NULL, NULL, 0, '2025-05-14 00:46:59', NULL),
       (2, NULL, '格子惊蛰版', '$2a$10$Dz76Xq3A4T0LOkswEUPG0e2oW29TyP438KePkv3GEZ1hiqSGqNE4e', '格子惊蛰版',
        'fl9420@qq.com', '19999999999', 'https://default-avatar-url.png', 0, 0, NULL, NULL, 0, '2025-06-04 07:05:17',
        NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role`
(
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`),
    KEY       `idx_role_id` (`role_id`),
    CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK
TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Dumping routines for database 'admin_x'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-07  0:19:37
