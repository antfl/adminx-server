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
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父部门ID',
  `dept_name` varchar(50) NOT NULL COMMENT '部门名称',
  `sort` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(50) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dept_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `uniq_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父菜单ID',
  `name` varchar(64) NOT NULL COMMENT '菜单英文名（路由名称）',
  `title` varchar(64) NOT NULL COMMENT '菜单中文名（显示名称）',
  `path` varchar(255) DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标类名',
  `is_cache` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否缓存(0:否 1:是)',
  `is_visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否显示(0:否 1:是)',
  `redirect` varchar(255) DEFAULT NULL COMMENT '重定向路径',
  `menu_type` tinyint NOT NULL DEFAULT '0' COMMENT '菜单类型(0:目录 1:菜单 2:按钮)',
  `permission` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态(0:停用 1:正常)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (4,0,'System','系统管理','/system','Layout','system',0,1,NULL,0,NULL,1,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(5,1,'User','用户管理','user','system/user/index','user',1,1,NULL,1,'system:user:list',1,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(6,2,'UserAdd','用户新增',NULL,NULL,NULL,0,1,NULL,2,'system:user:add',1,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(7,2,'UserEdit','用户修改',NULL,NULL,NULL,0,1,NULL,2,'system:user:edit',2,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(8,2,'UserDelete','用户删除',NULL,NULL,NULL,0,1,NULL,2,'system:user:delete',3,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(9,1,'Role','角色管理','role','system/role/index','role',1,1,NULL,1,'system:role:list',2,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(10,5,'RoleAdd','角色新增',NULL,NULL,NULL,0,1,NULL,2,'system:role:add',1,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(11,5,'RoleEdit','角色修改',NULL,NULL,NULL,0,1,NULL,2,'system:role:edit',2,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(12,5,'RoleDelete','角色删除',NULL,NULL,NULL,0,1,NULL,2,'system:role:delete',3,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(13,5,'RoleAssign','分配权限',NULL,NULL,NULL,0,1,NULL,2,'system:role:assign',4,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(14,1,'Menu','菜单管理','menu','system/menu/index','menu',1,1,NULL,1,'system:menu:list',3,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(15,10,'MenuAdd','菜单新增',NULL,NULL,NULL,0,1,NULL,2,'system:menu:add',1,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(16,10,'MenuEdit','菜单修改',NULL,NULL,NULL,0,1,NULL,2,'system:menu:edit',2,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(17,10,'MenuDelete','菜单删除',NULL,NULL,NULL,0,1,NULL,2,'system:menu:delete',3,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(18,0,'Monitor','系统监控','/monitor','Layout','monitor',0,1,NULL,0,NULL,2,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(19,14,'Online','在线用户','online','monitor/online/index','online',1,1,NULL,1,'monitor:online:list',1,1,'2025-06-08 16:11:13','2025-06-08 16:11:13'),(20,14,'Log','操作日志','log','monitor/log/index','log',1,1,NULL,1,'monitor:log:list',2,1,'2025-06-08 16:11:13','2025-06-08 16:11:13');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` tinyint DEFAULT '0' COMMENT '业务类型（1新增 2修改 3删除...）',
  `method` varchar(100) NOT NULL COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方式',
  `oper_ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `oper_location` varchar(255) DEFAULT NULL COMMENT '操作地点',
  `oper_param` text COMMENT '请求参数',
  `status` tinyint(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` text COMMENT '错误信息',
  `cost_time` bigint DEFAULT '0' COMMENT '耗时（毫秒）',
  `oper_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`log_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `sort` int DEFAULT '0' COMMENT '显示顺序',
  `data_scope` tinyint DEFAULT '1' COMMENT '数据范围（1：全部数据 2：自定义数据）',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uniq_role_key` (`role_key`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','super_admin',1,1,0,'拥有系统所有权限，可管理所有功能',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(2,'系统管理员','admin',2,1,0,'管理系统用户、角色和基础设置',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(3,'普通用户','user',3,2,0,'拥有基本使用权限',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(4,'部门管理员','dept_admin',4,2,0,'管理本部门用户和数据',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(5,'财务专员','finance',5,2,0,'负责财务相关操作和报表查看',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(6,'审计专员','auditor',6,2,0,'负责系统操作审计和日志审查',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(7,'开发人员','developer',7,2,0,'负责系统开发和维护',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(8,'测试人员','tester',8,2,0,'负责系统功能测试',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(9,'访客','guest',9,2,0,'只读权限，不能修改数据',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(10,'归档角色','archived',10,2,1,'已停用的角色示例',0,'2025-06-08 15:41:48','2025-06-08 15:41:48'),(11,'废弃角色','deprecated',11,2,0,'已删除的角色示例',1,'2024-01-01 00:00:00','2024-01-02 00:00:00');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_role_menu` (`role_id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,1,4,0,'2025-06-08 16:13:19',NULL),(2,1,18,0,'2025-06-08 16:13:19',NULL),(3,1,5,0,'2025-06-08 16:13:19',NULL),(4,1,9,0,'2025-06-08 16:13:19',NULL),(5,1,14,0,'2025-06-08 16:13:19',NULL),(6,1,6,0,'2025-06-08 16:13:19',NULL),(7,1,7,0,'2025-06-08 16:13:19',NULL),(8,1,8,0,'2025-06-08 16:13:19',NULL),(9,1,10,0,'2025-06-08 16:13:19',NULL),(10,1,11,0,'2025-06-08 16:13:19',NULL),(11,1,12,0,'2025-06-08 16:13:19',NULL),(12,1,13,0,'2025-06-08 16:13:19',NULL),(13,1,15,0,'2025-06-08 16:13:19',NULL),(14,1,16,0,'2025-06-08 16:13:19',NULL),(15,1,17,0,'2025-06-08 16:13:19',NULL),(16,1,19,0,'2025-06-08 16:13:19',NULL),(17,1,20,0,'2025-06-08 16:13:19',NULL),(32,2,1,0,'2025-06-08 16:13:25',NULL),(33,2,2,0,'2025-06-08 16:13:25',NULL),(34,2,3,0,'2025-06-08 16:13:25',NULL),(35,2,4,0,'2025-06-08 16:13:25',NULL),(36,2,5,0,'2025-06-08 16:13:25',NULL),(37,2,6,0,'2025-06-08 16:13:25',NULL),(38,2,7,0,'2025-06-08 16:13:25',NULL),(39,2,8,0,'2025-06-08 16:13:25',NULL),(40,2,9,0,'2025-06-08 16:13:25',NULL),(41,2,10,0,'2025-06-08 16:13:25',NULL),(42,2,11,0,'2025-06-08 16:13:25',NULL),(43,2,12,0,'2025-06-08 16:13:25',NULL),(44,2,13,0,'2025-06-08 16:13:25',NULL),(45,2,14,0,'2025-06-08 16:13:25',NULL),(46,3,2,0,'2025-06-08 16:13:29',NULL);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `gender` tinyint(1) DEFAULT '0' COMMENT '性别（0未知 1男 2女）',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uniq_username` (`username`),
  KEY `idx_dept_id` (`dept_id`),
  CONSTRAINT `fk_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`dept_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (3,NULL,'格子大暑版','$2a$10$b/22SH/M3GrxJtxSvlCH8O2ALHDDIQsj9wkTcx0plWhMxzJPI7Zdq','格子大暑版','fl9420@qq.com',NULL,'https://default-avatar-url.png',0,0,NULL,NULL,0,'2025-06-07 23:34:31',NULL),(4,NULL,'admin','$2a$10$8cnhsRWKs3u.VjDqntJ5B.NV2A0eJZk7Ym0tQ2EwTKqOH6y.yycEq','admin','fl9420@qq.com',NULL,'https://default-avatar-url.png',0,0,NULL,NULL,0,'2025-06-08 00:04:39',NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

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

-- Dump completed on 2025-06-08 19:51:21
