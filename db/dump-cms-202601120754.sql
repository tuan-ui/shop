/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: cms
-- ------------------------------------------------------
-- Server version	12.2.1-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `invalidated_token`
--

DROP TABLE IF EXISTS `invalidated_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `invalidated_token` (
  `id` varchar(255) NOT NULL,
  `expiry_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invalidated_token`
--

LOCK TABLES `invalidated_token` WRITE;
/*!40000 ALTER TABLE `invalidated_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `invalidated_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `id` uuid NOT NULL,
  `permission_name` varchar(255) NOT NULL,
  `permission_code` varchar(255) NOT NULL,
  `permission_url` varchar(255) DEFAULT NULL,
  `permission_parent` uuid DEFAULT NULL,
  `position` int(11) DEFAULT 0,
  `is_deleted` tinyint(1) DEFAULT 0,
  `is_menus` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission_code` (`permission_code`),
  KEY `fk_permission_parent` (`permission_parent`),
  CONSTRAINT `fk_permission_parent` FOREIGN KEY (`permission_parent`) REFERENCES `permissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES
('84cb3d26-df41-4359-9dc3-25803f1465e4','permission.delete','ROLE_DELETE',NULL,'a79a843d-ddc1-40e0-b269-5456a015d8a8',3,0,0),
('3beef263-bc97-48a4-9316-4aaab0661c90','permission.delete','USER_DELETE',NULL,'a2697036-9e1f-4726-9008-6e806b7d5ef8',3,0,0),
('e8cb16d0-e487-4016-8c57-4bba9882bc66','permission.edit','USER_EDIT',NULL,'a2697036-9e1f-4726-9008-6e806b7d5ef8',2,0,0),
('a79a843d-ddc1-40e0-b269-5456a015d8a8','sidebar.role','ROLE','/system/roles','36fe608f-60d4-4d3e-80d4-848a29ba50e3',2,0,1),
('ed9e23c7-6f2a-40eb-8386-5eef3a10c16c','permission.add','USER_ADD',NULL,'a2697036-9e1f-4726-9008-6e806b7d5ef8',1,0,0),
('a2697036-9e1f-4726-9008-6e806b7d5ef8','sidebar.user','USER','/system/users','36fe608f-60d4-4d3e-80d4-848a29ba50e3',1,0,1),
('69e7c4ec-5d04-461e-9aa0-6f526116fab7','permission.add','ROLE_ADD',NULL,'a79a843d-ddc1-40e0-b269-5456a015d8a8',1,0,0),
('36fe608f-60d4-4d3e-80d4-848a29ba50e3','sidebar.admin','SYS','/system',NULL,1,0,1),
('57a34aff-399e-462a-a2e3-f3e6e4b0b413','permission.edit','ROLE_EDIT',NULL,'a79a843d-ddc1-40e0-b269-5456a015d8a8',2,0,0),
('3ec6b18c-6b49-4b3b-9fb0-f3f008c6d86e','permission.permission','ROLE_PERMISSION',NULL,'a79a843d-ddc1-40e0-b269-5456a015d8a8',4,0,0);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions_roles`
--

DROP TABLE IF EXISTS `permissions_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions_roles` (
  `is_half` bit(1) NOT NULL,
  `permission_id` uuid NOT NULL,
  `role_id` uuid NOT NULL,
  PRIMARY KEY (`is_half`,`permission_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions_roles`
--

LOCK TABLES `permissions_roles` WRITE;
/*!40000 ALTER TABLE `permissions_roles` DISABLE KEYS */;
INSERT INTO `permissions_roles` VALUES
(0x00,'84cb3d26-df41-4359-9dc3-25803f1465e4','ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9'),
(0x00,'84cb3d26-df41-4359-9dc3-25803f1465e4','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x00,'3beef263-bc97-48a4-9316-4aaab0661c90','ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9'),
(0x00,'3beef263-bc97-48a4-9316-4aaab0661c90','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x00,'e8cb16d0-e487-4016-8c57-4bba9882bc66','ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9'),
(0x00,'e8cb16d0-e487-4016-8c57-4bba9882bc66','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x00,'a79a843d-ddc1-40e0-b269-5456a015d8a8','ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9'),
(0x00,'a79a843d-ddc1-40e0-b269-5456a015d8a8','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x00,'ed9e23c7-6f2a-40eb-8386-5eef3a10c16c','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x00,'a2697036-9e1f-4726-9008-6e806b7d5ef8','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x00,'69e7c4ec-5d04-461e-9aa0-6f526116fab7','ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9'),
(0x00,'69e7c4ec-5d04-461e-9aa0-6f526116fab7','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x00,'36fe608f-60d4-4d3e-80d4-848a29ba50e3','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x00,'57a34aff-399e-462a-a2e3-f3e6e4b0b413','ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9'),
(0x00,'57a34aff-399e-462a-a2e3-f3e6e4b0b413','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x00,'3ec6b18c-6b49-4b3b-9fb0-f3f008c6d86e','ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9'),
(0x00,'3ec6b18c-6b49-4b3b-9fb0-f3f008c6d86e','3820fec3-5a94-4f99-af9a-e8822ce35168'),
(0x01,'a2697036-9e1f-4726-9008-6e806b7d5ef8','ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9'),
(0x01,'36fe608f-60d4-4d3e-80d4-848a29ba50e3','ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9');
/*!40000 ALTER TABLE `permissions_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `expire_time` datetime(6) DEFAULT NULL,
  `refresh_token` varchar(60) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8r12ro1mv3cntrxyndvx99yvl` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_token`
--

LOCK TABLES `refresh_token` WRITE;
/*!40000 ALTER TABLE `refresh_token` DISABLE KEYS */;
INSERT INTO `refresh_token` VALUES
(1,'2026-01-18 21:34:12.680888','a91a883d-a117-4936-9ae1-569d4b62757d','admin'),
(2,'2026-01-15 22:00:52.049941','c2f2ff17-61eb-4f21-bb74-e280713adf88','nv1');
/*!40000 ALTER TABLE `refresh_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` uuid NOT NULL,
  `role_name` varchar(255) NOT NULL,
  `role_code` varchar(50) NOT NULL,
  `role_description` varchar(2000) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `create_at` datetime NOT NULL DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `deleted_at` datetime DEFAULT NULL,
  `create_by` uuid DEFAULT NULL,
  `update_by` uuid DEFAULT NULL,
  `deleted_by` uuid DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES
('ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9','Quản trị','ADMIN','Quản trị hệ thống',1,0,'2025-12-31 22:32:30','2025-12-31 23:21:46',NULL,NULL,'38107268-40b8-4447-903d-284a559db916',NULL),
('3820fec3-5a94-4f99-af9a-e8822ce35168','Nhân viên','NHANVIEN','Nhân viên',1,0,'2025-12-31 23:19:06','2025-12-31 23:19:06',NULL,'38107268-40b8-4447-903d-284a559db916','38107268-40b8-4447-903d-284a559db916',NULL);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` uuid NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `user_code` varchar(255) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `gender` tinyint(1) DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `birthday` datetime(6) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `create_at` datetime NOT NULL DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `deleted_at` datetime DEFAULT NULL,
  `create_by` uuid DEFAULT NULL,
  `update_by` uuid DEFAULT NULL,
  `deleted_by` uuid DEFAULT NULL,
  `identify_code` varchar(20) DEFAULT NULL,
  `is_admin` int(11) DEFAULT NULL,
  `issue_date` datetime(6) DEFAULT NULL,
  `issue_place` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `user_code` (`user_code`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES
('38107268-40b8-4447-903d-284a559db916','admin','admin',NULL,NULL,'$2a$10$yaGv.P3LOP2kpH.JreVbY.SpDH76JmoKGlwkadjvIu2vibE7eDAl2',NULL,'quản trị viên',NULL,'2026-01-01 00:00:00.000000',1,0,'2025-12-30 20:47:48','2026-01-08 21:47:42',NULL,NULL,'38107268-40b8-4447-903d-284a559db916',NULL,'',0,NULL,''),
('eab18e54-9b6b-43ee-9305-ab6d245ca029','nv02','nv02','\\profile\\1767884196405_z6888975142032_e18ad9718cc06962de3a981c5352ab71.jpg',NULL,'$2a$10$yaGv.P3LOP2kpH.JreVbY.SpDH76JmoKGlwkadjvIu2vibE7eDAl2',NULL,'nv02',NULL,'2026-01-01 00:00:00.000000',1,0,'2026-01-08 21:56:36','2026-01-08 22:16:09',NULL,'38107268-40b8-4447-903d-284a559db916','38107268-40b8-4447-903d-284a559db916',NULL,'',0,NULL,''),
('4b58c095-805e-4eb7-abbf-f9fc621776f5','nv1','nv01',NULL,NULL,'$2a$10$yaGv.P3LOP2kpH.JreVbY.SpDH76JmoKGlwkadjvIu2vibE7eDAl2',NULL,'nv1',NULL,'2026-01-01 00:00:00.000000',1,0,'2026-01-08 21:37:12','2026-01-08 22:00:17',NULL,'38107268-40b8-4447-903d-284a559db916','38107268-40b8-4447-903d-284a559db916',NULL,'',0,NULL,'');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_roles` (
  `role_id` uuid NOT NULL,
  `user_id` uuid NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES
('ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9','eab18e54-9b6b-43ee-9305-ab6d245ca029'),
('ce3b7fad-58f9-4dd2-9e38-3b72f841c4b9','4b58c095-805e-4eb7-abbf-f9fc621776f5'),
('3820fec3-5a94-4f99-af9a-e8822ce35168','38107268-40b8-4447-903d-284a559db916'),
('3820fec3-5a94-4f99-af9a-e8822ce35168','eab18e54-9b6b-43ee-9305-ab6d245ca029');
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'cms'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-01-12  7:54:12
