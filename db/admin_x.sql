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
-- Table structure for table `operation_log`
--

DROP TABLE IF EXISTS `operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `operator` varchar(50) NOT NULL COMMENT '操作人',
  `module` varchar(50) NOT NULL COMMENT '操作模块',
  `type` varchar(20) NOT NULL COMMENT '操作类型',
  `description` varchar(200) NOT NULL COMMENT '操作描述',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `operation_time` datetime NOT NULL COMMENT '操作时间',
  `duration` bigint DEFAULT NULL COMMENT '执行时长(ms)',
  `ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `params` text COMMENT '请求参数',
  `result` text COMMENT '返回结果',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '操作状态(1成功 0失败)',
  `error_msg` text COMMENT '错误信息',
  PRIMARY KEY (`id`),
  KEY `idx_operator` (`operator`),
  KEY `idx_module` (`module`)
) ENGINE=InnoDB AUTO_INCREMENT=194 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_log`
--

LOCK TABLES `operation_log` WRITE;
/*!40000 ALTER TABLE `operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `operation_log` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
  `dict_code` varchar(100) NOT NULL COMMENT '字典编码',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0：禁用 1：启用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_dict_code` (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES (1,'性别','gender','用户性别字典',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(2,'用户状态','user_status','用户账户状态',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(3,'订单状态','order_status','订单流程状态',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(4,'支付方式','payment_type','支付渠道类型',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(5,'商品类型','product_category','商品分类',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(6,'通知类型','notification_type','系统通知分类',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(7,'审核状态','audit_status','内容审核状态',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(8,'学历等级','education_level','教育程度',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(9,'省份地区','province','中国省级行政区',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(10,'优先级','priority','任务优先级',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(11,'发票类型','invoice_type','发票种类',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(12,'物流状态','logistics_status','快递状态',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(13,'积分类型','points_type','积分变更类型',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(14,'优惠券类型','coupon_type','优惠券分类',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(15,'文章分类','article_category','内容分类',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(16,'设备类型','device_type','终端设备类型',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(17,'结算方式','settlement_method','财务结算方式',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(18,'合同类型','contract_type','合同分类',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(19,'投诉类型','complaint_type','用户投诉分类',1,'2025-06-18 23:15:12','2025-06-18 23:15:12'),(20,'库存状态','inventory_status','库存状态',1,'2025-06-18 23:15:12','2025-06-18 23:15:12');
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_item`
--

DROP TABLE IF EXISTS `sys_dict_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_id` bigint NOT NULL COMMENT '字典ID',
  `item_label` varchar(100) NOT NULL COMMENT '字典项标签',
  `item_value` varchar(100) NOT NULL COMMENT '字典项值',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0：禁用 1：启用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dict_id` (`dict_id`),
  CONSTRAINT `fk_dict_id` FOREIGN KEY (`dict_id`) REFERENCES `sys_dict` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典值表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_item`
--

LOCK TABLES `sys_dict_item` WRITE;
/*!40000 ALTER TABLE `sys_dict_item` DISABLE KEYS */;
INSERT INTO `sys_dict_item` VALUES (1,1,'男','male',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(2,1,'女','female',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(3,1,'未知','unknown',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(4,1,'保密','secret',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(5,1,'其他','other',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(6,2,'正常','normal',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(7,2,'冻结','frozen',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(8,2,'注销','canceled',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(9,2,'未激活','inactive',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(10,2,'黑名单','blacklist',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(11,3,'待付款','unpaid',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(12,3,'已付款','paid',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(13,3,'已发货','shipped',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(14,3,'已完成','completed',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(15,3,'已取消','canceled',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(16,4,'支付宝','alipay',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(17,4,'微信支付','wechat_pay',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(18,4,'银联支付','union_pay',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(19,4,'现金支付','cash',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(20,4,'苹果支付','apple_pay',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(21,5,'电子产品','electronics',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(22,5,'服装鞋帽','clothing',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(23,5,'食品生鲜','food',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(24,5,'家居家装','furniture',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(25,5,'图书音像','books',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(26,6,'系统通知','system',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(27,6,'订单通知','order',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(28,6,'促销通知','promotion',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(29,6,'账户通知','account',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(30,6,'活动通知','activity',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(31,7,'待审核','pending',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(32,7,'审核通过','approved',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(33,7,'审核拒绝','rejected',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(34,7,'已撤回','withdrawn',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(35,7,'审核中','processing',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(36,8,'小学','primary',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(37,8,'初中','junior',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(38,8,'高中','high',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(39,8,'本科','bachelor',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(40,8,'硕士及以上','master',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(41,9,'北京','beijing',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(42,9,'上海','shanghai',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(43,9,'广东','guangdong',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(44,9,'江苏','jiangsu',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(45,9,'浙江','zhejiang',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(46,10,'紧急','urgent',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(47,10,'高','high',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(48,10,'中','medium',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(49,10,'低','low',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(50,10,'普通','normal',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(51,11,'增值税普票','normal',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(52,11,'增值税专票','special',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(53,11,'电子发票','electronic',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(54,11,'纸质发票','paper',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(55,11,'不开票','no_invoice',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(56,12,'待揽收','pending',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(57,12,'运输中','shipping',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(58,12,'派送中','delivering',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(59,12,'已签收','signed',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(60,12,'异常','exception',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(61,13,'签到奖励','sign_in',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(62,13,'消费获得','consume',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(63,13,'活动赠送','activity',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(64,13,'积分兑换','exchange',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(65,13,'管理员调整','admin',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(66,14,'满减券','discount',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(67,14,'折扣券','percentage',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(68,14,'免邮券','free_shipping',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(69,14,'新人券','new_user',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(70,14,'兑换券','exchange',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(71,15,'技术文章','technology',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(72,15,'行业动态','industry',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(73,15,'使用教程','tutorial',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(74,15,'产品更新','update',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(75,15,'客户案例','case',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(76,16,'iOS手机','ios_phone',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(77,16,'Android手机','android_phone',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(78,16,'Windows电脑','windows_pc',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(79,16,'Mac电脑','mac_pc',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(80,16,'平板设备','tablet',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(81,17,'月结','monthly',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(82,17,'周结','weekly',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(83,17,'日结','daily',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(84,17,'即时结算','instant',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(85,17,'季度结算','quarterly',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(86,18,'采购合同','purchase',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(87,18,'销售合同','sale',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(88,18,'服务合同','service',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(89,18,'租赁合同','lease',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(90,18,'合作协议','cooperation',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(91,19,'商品质量','quality',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(92,19,'物流问题','logistics',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(93,19,'服务态度','service',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(94,19,'虚假宣传','false_ad',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(95,19,'其他问题','other',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(96,20,'充足','sufficient',1,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(97,20,'预警','warning',2,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(98,20,'缺货','out_of_stock',3,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(99,20,'预定中','reserved',4,1,'2025-06-18 23:16:11','2025-06-18 23:16:11'),(100,20,'在途','in_transit',5,1,'2025-06-18 23:16:11','2025-06-18 23:16:11');
/*!40000 ALTER TABLE `sys_dict_item` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'dashboard','仪表盘','/dashboard',NULL,'el-icon-monitor',1,1,NULL,0,NULL,0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(2,0,'system','系统管理','/system',NULL,'el-icon-setting',1,1,NULL,0,NULL,1,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(3,0,'monitor','系统监控','/monitor',NULL,'el-icon-data-line',0,1,NULL,0,NULL,2,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(4,0,'tool','系统工具','/tool',NULL,'el-icon-briefcase',0,1,NULL,0,NULL,3,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(5,0,'business','业务管理','/business',NULL,'el-icon-shopping',1,1,NULL,0,NULL,4,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(6,1,'workbench','工作台','/dashboard/workbench','dashboard/workbench','el-icon-s-grid',1,1,NULL,1,NULL,0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(7,2,'user','用户管理','/system/user','system/user/index','el-icon-user',1,1,NULL,1,'system:user:list',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(8,2,'dept','部门管理','/system/dept','system/dept/index','el-icon-office-building',1,1,NULL,1,'system:dept:list',1,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(9,3,'online','在线用户','/monitor/online','monitor/online/index','el-icon-connection',0,1,NULL,1,'monitor:online:list',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(10,4,'codegen','代码生成','/tool/codegen','tool/codegen/index','el-icon-cpu',0,1,NULL,1,'tool:codegen:list',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(11,5,'order','订单管理','/business/order','business/order/index','el-icon-s-order',1,1,NULL,1,'business:order:list',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(12,6,'refresh','刷新工作台',NULL,NULL,NULL,0,1,NULL,2,'dashboard:workbench:refresh',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(13,7,'addUser','新增用户',NULL,NULL,NULL,0,1,NULL,2,'system:user:add',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(14,7,'editUser','编辑用户',NULL,NULL,NULL,0,1,NULL,2,'system:user:edit',1,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(15,8,'addDept','新增部门',NULL,NULL,NULL,0,1,NULL,2,'system:dept:add',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(16,9,'forceLogout','强退用户',NULL,NULL,NULL,0,1,NULL,2,'monitor:online:forceLogout',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(17,10,'genCode','生成代码',NULL,NULL,NULL,0,1,NULL,2,'tool:codegen:gen',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(18,11,'exportOrder','导出订单',NULL,NULL,NULL,0,1,NULL,2,'business:order:export',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(19,11,'detailOrder','订单详情',NULL,NULL,NULL,0,1,NULL,2,'business:order:detail',1,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(20,0,'message','消息中心','/message','message/index','el-icon-chat-dot-round',0,1,NULL,1,NULL,5,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(21,0,'finance','财务管理','/finance','finance/index','el-icon-money',1,1,NULL,1,'finance:manage',6,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(22,0,'report','统计报表','/report','report/index','el-icon-data-analysis',1,1,NULL,1,NULL,7,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(23,2,'config','参数设置','/system/config',NULL,'el-icon-document',1,1,NULL,0,NULL,2,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(24,23,'globalConfig','全局参数','/system/config/global','system/config/global','el-icon-set-up',1,1,NULL,1,'system:config:global',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(25,23,'moduleConfig','模块参数','/system/config/module','system/config/module','el-icon-files',1,1,NULL,1,'system:config:module',1,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(26,24,'saveGlobal','保存配置',NULL,NULL,NULL,0,1,NULL,2,'system:config:global:save',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(27,25,'resetModule','重置配置',NULL,NULL,NULL,0,1,NULL,2,'system:config:module:reset',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(28,0,'help','帮助中心','/help','help/index','el-icon-question',0,1,NULL,1,NULL,8,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(29,0,'log','操作日志','/log','log/index','el-icon-notebook-2',1,1,NULL,1,'system:log:list',9,1,'2025-06-14 16:08:09','2025-06-14 16:08:09'),(30,29,'exportLog','导出日志',NULL,NULL,NULL,0,1,NULL,2,'system:log:export',0,1,'2025-06-14 16:08:09','2025-06-14 16:08:09');
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
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (32,2,1,0,'2025-06-08 16:13:25',NULL),(33,2,2,0,'2025-06-08 16:13:25',NULL),(34,2,3,0,'2025-06-08 16:13:25',NULL),(35,2,4,0,'2025-06-08 16:13:25',NULL),(36,2,5,0,'2025-06-08 16:13:25',NULL),(37,2,6,0,'2025-06-08 16:13:25',NULL),(38,2,7,0,'2025-06-08 16:13:25',NULL),(39,2,8,0,'2025-06-08 16:13:25',NULL),(40,2,9,0,'2025-06-08 16:13:25',NULL),(41,2,10,0,'2025-06-08 16:13:25',NULL),(42,2,11,0,'2025-06-08 16:13:25',NULL),(43,2,12,0,'2025-06-08 16:13:25',NULL),(44,2,13,0,'2025-06-08 16:13:25',NULL),(45,2,14,0,'2025-06-08 16:13:25',NULL),(46,3,2,0,'2025-06-08 16:13:29',NULL),(101,1,1,0,'2025-06-15 16:46:03',NULL),(102,1,3,0,'2025-06-15 16:46:03',NULL),(103,1,4,0,'2025-06-15 16:46:03',NULL),(104,1,5,0,'2025-06-15 16:46:03',NULL),(105,1,6,0,'2025-06-15 16:46:03',NULL),(106,1,7,0,'2025-06-15 16:46:03',NULL),(107,1,8,0,'2025-06-15 16:46:03',NULL),(108,1,9,0,'2025-06-15 16:46:03',NULL),(109,1,10,0,'2025-06-15 16:46:03',NULL),(110,1,11,0,'2025-06-15 16:46:03',NULL),(111,1,12,0,'2025-06-15 16:46:03',NULL),(112,1,13,0,'2025-06-15 16:46:03',NULL),(113,1,14,0,'2025-06-15 16:46:03',NULL),(114,1,15,0,'2025-06-15 16:46:03',NULL),(115,1,16,0,'2025-06-15 16:46:03',NULL),(116,1,17,0,'2025-06-15 16:46:03',NULL),(117,1,18,0,'2025-06-15 16:46:03',NULL),(118,1,19,0,'2025-06-15 16:46:03',NULL),(119,1,20,0,'2025-06-15 16:46:03',NULL),(120,1,21,0,'2025-06-15 16:46:03',NULL),(121,1,22,0,'2025-06-15 16:46:03',NULL),(122,1,30,0,'2025-06-15 16:46:03',NULL),(123,1,29,0,'2025-06-15 16:46:03',NULL);
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
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
  `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0:正常 1:删除)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`) COMMENT '用户角色唯一约束',
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
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

-- Dump completed on 2025-06-18 23:38:11
