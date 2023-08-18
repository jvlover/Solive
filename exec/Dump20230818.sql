-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: solive
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `apply`
--

DROP TABLE IF EXISTS `apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply` (
  `id` bigint NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `estimated_time` int NOT NULL,
  `solve_point` int NOT NULL,
  `time` datetime(6) DEFAULT NULL,
  `question_id` bigint DEFAULT NULL,
  `teacher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK54q5rymfsy6fiowlkt4vnjnki` (`question_id`),
  KEY `FKbubmbpfv96o72tq69yw0jk3k2` (`teacher_id`),
  CONSTRAINT `FK54q5rymfsy6fiowlkt4vnjnki` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`),
  CONSTRAINT `FKbubmbpfv96o72tq69yw0jk3k2` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply`
--

LOCK TABLES `apply` WRITE;
/*!40000 ALTER TABLE `apply` DISABLE KEYS */;
INSERT INTO `apply` VALUES (561,NULL,10,1000,'2023-08-18 01:45:55.836048',878,656),(562,NULL,5,1200,'2023-08-18 01:46:33.748359',878,657),(563,NULL,20,500,'2023-08-18 01:51:29.264908',878,658),(564,NULL,5,1500,'2023-08-18 01:59:24.447353',879,658),(565,NULL,10,1000,'2023-08-18 02:12:55.281698',880,656),(566,NULL,5,1200,'2023-08-18 02:13:24.111279',880,657),(567,NULL,20,500,'2023-08-18 02:14:08.916156',880,658),(568,NULL,5,1000,'2023-08-18 02:20:02.119553',881,657),(602,NULL,1,1000,'2023-08-18 04:07:06.542021',902,656),(652,NULL,5,1000,'2023-08-18 05:03:28.735214',882,656),(653,NULL,12,1234,'2023-08-18 05:16:48.237938',952,656),(654,NULL,20,1100,'2023-08-18 05:21:29.577943',953,1153),(655,NULL,123,123,'2023-08-18 05:55:26.096812',954,656),(656,NULL,123,12,'2023-08-18 05:57:09.566647',955,656),(657,NULL,123,12,'2023-08-18 06:18:16.795543',956,656),(658,NULL,123,123,'2023-08-18 06:21:10.696553',957,656),(659,NULL,12,12,'2023-08-18 06:34:47.166631',958,656),(660,NULL,23,3,'2023-08-18 06:41:20.443410',959,656),(661,NULL,123,213,'2023-08-18 06:58:18.392260',960,656),(662,NULL,5,10,'2023-08-18 07:16:23.253098',883,656),(663,NULL,5,10,'2023-08-18 07:18:00.187716',885,656),(664,NULL,5,590,'2023-08-18 07:20:15.243641',893,656),(665,NULL,6,400,'2023-08-18 07:23:12.939215',884,953),(666,NULL,6,500,'2023-08-18 08:22:12.858982',894,953),(667,NULL,5,400,'2023-08-18 08:32:53.021662',888,953),(668,NULL,5,300,'2023-08-18 08:45:59.785556',961,953);
/*!40000 ALTER TABLE `apply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apply_seq`
--

DROP TABLE IF EXISTS `apply_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply_seq`
--

LOCK TABLES `apply_seq` WRITE;
/*!40000 ALTER TABLE `apply_seq` DISABLE KEYS */;
INSERT INTO `apply_seq` VALUES (751);
/*!40000 ALTER TABLE `apply_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `id` bigint NOT NULL,
  `content` text NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `last_update_time` datetime(6) DEFAULT NULL,
  `like_count` bigint NOT NULL DEFAULT '0',
  `report_count` int NOT NULL DEFAULT '0',
  `time` datetime(6) DEFAULT NULL,
  `title` varchar(40) NOT NULL,
  `view_count` bigint NOT NULL DEFAULT '0',
  `master_code_id` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKahvc6yl8xtdnvdailsgmc16r8` (`master_code_id`),
  KEY `FKbc2qerk3l47javnl2yvn51uoi` (`user_id`),
  CONSTRAINT `FKahvc6yl8xtdnvdailsgmc16r8` FOREIGN KEY (`master_code_id`) REFERENCES `master_code` (`id`),
  CONSTRAINT `FKbc2qerk3l47javnl2yvn51uoi` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (52,'2023년 8월 17일 오후 15시부터 solive 서비스가 오픈되었습니다.',NULL,'2023-08-17 16:11:18.312174',0,0,'2023-08-17 16:11:18.312277','서버 가오픈 시작합니다',6,2000,0),(103,'문제 해결은, Solive! 모르는 문제에 대한 선생님을 바로 구해보세요. 1대1 즉석 과외 신청, 어떻게 풀어야할지 모르겠다면, 문제를 카메라로 찍어서 올려보세요. 솔라이브 선생님들의 풀이를 화상 강의를 통해 지도받을 수 있어요!',NULL,'2023-08-17 16:35:49.918279',0,0,'2023-08-17 16:35:49.918295','Solive Service 소개',3,2000,0),(252,'2023년 8월 17일 22시부터 2023년 8월 18일 9시까지 서버 점검이 있겠습니다.',NULL,'2023-08-17 21:28:54.708320',0,0,'2023-08-17 21:28:54.708333','8월 17일 점검 공지',1,2000,0),(253,'2023년 8월 18년 10시부터 Solive 서비스 정식 오픈이 있습니다. 많은 이용 부탁드립니다.',NULL,'2023-08-17 21:29:40.957088',0,0,'2023-08-17 21:29:40.957101','8월 18일 10시 서비스 오픈',1,2000,0);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_like`
--

DROP TABLE IF EXISTS `article_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_like` (
  `article_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`article_id`,`user_id`),
  KEY `FKm3fo00rg4vr6dbnsbu31ep972` (`user_id`),
  CONSTRAINT `FKabthli6g1qjriusniw93pbesw` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `FKm3fo00rg4vr6dbnsbu31ep972` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_like`
--

LOCK TABLES `article_like` WRITE;
/*!40000 ALTER TABLE `article_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_picture`
--

DROP TABLE IF EXISTS `article_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_picture` (
  `id` bigint NOT NULL,
  `content_type` varchar(100) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `file_name` varchar(255) NOT NULL,
  `time` datetime(6) DEFAULT NULL,
  `article_id` bigint DEFAULT NULL,
  `original_name` varchar(100) NOT NULL,
  `path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrouyq5ucmpxxkg42u876plat9` (`article_id`),
  CONSTRAINT `FKrouyq5ucmpxxkg42u876plat9` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_picture`
--

LOCK TABLES `article_picture` WRITE;
/*!40000 ALTER TABLE `article_picture` DISABLE KEYS */;
INSERT INTO `article_picture` VALUES (1,'image/png',NULL,'article/b4f85350-5c95-4d92-8bd1-45e6a9bcdb7f.png','2023-08-17 16:11:18.320947',52,'가오픈.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/article/b4f85350-5c95-4d92-8bd1-45e6a9bcdb7f.png'),(3,'image/png',NULL,'article/27aeea9c-b492-4a50-8e13-6d5d0d02c2ab.png','2023-08-17 16:35:49.921362',103,'배너 2.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/article/27aeea9c-b492-4a50-8e13-6d5d0d02c2ab.png');
/*!40000 ALTER TABLE `article_picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_picture_seq`
--

DROP TABLE IF EXISTS `article_picture_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_picture_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_picture_seq`
--

LOCK TABLES `article_picture_seq` WRITE;
/*!40000 ALTER TABLE `article_picture_seq` DISABLE KEYS */;
INSERT INTO `article_picture_seq` VALUES (201);
/*!40000 ALTER TABLE `article_picture_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_report`
--

DROP TABLE IF EXISTS `article_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_report` (
  `article_id` bigint NOT NULL,
  `user_report_id` bigint NOT NULL,
  `content` varchar(255) NOT NULL,
  `time` datetime(6) DEFAULT NULL,
  `user_reported_id` bigint DEFAULT NULL,
  PRIMARY KEY (`article_id`,`user_report_id`),
  KEY `FKg3uyk7fw0th961qtas5ieu0c5` (`user_report_id`),
  KEY `FK91tumwvocmfpjjxa54l5wl3gm` (`user_reported_id`),
  CONSTRAINT `FK91tumwvocmfpjjxa54l5wl3gm` FOREIGN KEY (`user_reported_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKg3uyk7fw0th961qtas5ieu0c5` FOREIGN KEY (`user_report_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKk4x3soahjiqlnhuxhuihk0sgh` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_report`
--

LOCK TABLES `article_report` WRITE;
/*!40000 ALTER TABLE `article_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_seq`
--

DROP TABLE IF EXISTS `article_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_seq`
--

LOCK TABLES `article_seq` WRITE;
/*!40000 ALTER TABLE `article_seq` DISABLE KEYS */;
INSERT INTO `article_seq` VALUES (351);
/*!40000 ALTER TABLE `article_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `student_id` bigint NOT NULL,
  `teacher_id` bigint NOT NULL,
  `delated_at` datetime(6) DEFAULT NULL,
  `time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`student_id`,`teacher_id`),
  KEY `FKlq63f0tekd2d8smiuh0uci8rv` (`teacher_id`),
  CONSTRAINT `FKkpvweq4jn8copei2bewx29x1f` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `FKlq63f0tekd2d8smiuh0uci8rv` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite`
--

LOCK TABLES `favorite` WRITE;
/*!40000 ALTER TABLE `favorite` DISABLE KEYS */;
INSERT INTO `favorite` VALUES (952,656,NULL,'2023-08-18 05:04:17.677108'),(952,953,NULL,'2023-08-17 12:26:16.154204'),(1052,656,NULL,'2023-08-18 06:26:03.803419'),(1105,656,NULL,'2023-08-18 05:18:08.573767'),(1152,1153,NULL,'2023-08-18 05:22:30.946816');
/*!40000 ALTER TABLE `favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `master_code`
--

DROP TABLE IF EXISTS `master_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `master_code` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `master_code`
--

LOCK TABLES `master_code` WRITE;
/*!40000 ALTER TABLE `master_code` DISABLE KEYS */;
INSERT INTO `master_code` VALUES (1,'학생'),(2,'강사'),(3,'운영자'),(11,'로그아웃'),(12,'로그인'),(13,'강의 중'),(1000,'없음'),(1111,'지수로그'),(1112,'삼각함수'),(1113,'극한'),(1121,'극한'),(1122,'미분'),(1123,'적분'),(1131,'확률'),(1132,'통계'),(1141,'이차곡선'),(1142,'벡터'),(1143,'공간도형'),(1151,'기타수학'),(1211,'기초과학'),(2000,'공지사항'),(2100,'사진 없는 게시글'),(2101,'사진 있는 게시글');
/*!40000 ALTER TABLE `master_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matched`
--

DROP TABLE IF EXISTS `matched`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matched` (
  `id` bigint NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `extension_count` int NOT NULL DEFAULT '0',
  `matched_time` datetime(6) DEFAULT NULL,
  `reported_at` datetime(6) DEFAULT NULL,
  `solve_point` int NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `video_url` varchar(255) DEFAULT NULL,
  `question_id` bigint DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  `teacher_id` bigint DEFAULT NULL,
  `session_id` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4h6k7xibadjv0hnmwfd4lpwbs` (`question_id`),
  KEY `FKk6fuqr4h1g3ry2rn5s1dk71fx` (`student_id`),
  KEY `FK2esx6d19gufp42txu2ty95yuo` (`teacher_id`),
  CONSTRAINT `FK2esx6d19gufp42txu2ty95yuo` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`),
  CONSTRAINT `FK4h6k7xibadjv0hnmwfd4lpwbs` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`),
  CONSTRAINT `FKk6fuqr4h1g3ry2rn5s1dk71fx` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matched`
--

LOCK TABLES `matched` WRITE;
/*!40000 ALTER TABLE `matched` DISABLE KEYS */;
INSERT INTO `matched` VALUES (673,NULL,0,'2023-08-18 01:56:53.587437',NULL,500,NULL,NULL,878,1107,658,'tjdgh12_2023-08-17T165653584492939'),(674,'2023-08-18 01:58:02',0,'2023-08-18 01:56:56.768846',NULL,500,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/tjdgh12_2023-08-17T165656765482280/tjdgh12_2023-08-17T165656765482280.mp4',878,1107,658,'tjdgh12_2023-08-17T165656765482280'),(675,NULL,0,'2023-08-18 01:59:31.462349',NULL,1500,NULL,NULL,879,1107,658,'tjdgh12_2023-08-17T165931459891209'),(676,'2023-08-18 02:02:16',0,'2023-08-18 01:59:33.044093',NULL,1500,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/tjdgh12_2023-08-17T165933042299735/tjdgh12_2023-08-17T165933042299735.mp4',879,1107,658,'tjdgh12_2023-08-17T165933042299735'),(677,NULL,0,'2023-08-18 02:15:11.585767',NULL,1000,NULL,NULL,880,1105,656,'wngh1698_2023-08-17T171511583299473'),(678,'2023-08-18 02:16:35',0,'2023-08-18 02:15:13.257558',NULL,1000,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/wngh1698_2023-08-17T171513254996214/wngh1698_2023-08-17T171513254996214.mp4',880,1105,656,'wngh1698_2023-08-17T171513254996214'),(679,NULL,0,'2023-08-18 02:20:14.822242',NULL,1000,NULL,NULL,881,1105,657,'wngh1698_2023-08-17T172014819313103'),(680,'2023-08-18 02:25:05',0,'2023-08-18 02:20:16.418320',NULL,1000,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/wngh1698_2023-08-17T172016414639878/wngh1698_2023-08-17T172016414639878.mp4',881,1105,657,'wngh1698_2023-08-17T172016414639878'),(702,NULL,0,'2023-08-18 05:03:36.223786',NULL,1000,NULL,NULL,882,952,656,'dign_2023-08-17T200336210764297'),(703,'2023-08-18 05:04:06',0,'2023-08-18 05:03:37.431954',NULL,1000,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/dign_2023-08-17T200337428918721/dign_2023-08-17T200337428918721.mp4',882,952,656,'dign_2023-08-17T200337428918721'),(704,NULL,0,'2023-08-18 05:17:02.419743',NULL,1234,NULL,NULL,952,1105,656,'wngh1698_2023-08-17T201702413087735'),(705,'2023-08-18 05:18:00',0,'2023-08-18 05:17:03.493265',NULL,1234,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/wngh1698_2023-08-17T201703489669921/wngh1698_2023-08-17T201703489669921.mp4',952,1105,656,'wngh1698_2023-08-17T201703489669921'),(706,NULL,0,'2023-08-18 05:22:04.695888',NULL,1100,NULL,NULL,953,1152,1153,'test10_2023-08-17T202204694141669'),(707,'2023-08-18 05:22:26',0,'2023-08-18 05:22:05.722549',NULL,1100,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/test10_2023-08-17T202205720744315/test10_2023-08-17T202205720744315.mp4',953,1152,1153,'test10_2023-08-17T202205720744315'),(708,NULL,0,'2023-08-18 05:55:38.335233',NULL,123,NULL,NULL,954,1052,656,'shtjdgh00_2023-08-17T205538331879653'),(709,NULL,0,'2023-08-18 05:55:39.601647',NULL,123,NULL,NULL,954,1052,656,'shtjdgh00_2023-08-17T205539598588723'),(710,NULL,0,'2023-08-18 05:57:18.111786',NULL,12,NULL,NULL,955,1052,656,'shtjdgh00_2023-08-17T205718109641825'),(711,NULL,0,'2023-08-18 05:58:55.580396',NULL,12,NULL,NULL,955,1052,656,'shtjdgh00_2023-08-17T205855578234039'),(712,NULL,0,'2023-08-18 06:17:42.262045',NULL,12,NULL,NULL,955,1052,656,'shtjdgh00_2023-08-17T211742259762481'),(713,NULL,0,'2023-08-18 06:18:42.502512',NULL,12,NULL,NULL,956,1052,656,'shtjdgh00_2023-08-17T211842500217075'),(714,'2023-08-18 06:20:38',0,'2023-08-18 06:18:47.518701',NULL,12,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/shtjdgh00_2023-08-17T211847516699174/shtjdgh00_2023-08-17T211847516699174.mp4',956,1052,656,'shtjdgh00_2023-08-17T211847516699174'),(715,NULL,0,'2023-08-18 06:19:28.539669',NULL,12,NULL,NULL,956,1052,656,'shtjdgh00_2023-08-17T211928537116709'),(716,NULL,0,'2023-08-18 06:20:30.562668',NULL,12,NULL,NULL,956,1052,656,'shtjdgh00_2023-08-17T212030560848603'),(717,NULL,0,'2023-08-18 06:21:21.062484',NULL,123,NULL,NULL,957,1052,656,'shtjdgh00_2023-08-17T212121059347766'),(718,NULL,0,'2023-08-18 06:21:22.240440',NULL,123,NULL,NULL,957,1052,656,'shtjdgh00_2023-08-17T212122238071606'),(719,NULL,0,'2023-08-18 06:22:19.553378',NULL,123,NULL,NULL,957,1052,656,'shtjdgh00_2023-08-17T212219551552238'),(720,NULL,0,'2023-08-18 06:34:55.669766',NULL,12,NULL,NULL,958,1052,656,'shtjdgh00_2023-08-17T213455667875082'),(721,'2023-08-18 06:36:32',0,'2023-08-18 06:34:56.952836',NULL,12,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/shtjdgh00_2023-08-17T213456950818130/shtjdgh00_2023-08-17T213456950818130.mp4',958,1052,656,'shtjdgh00_2023-08-17T213456950818130'),(722,NULL,0,'2023-08-18 06:41:40.976078',NULL,3,NULL,NULL,959,1052,656,'shtjdgh00_2023-08-17T214140974207693'),(723,NULL,0,'2023-08-18 06:41:42.127003',NULL,3,NULL,NULL,959,1052,656,'shtjdgh00_2023-08-17T214142125214176'),(724,NULL,0,'2023-08-18 06:58:26.082994',NULL,213,NULL,NULL,960,666,656,'test123_2023-08-17T215826080963206'),(725,NULL,0,'2023-08-18 06:58:27.431189',NULL,213,NULL,NULL,960,666,656,'test123_2023-08-17T215827429132270'),(726,NULL,0,'2023-08-18 07:16:27.353648',NULL,10,NULL,NULL,883,952,656,'dign_2023-08-17T221627351593175'),(727,NULL,0,'2023-08-18 07:16:34.935315',NULL,10,NULL,NULL,883,952,656,'dign_2023-08-17T221634933256314'),(728,NULL,0,'2023-08-18 07:17:37.853763',NULL,1000,NULL,NULL,902,952,656,'dign_2023-08-17T221737851928780'),(729,'2023-08-18 07:17:42',0,'2023-08-18 07:17:38.909763',NULL,1000,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/dign_2023-08-17T221738907905571/dign_2023-08-17T221738907905571.mp4',902,952,656,'dign_2023-08-17T221738907905571'),(730,NULL,0,'2023-08-18 07:18:06.045249',NULL,10,NULL,NULL,885,952,656,'dign_2023-08-17T221806042928513'),(731,'2023-08-18 07:18:29',0,'2023-08-18 07:18:06.995551',NULL,10,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/dign_2023-08-17T221806993448863/dign_2023-08-17T221806993448863.mp4',885,952,656,'dign_2023-08-17T221806993448863'),(732,NULL,0,'2023-08-18 07:20:19.265146',NULL,590,NULL,NULL,893,952,656,'dign_2023-08-17T222019262687123'),(733,NULL,0,'2023-08-18 07:20:21.103422',NULL,590,NULL,NULL,893,952,656,'dign_2023-08-17T222021100681964'),(734,NULL,0,'2023-08-18 07:23:45.601162',NULL,400,NULL,NULL,884,952,953,'dign_2023-08-17T222345598670687'),(735,'2023-08-18 07:24:01',0,'2023-08-18 07:23:47.220700',NULL,400,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/dign_2023-08-17T222347218167084/dign_2023-08-17T222347218167084.mp4',884,952,953,'dign_2023-08-17T222347218167084'),(736,NULL,0,'2023-08-18 08:30:53.545709',NULL,500,NULL,NULL,894,952,953,'dign_2023-08-17T233053543952612'),(737,'2023-08-18 08:31:57',0,'2023-08-18 08:30:54.864626',NULL,500,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/dign_2023-08-17T233054853307058/dign_2023-08-17T233054853307058.mp4',894,952,953,'dign_2023-08-17T233054853307058'),(738,NULL,0,'2023-08-18 08:37:17.910156',NULL,400,NULL,NULL,888,952,953,'dign_2023-08-17T233717908342940'),(739,NULL,0,'2023-08-18 08:37:19.345758',NULL,400,NULL,NULL,888,952,953,'dign_2023-08-17T233719343916605'),(740,NULL,0,'2023-08-18 08:46:21.853425',NULL,300,NULL,NULL,961,952,953,'dign_2023-08-17T234621851605859'),(741,'2023-08-18 08:46:45',0,'2023-08-18 08:46:22.879326',NULL,300,NULL,'https://i9a107.p.ssafy.io:8447/openvidu/recordings/dign_2023-08-17T234622877558142/dign_2023-08-17T234622877558142.mp4',961,952,953,'dign_2023-08-17T234622877558142');
/*!40000 ALTER TABLE `matched` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matched_seq`
--

DROP TABLE IF EXISTS `matched_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matched_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matched_seq`
--

LOCK TABLES `matched_seq` WRITE;
/*!40000 ALTER TABLE `matched_seq` DISABLE KEYS */;
INSERT INTO `matched_seq` VALUES (801);
/*!40000 ALTER TABLE `matched_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL,
  `content` varchar(255) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `read_at` datetime(6) DEFAULT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` varchar(255) NOT NULL,
  `receive_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf2fmll4u9unq64oyod3066srt` (`receive_user_id`),
  CONSTRAINT `FKf2fmll4u9unq64oyod3066srt` FOREIGN KEY (`receive_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1332,'일더하기 일 정답을 알려주세요',NULL,NULL,'2023-08-17 16:45:55','성호최고님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1107),(1333,'일더하기 일 정답을 알려주세요',NULL,NULL,'2023-08-17 16:46:33','성호최고님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1107),(1334,'일더하기 일 정답을 알려주세요',NULL,NULL,'2023-08-17 16:51:29','성호최고님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1107),(1335,'tjdgh12_2023-08-17T165653584492939',NULL,NULL,'2023-08-17 16:56:53','갈색강아지님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 tjdgh12_2023-08-17T165653584492939 입니다.',658),(1336,'tjdgh12_2023-08-17T165656765482280',NULL,NULL,'2023-08-17 16:56:56','갈색강아지님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 tjdgh12_2023-08-17T165656765482280 입니다.',658),(1337,'1더하기 1 알려달라고!',NULL,NULL,'2023-08-17 16:59:24','성호최고님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1107),(1338,'tjdgh12_2023-08-17T165931459891209',NULL,NULL,'2023-08-17 16:59:31','갈색강아지님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 tjdgh12_2023-08-17T165931459891209 입니다.',658),(1339,'tjdgh12_2023-08-17T165933042299735',NULL,NULL,'2023-08-17 16:59:33','갈색강아지님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 tjdgh12_2023-08-17T165933042299735 입니다.',658),(1340,'문제를 잘 모르겠습니다 도와주세요!',NULL,NULL,'2023-08-17 17:12:55','끼우수몽님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1105),(1341,'문제를 잘 모르겠습니다 도와주세요!',NULL,NULL,'2023-08-17 17:13:24','끼우수몽님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1105),(1342,'문제를 잘 모르겠습니다 도와주세요!',NULL,NULL,'2023-08-17 17:14:08','끼우수몽님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1105),(1343,'wngh1698_2023-08-17T171511583299473',NULL,NULL,'2023-08-17 17:15:11','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 wngh1698_2023-08-17T171511583299473 입니다.',656),(1344,'wngh1698_2023-08-17T171513254996214',NULL,NULL,'2023-08-17 17:15:13','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 wngh1698_2023-08-17T171513254996214 입니다.',656),(1345,'선생님들 제발 도와주세요',NULL,NULL,'2023-08-17 17:20:02','끼우수몽님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1105),(1346,'wngh1698_2023-08-17T172014819313103',NULL,NULL,'2023-08-17 17:20:14','어묵이좋아님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 wngh1698_2023-08-17T172014819313103 입니다.',657),(1347,'wngh1698_2023-08-17T172016414639878',NULL,NULL,'2023-08-17 17:20:16','어묵이좋아님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 wngh1698_2023-08-17T172016414639878 입니다.',657),(1352,'최종 테스트',NULL,NULL,'2023-08-17 19:07:06','학생123님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',952),(1402,'1번',NULL,NULL,'2023-08-17 20:03:28','학생123님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',952),(1403,'dign_2023-08-17T200336210764297',NULL,NULL,'2023-08-17 20:03:36','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T200336210764297 입니다.',656),(1404,'dign_2023-08-17T200337428918721',NULL,NULL,'2023-08-17 20:03:37','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T200337428918721 입니다.',656),(1405,'사차함수라니 도대체 뭘까요',NULL,NULL,'2023-08-17 20:16:48','끼우수몽님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1105),(1406,'wngh1698_2023-08-17T201702413087735',NULL,NULL,'2023-08-17 20:17:02','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 wngh1698_2023-08-17T201702413087735 입니다.',656),(1407,'wngh1698_2023-08-17T201703489669921',NULL,NULL,'2023-08-17 20:17:03','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 wngh1698_2023-08-17T201703489669921 입니다.',656),(1408,'제발 알려주세요',NULL,NULL,'2023-08-17 20:21:29','간다드래프트2님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1152),(1409,'test10_2023-08-17T202204694141669',NULL,NULL,'2023-08-17 20:22:04','saf님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 test10_2023-08-17T202204694141669 입니다.',1153),(1410,'test10_2023-08-17T202205720744315',NULL,NULL,'2023-08-17 20:22:05','saf님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 test10_2023-08-17T202205720744315 입니다.',1153),(1411,'1241241',NULL,NULL,'2023-08-17 20:55:26','성호노노님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1052),(1412,'shtjdgh00_2023-08-17T205538331879653',NULL,NULL,'2023-08-17 20:55:38','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T205538331879653 입니다.',656),(1413,'shtjdgh00_2023-08-17T205539598588723',NULL,NULL,'2023-08-17 20:55:39','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T205539598588723 입니다.',656),(1414,'23131',NULL,NULL,'2023-08-17 20:57:09','성호노노님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1052),(1415,'shtjdgh00_2023-08-17T205718109641825',NULL,NULL,'2023-08-17 20:57:18','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T205718109641825 입니다.',656),(1416,'shtjdgh00_2023-08-17T205855578234039',NULL,NULL,'2023-08-17 20:58:55','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T205855578234039 입니다.',656),(1417,'shtjdgh00_2023-08-17T211742259762481',NULL,NULL,'2023-08-17 21:17:42','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T211742259762481 입니다.',656),(1418,'23',NULL,NULL,'2023-08-17 21:18:16','성호노노님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1052),(1419,'shtjdgh00_2023-08-17T211842500217075',NULL,NULL,'2023-08-17 21:18:42','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T211842500217075 입니다.',656),(1420,'shtjdgh00_2023-08-17T211847516699174',NULL,NULL,'2023-08-17 21:18:47','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T211847516699174 입니다.',656),(1421,'shtjdgh00_2023-08-17T211928537116709',NULL,NULL,'2023-08-17 21:19:28','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T211928537116709 입니다.',656),(1422,'shtjdgh00_2023-08-17T212030560848603',NULL,NULL,'2023-08-17 21:20:30','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T212030560848603 입니다.',656),(1423,'231',NULL,NULL,'2023-08-17 21:21:10','성호노노님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1052),(1424,'shtjdgh00_2023-08-17T212121059347766',NULL,NULL,'2023-08-17 21:21:21','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T212121059347766 입니다.',656),(1425,'shtjdgh00_2023-08-17T212122238071606',NULL,NULL,'2023-08-17 21:21:22','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T212122238071606 입니다.',656),(1426,'shtjdgh00_2023-08-17T212219551552238',NULL,NULL,'2023-08-17 21:22:19','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T212219551552238 입니다.',656),(1427,'314',NULL,NULL,'2023-08-17 21:34:47','성호노노님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1052),(1428,'shtjdgh00_2023-08-17T213455667875082',NULL,NULL,'2023-08-17 21:34:55','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T213455667875082 입니다.',656),(1429,'shtjdgh00_2023-08-17T213456950818130',NULL,NULL,'2023-08-17 21:34:56','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T213456950818130 입니다.',656),(1430,'123412',NULL,NULL,'2023-08-17 21:41:20','성호노노님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',1052),(1431,'shtjdgh00_2023-08-17T214140974207693',NULL,NULL,'2023-08-17 21:41:40','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T214140974207693 입니다.',656),(1432,'shtjdgh00_2023-08-17T214142125214176',NULL,NULL,'2023-08-17 21:41:42','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 shtjdgh00_2023-08-17T214142125214176 입니다.',656),(1433,'123',NULL,NULL,'2023-08-17 21:58:18','testor님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',666),(1434,'test123_2023-08-17T215826080963206',NULL,NULL,'2023-08-17 21:58:26','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 test123_2023-08-17T215826080963206 입니다.',656),(1435,'test123_2023-08-17T215827429132270',NULL,NULL,'2023-08-17 21:58:27','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 test123_2023-08-17T215827429132270 입니다.',656),(1436,'2번',NULL,NULL,'2023-08-17 22:16:23','학생123님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',952),(1437,'dign_2023-08-17T221627351593175',NULL,NULL,'2023-08-17 22:16:27','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T221627351593175 입니다.',656),(1438,'dign_2023-08-17T221634933256314',NULL,NULL,'2023-08-17 22:16:34','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T221634933256314 입니다.',656),(1439,'dign_2023-08-17T221737851928780',NULL,NULL,'2023-08-17 22:17:37','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T221737851928780 입니다.',656),(1440,'dign_2023-08-17T221738907905571',NULL,NULL,'2023-08-17 22:17:38','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T221738907905571 입니다.',656),(1441,'3번',NULL,NULL,'2023-08-17 22:18:00','학생123님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',952),(1442,'dign_2023-08-17T221806042928513',NULL,NULL,'2023-08-17 22:18:06','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T221806042928513 입니다.',656),(1443,'dign_2023-08-17T221806993448863',NULL,NULL,'2023-08-17 22:18:06','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T221806993448863 입니다.',656),(1444,'5번',NULL,NULL,'2023-08-17 22:20:15','학생123님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',952),(1445,'dign_2023-08-17T222019262687123',NULL,NULL,'2023-08-17 22:20:19','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T222019262687123 입니다.',656),(1446,'dign_2023-08-17T222021100681964',NULL,NULL,'2023-08-17 22:20:21','녹차라떼원샷님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T222021100681964 입니다.',656),(1447,'2번',NULL,NULL,'2023-08-17 22:23:12','학생123님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',952),(1448,'dign_2023-08-17T222345598670687',NULL,NULL,'2023-08-17 22:23:45','강사123님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T222345598670687 입니다.',953),(1449,'dign_2023-08-17T222347218167084',NULL,NULL,'2023-08-17 22:23:47','강사123님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T222347218167084 입니다.',953),(1450,'5번',NULL,NULL,'2023-08-17 23:22:12','학생123님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',952),(1451,'dign_2023-08-17T233053543952612',NULL,NULL,'2023-08-17 23:30:53','강사123님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T233053543952612 입니다.',953),(1452,'dign_2023-08-17T233054853307058',NULL,NULL,'2023-08-17 23:30:54','강사123님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T233054853307058 입니다.',953),(1453,'4번',NULL,NULL,'2023-08-17 23:32:53','학생123님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',952),(1454,'dign_2023-08-17T233717908342940',NULL,NULL,'2023-08-17 23:37:17','강사123님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T233717908342940 입니다.',953),(1455,'dign_2023-08-17T233719343916605',NULL,NULL,'2023-08-17 23:37:19','강사123님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T233719343916605 입니다.',953),(1456,'알림 문제 TEST',NULL,NULL,'2023-08-17 23:45:59','학생123님, 등록하신 문제에 새로운 풀이 요청이 도착했습니다.',952),(1457,'dign_2023-08-17T234621851605859',NULL,NULL,'2023-08-17 23:46:21','강사123님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T234621851605859 입니다.',953),(1458,'dign_2023-08-17T234622877558142',NULL,NULL,'2023-08-17 23:46:22','강사123님, 지원하신 요청이 승낙되어 매칭이 성사되었습니다. 강의 세션 ID는 dign_2023-08-17T234622877558142 입니다.',953);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_seq`
--

DROP TABLE IF EXISTS `notification_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_seq`
--

LOCK TABLES `notification_seq` WRITE;
/*!40000 ALTER TABLE `notification_seq` DISABLE KEYS */;
INSERT INTO `notification_seq` VALUES (1551);
/*!40000 ALTER TABLE `notification_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` bigint NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `last_update_time` datetime(6) DEFAULT NULL,
  `matching_state` int NOT NULL DEFAULT '0',
  `time` datetime(6) DEFAULT NULL,
  `title` varchar(40) NOT NULL,
  `master_code_id` int DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsgfle8w1t7879j7o9j1pbpkpi` (`master_code_id`),
  KEY `FKpy606nju8eiyk6ma8dg4k1lxh` (`student_id`),
  CONSTRAINT `FKpy606nju8eiyk6ma8dg4k1lxh` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `FKsgfle8w1t7879j7o9j1pbpkpi` FOREIGN KEY (`master_code_id`) REFERENCES `master_code` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (860,NULL,'모르겠워요','2023-08-17 23:51:44.820753',0,'2023-08-17 23:51:44.820768','공간도형',1143,1104),(861,NULL,'화질이 안 좋아서 죄송합니다. 강의 시간에 제대로 올릴게요!! 풀이를 어떻게 시작해야 할 지 잘 모르겠습니다 ㅠㅠ','2023-08-18 00:09:50.300366',0,'2023-08-18 00:09:50.300379','이 공간도형 문제 잘 모르겠어용 ㅠㅠ',1143,1105),(862,NULL,'이 문제 어떻게 접근해서 어떤 공식으로 풀어야 할까요','2023-08-18 00:11:35.260152',0,'2023-08-18 00:11:35.260165','삼각함수 문제 막힘',1112,1105),(863,NULL,'ㄱㄴㄷ 고르는 문제인데, 이런 문제가 특히 어렵더라구요. 로그 지수 관련해서 이런 문제 쉽게 푸는 방법이 있을까요?','2023-08-18 00:13:54.168876',0,'2023-08-18 00:13:54.168890','로그 문제 가르쳐주세요!!',1111,1105),(864,NULL,'나누는 왜 x-y 가 되나요?? 부끄럽지만 이해가 잘 안 되어요','2023-08-18 00:15:56.069447',0,'2023-08-18 00:15:56.069461','지수 로그 공식 질문',1111,1105),(865,NULL,'여기 각도 계산이 잘 안 되네요. 어떻게 접근해야 하나요?.?','2023-08-18 00:18:22.764764',0,'2023-08-18 00:18:22.764782','삼각함수 기초 문제 질문',1112,1105),(866,NULL,'배수가 될 확률을 구하라고 하는데 잘 모르겠습니다!! 같은 수도 배수로 쳐야 하나요?','2023-08-18 00:30:03.102809',0,'2023-08-18 00:30:03.102823','주사위 확률문제 질문이에요 ㅎㅎ',1131,1106),(867,NULL,'어려운 문제 같지는 않은데 잘 와닿지가 않네요. 두 확률을 곱해야 하나요??','2023-08-18 00:31:45.388604',0,'2023-08-18 00:31:45.388618','검은 공 빨간 공 선택하는 확률 문제',1131,1106),(868,NULL,'연속확률분포 문제인데 좀 많이 어려워요!! d 값을 어떻게 구할 수 있을까용?','2023-08-18 00:35:19.518424',0,'2023-08-18 00:35:19.518439','연속 확률 분포 문제 질문!!ㅎㅎ',1131,1106),(869,NULL,'어디에서 오류가 있는건지 설명해주실 강사분 있나요?? 저 이해를 못했어요 ㅠㅠ','2023-08-18 00:36:22.461802',0,'2023-08-18 00:36:22.461817','이거 오류가 있는 문제라는데',1131,1106),(870,NULL,'무기명과 기명의 차이가 뭐죠?? 잘 모르겠습니다!!','2023-08-18 00:42:24.196604',0,'2023-08-18 00:42:24.196619','통계 문제 질문!!',1132,1106),(871,NULL,'삼차함수인데 저기서 미분가능하지 않다면 저 점을 기준으로 x축 위아래로 갈리는거 맞져?','2023-08-18 00:46:21.456856',0,'2023-08-18 00:46:21.456873','함수의 절댓값이 미분가능하지 않다는 것의 의미가?',1122,1107),(872,NULL,'저 점에서 미분가능하게 하려면 살펴봐야 하는 조건들 뭐가 있었는지 잘 몰라서 그러는데 알려주실 분 있나요','2023-08-18 00:47:54.791747',0,'2023-08-18 00:47:54.791761','특정 지점 기준으로 함수 정의가 바뀌는 경우 질문',1122,1107),(873,NULL,'이거 -1 ~ 1일 때에만 저렇게 되는데 조건들을 봐도 어떻게 풀어야 할지 감이 안 잡힙니다. 도와주세요. 급합니다!!','2023-08-18 00:49:15.510764',0,'2023-08-18 00:49:15.510780','f(3) 알려주세요',1123,1107),(874,NULL,'부정적분 이거 어떻게 풀어요 ㅠㅠ e 부정적분 못 풀겠어요','2023-08-18 00:51:17.937400',0,'2023-08-18 00:51:17.937415','부정적분이 은근 어려워요. 개념좀 알려주세요',1123,1107),(878,NULL,'답을 구하기 어려워요 ㅠㅠ','2023-08-18 01:42:28.982654',2,'2023-08-18 01:42:28.982668','일더하기 일 정답을 알려주세요',1112,1107),(879,NULL,'알려줘 제바 류ㅠ','2023-08-18 01:58:54.321824',2,'2023-08-18 01:58:54.321852','1더하기 1 알려달라고!',1112,1107),(880,NULL,'실수의 개념을 잘 모르겠습니다 도대체 어떻게 답을 구하는 걸까요 도와주세요!','2023-08-18 02:12:04.492790',2,'2023-08-18 02:12:04.492803','문제를 잘 모르겠습니다 도와주세요!',1122,1105),(881,NULL,'실수가 뭘까요 f위에 있는 저 점은 무엇일까요? 모두 너무 어렵습니다...','2023-08-18 02:17:23.608010',2,'2023-08-18 02:17:23.608024','선생님들 제발 도와주세요',1122,1105),(882,NULL,'1번\n','2023-08-18 02:57:09.912673',2,'2023-08-18 02:57:09.912686','1번',1111,952),(883,NULL,'2번','2023-08-18 02:57:31.234502',2,'2023-08-18 02:57:31.234516','2번',1111,952),(884,NULL,'2번','2023-08-18 02:57:32.218473',2,'2023-08-18 02:57:32.218485','2번',1111,952),(885,NULL,'3번\n','2023-08-18 02:57:55.124910',2,'2023-08-18 02:57:55.124923','3번',1111,952),(886,NULL,'3번\n','2023-08-18 02:57:57.919811',0,'2023-08-18 02:57:57.919823','3번',1111,952),(887,NULL,'4번\n','2023-08-18 02:58:22.875127',0,'2023-08-18 02:58:22.875139','4번',1111,952),(888,NULL,'4번\n','2023-08-18 02:58:23.025456',2,'2023-08-18 02:58:23.025474','4번',1111,952),(889,NULL,'4번\n','2023-08-18 02:58:24.245700',0,'2023-08-18 02:58:24.245713','4번',1111,952),(890,NULL,'4번\n','2023-08-18 02:58:25.108226',0,'2023-08-18 02:58:25.108241','4번',1111,952),(891,NULL,'5번\n','2023-08-18 02:58:46.789087',0,'2023-08-18 02:58:46.789102','5번',1111,952),(892,NULL,'5번\n','2023-08-18 02:58:47.670006',0,'2023-08-18 02:58:47.670018','5번',1111,952),(893,NULL,'5번\n','2023-08-18 02:58:47.996880',2,'2023-08-18 02:58:47.996892','5번',1111,952),(894,NULL,'5번\n','2023-08-18 02:58:48.340839',2,'2023-08-18 02:58:48.340851','5번',1111,952),(895,NULL,'5번\n','2023-08-18 02:58:48.469793',0,'2023-08-18 02:58:48.469807','5번',1111,952),(902,NULL,'최종테스트','2023-08-18 04:06:38.357812',2,'2023-08-18 04:06:38.357853','최종 테스트',1111,952),(952,NULL,'사차함수의 적분이라니 저에게는 너무 큰 벽입니다 도와주세요!','2023-08-18 05:13:16.680772',2,'2023-08-18 05:13:16.680794','사차함수라니 도대체 뭘까요',1123,1105),(953,NULL,'이거 알려주실 고수분들 구합니다','2023-08-18 05:19:53.295101',2,'2023-08-18 05:19:53.295115','제발 알려주세요',1122,1152),(954,NULL,'124114','2023-08-18 05:54:58.304182',2,'2023-08-18 05:54:58.304194','1241241',1111,1052),(955,NULL,'12313','2023-08-18 05:56:58.710525',2,'2023-08-18 05:56:58.710536','23131',1111,1052),(956,NULL,'123','2023-08-18 06:18:03.760996',2,'2023-08-18 06:18:03.761012','23',1111,1052),(957,NULL,'12314','2023-08-18 06:20:51.760596',2,'2023-08-18 06:20:51.760611','231',1111,1052),(958,NULL,'42','2023-08-18 06:34:37.176557',2,'2023-08-18 06:34:37.176572','314',1111,1052),(959,NULL,'ㅁㄻㅈㄷㄹ','2023-08-18 06:41:09.564643',2,'2023-08-18 06:41:09.564657','123412',1111,1052),(960,NULL,'123123','2023-08-18 06:57:50.949306',2,'2023-08-18 06:57:50.949319','123',1111,666),(961,NULL,'알림 문제 테스트','2023-08-18 08:45:20.430313',2,'2023-08-18 08:45:20.430326','알림 문제 TEST',1111,952);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_picture`
--

DROP TABLE IF EXISTS `question_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_picture` (
  `id` bigint NOT NULL,
  `content_type` varchar(100) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `time` datetime(6) DEFAULT NULL,
  `question_id` bigint DEFAULT NULL,
  `original_name` varchar(100) NOT NULL,
  `path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKncgfd26os5s3v0bfw30qkacx1` (`question_id`),
  CONSTRAINT `FKncgfd26os5s3v0bfw30qkacx1` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_picture`
--

LOCK TABLES `question_picture` WRITE;
/*!40000 ALTER TABLE `question_picture` DISABLE KEYS */;
INSERT INTO `question_picture` VALUES (860,'image/png','question/16c586be-52ee-43e4-b3a6-8adafd232b4e.png','2023-08-17 23:51:44.822657',860,'gdgd.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/16c586be-52ee-43e4-b3a6-8adafd232b4e.png'),(861,'image/png','question/e1572ccc-5954-4fac-b60b-4597c2b1d67c.png','2023-08-18 00:09:50.302012',861,'gdgd.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/e1572ccc-5954-4fac-b60b-4597c2b1d67c.png'),(862,'image/png','question/77ed8e08-7ebd-4e0e-a6df-00b1ff13f057.png','2023-08-18 00:11:35.261904',862,'aaa.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/77ed8e08-7ebd-4e0e-a6df-00b1ff13f057.png'),(863,'image/jpeg','question/1c784011-8e06-42ce-a420-1de6ac02b1d9.jpg','2023-08-18 00:13:54.170600',863,'다운로드.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/1c784011-8e06-42ce-a420-1de6ac02b1d9.jpg'),(864,'image/png','question/ba949db0-d2cf-477f-8fa7-6dd66e8db3e2.png','2023-08-18 00:15:56.071179',864,'로그지수.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/ba949db0-d2cf-477f-8fa7-6dd66e8db3e2.png'),(865,'image/jpeg','question/68b115b2-9ca3-449d-b130-040a96253d2e.jpg','2023-08-18 00:18:22.766546',865,'삼각함수.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/68b115b2-9ca3-449d-b130-040a96253d2e.jpg'),(866,'image/png','question/f3b05512-d324-4b4f-a712-5e499bdff85a.png','2023-08-18 00:30:03.104596',866,'다운로드a.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/f3b05512-d324-4b4f-a712-5e499bdff85a.png'),(867,'image/png','question/d4d61167-482f-4f76-9979-4b2afdd5410d.png','2023-08-18 00:31:45.390226',867,'gfgf.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/d4d61167-482f-4f76-9979-4b2afdd5410d.png'),(868,'image/png','question/a5f28720-c643-475b-b21c-c75aeb45b8a0.png','2023-08-18 00:35:19.520163',868,'다운로드.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/a5f28720-c643-475b-b21c-c75aeb45b8a0.png'),(869,'image/jpeg','question/9fe484f3-aeac-4e44-93fa-326db7bde1ef.jpg','2023-08-18 00:36:22.463620',869,'dhfb.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/9fe484f3-aeac-4e44-93fa-326db7bde1ef.jpg'),(870,'image/png','question/208de58e-e9f0-46bb-8ec3-b25af4c2d866.png','2023-08-18 00:42:24.198282',870,'gnqh.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/208de58e-e9f0-46bb-8ec3-b25af4c2d866.png'),(871,'image/jpeg','question/1835a62d-b429-40d4-b59c-5c1076b9d09c.jpg','2023-08-18 00:46:21.458608',871,'aq.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/1835a62d-b429-40d4-b59c-5c1076b9d09c.jpg'),(872,'image/png','question/3af6ae6d-c327-4fda-b942-dfe25cc6e3a4.png','2023-08-18 00:47:54.793410',872,'aqaq.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/3af6ae6d-c327-4fda-b942-dfe25cc6e3a4.png'),(873,'image/jpeg','question/55728a6c-1771-4cfc-b13a-3709bcf3d52b.jpg','2023-08-18 00:49:15.512397',873,'wqwq.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/55728a6c-1771-4cfc-b13a-3709bcf3d52b.jpg'),(874,'image/jpeg','question/389861cb-8c82-44ff-b4c1-ed219ba0fce7.jpg','2023-08-18 00:51:17.939124',874,'ewq.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/389861cb-8c82-44ff-b4c1-ed219ba0fce7.jpg'),(878,'image/png','question/a5b9aa24-7e42-4fa1-84e0-606cf369dd69.png','2023-08-18 01:42:28.984153',878,'일더하기일.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/a5b9aa24-7e42-4fa1-84e0-606cf369dd69.png'),(879,'image/png','question/7a86e6bd-fe11-4bd5-a7e2-eb50061df929.png','2023-08-18 01:58:54.323485',879,'일더하기일.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/7a86e6bd-fe11-4bd5-a7e2-eb50061df929.png'),(880,'image/png','question/95c186eb-1ee0-4555-9824-adf4174612c5.png','2023-08-18 02:12:04.494455',880,'aaa.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/95c186eb-1ee0-4555-9824-adf4174612c5.png'),(881,'image/png','question/0ed8efdc-07d6-4330-a25f-985bec998768.png','2023-08-18 02:17:23.609787',881,'aaa.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/0ed8efdc-07d6-4330-a25f-985bec998768.png'),(882,'image/png','question/8af89149-555b-4700-8a88-364e89c5bbd0.png','2023-08-18 02:57:09.914460',882,'student.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/8af89149-555b-4700-8a88-364e89c5bbd0.png'),(883,'image/png','question/4f0f31c1-4d23-4731-87f4-6e62349b63e1.png','2023-08-18 02:57:31.236036',883,'student.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/4f0f31c1-4d23-4731-87f4-6e62349b63e1.png'),(884,'image/png','question/d590a771-98ef-4708-9a5e-3c9ced945ca9.png','2023-08-18 02:57:32.220077',884,'student.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/d590a771-98ef-4708-9a5e-3c9ced945ca9.png'),(885,'image/png','question/a0e538a7-be95-4d22-b173-24a407b757de.png','2023-08-18 02:57:55.126616',885,'student.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/a0e538a7-be95-4d22-b173-24a407b757de.png'),(886,'image/png','question/931ada83-e69d-4669-ae78-22e9bcf2322a.png','2023-08-18 02:57:57.921235',886,'student.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/931ada83-e69d-4669-ae78-22e9bcf2322a.png'),(887,'image/png','question/4bb81498-508e-4550-a9db-0ed5ba5051f4.png','2023-08-18 02:58:22.876626',887,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/4bb81498-508e-4550-a9db-0ed5ba5051f4.png'),(888,'image/png','question/6e6ed0da-c7de-4af9-87dd-79c0d9379b00.png','2023-08-18 02:58:23.027079',888,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/6e6ed0da-c7de-4af9-87dd-79c0d9379b00.png'),(889,'image/png','question/d4ac0f6d-51a6-4a0b-a483-5a9541a2bae7.png','2023-08-18 02:58:24.247196',889,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/d4ac0f6d-51a6-4a0b-a483-5a9541a2bae7.png'),(890,'image/png','question/3db98007-6c35-478a-acfc-be95b98e3713.png','2023-08-18 02:58:25.109782',890,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/3db98007-6c35-478a-acfc-be95b98e3713.png'),(891,'image/png','question/fc9ba483-cb33-4d3a-baf8-fbbe3b2df1fc.png','2023-08-18 02:58:46.790697',891,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/fc9ba483-cb33-4d3a-baf8-fbbe3b2df1fc.png'),(892,'image/png','question/eadeb875-5dbc-4936-bedd-ca94bf5c9a44.png','2023-08-18 02:58:47.671464',892,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/eadeb875-5dbc-4936-bedd-ca94bf5c9a44.png'),(893,'image/png','question/85f1f623-82b1-4d0b-89c2-2aca59825e95.png','2023-08-18 02:58:47.998509',893,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/85f1f623-82b1-4d0b-89c2-2aca59825e95.png'),(894,'image/png','question/c17dfdf4-7016-4c50-993c-e2f96c6fc0b0.png','2023-08-18 02:58:48.342365',894,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/c17dfdf4-7016-4c50-993c-e2f96c6fc0b0.png'),(895,'image/png','question/75988ab7-2e2d-4e9b-918f-7b179a26efbb.png','2023-08-18 02:58:48.471265',895,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/75988ab7-2e2d-4e9b-918f-7b179a26efbb.png'),(902,'image/png','question/790179f0-8771-4c20-b6d2-e0c4f439c8c5.png','2023-08-18 04:06:38.367480',902,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/790179f0-8771-4c20-b6d2-e0c4f439c8c5.png'),(952,'image/jpeg','question/d41ba01c-6428-4d2f-858a-55006da1ef0c.jpg','2023-08-18 05:13:16.682895',952,'wqwq.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/d41ba01c-6428-4d2f-858a-55006da1ef0c.jpg'),(953,'image/jpeg','question/2270f1c5-cd22-4a4b-9cd4-ae463f980e2e.jpg','2023-08-18 05:19:53.297010',953,'wqwq.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/2270f1c5-cd22-4a4b-9cd4-ae463f980e2e.jpg'),(954,'image/png','question/8181b2de-b2b2-4c0c-9d8b-063ce194de62.png','2023-08-18 05:54:58.305999',954,'Solive.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/8181b2de-b2b2-4c0c-9d8b-063ce194de62.png'),(955,'image/png','question/fb6272e1-773a-4691-8dd1-1dea18370dac.png','2023-08-18 05:56:58.712535',955,'배너 5.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/fb6272e1-773a-4691-8dd1-1dea18370dac.png'),(956,'image/png','question/23cea173-5854-4cfa-9ae8-c37b02d834b3.png','2023-08-18 06:18:03.762817',956,'Solive.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/23cea173-5854-4cfa-9ae8-c37b02d834b3.png'),(957,'image/png','question/1ce57619-1fcb-4397-83c2-35b888d02919.png','2023-08-18 06:20:51.762421',957,'Solivew.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/1ce57619-1fcb-4397-83c2-35b888d02919.png'),(958,'image/png','question/c8ad44f0-57de-4a4b-9f47-d095f986bc27.png','2023-08-18 06:34:37.178313',958,'배너 4.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/c8ad44f0-57de-4a4b-9f47-d095f986bc27.png'),(959,'image/png','question/958996ac-eabf-433c-94fe-6f27623a1da0.png','2023-08-18 06:41:09.566433',959,'Solive.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/958996ac-eabf-433c-94fe-6f27623a1da0.png'),(960,'image/png','question/c445a275-5169-478a-be64-8e4807db7582.png','2023-08-18 06:57:50.951183',960,'Solivew.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/c445a275-5169-478a-be64-8e4807db7582.png'),(961,'image/png','question/d4adb50b-b341-4af5-bdd0-623655e742e5.png','2023-08-18 08:45:20.432141',961,'student.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/question/d4adb50b-b341-4af5-bdd0-623655e742e5.png');
/*!40000 ALTER TABLE `question_picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_picture_seq`
--

DROP TABLE IF EXISTS `question_picture_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_picture_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_picture_seq`
--

LOCK TABLES `question_picture_seq` WRITE;
/*!40000 ALTER TABLE `question_picture_seq` DISABLE KEYS */;
INSERT INTO `question_picture_seq` VALUES (1051);
/*!40000 ALTER TABLE `question_picture_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_seq`
--

DROP TABLE IF EXISTS `question_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_seq`
--

LOCK TABLES `question_seq` WRITE;
/*!40000 ALTER TABLE `question_seq` DISABLE KEYS */;
INSERT INTO `question_seq` VALUES (1051);
/*!40000 ALTER TABLE `question_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `question_count` int NOT NULL DEFAULT '0',
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKqytew32213tbnj8u0er377k3q` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (5,503),(0,505),(13,602),(0,604),(1,653),(0,654),(0,655),(1,666),(0,702),(1,752),(2,802),(0,853),(1,854),(27,952),(5,1002),(15,1052),(2,1104),(8,1105),(5,1106),(8,1107),(1,1152);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `rating_count` int NOT NULL DEFAULT '0',
  `rating_sum` int NOT NULL DEFAULT '0',
  `solved_count` int NOT NULL DEFAULT '0',
  `id` bigint NOT NULL,
  `subject_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcr04trtdvvojpd10srj9a79c3` (`subject_id`),
  CONSTRAINT `FKcr04trtdvvojpd10srj9a79c3` FOREIGN KEY (`subject_id`) REFERENCES `master_code` (`id`),
  CONSTRAINT `FKlicv62vmu1ydw117bbxqhkof1` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (9,35,1,502,1000),(0,0,0,552,1000),(0,0,0,603,1000),(9,33,8,656,1132),(1,5,2,657,1121),(2,4,3,658,1132),(0,0,0,659,1122),(0,0,0,660,1143),(0,0,0,661,1000),(0,0,0,662,1141),(0,0,0,663,1000),(0,0,0,664,1000),(0,0,0,665,1000),(0,0,0,852,1000),(0,0,0,902,1000),(0,0,0,903,1000),(0,0,3,953,1143),(0,0,0,1102,1000),(2,10,0,1103,1000),(1,5,1,1153,1141);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `dtype` varchar(31) NOT NULL,
  `id` bigint NOT NULL,
  `content_type` varchar(100) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `email` varchar(40) NOT NULL,
  `experience` bigint DEFAULT '0',
  `file_name` varchar(255) DEFAULT NULL,
  `gender` int NOT NULL,
  `introduce` varchar(255) DEFAULT NULL,
  `login_id` varchar(40) NOT NULL,
  `login_password` char(60) NOT NULL,
  `nickname` varchar(10) NOT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `signin_time` datetime(6) DEFAULT NULL,
  `master_code_id` int DEFAULT NULL,
  `state_id` int DEFAULT NULL,
  `original_name` varchar(100) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `solve_point` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FKp02m4f3m1foucvxa5lgjnnkcp` (`master_code_id`),
  KEY `FKteow0n1n1umoigwccsxcevjll` (`state_id`),
  CONSTRAINT `FKp02m4f3m1foucvxa5lgjnnkcp` FOREIGN KEY (`master_code_id`) REFERENCES `master_code` (`id`),
  CONSTRAINT `FKteow0n1n1umoigwccsxcevjll` FOREIGN KEY (`state_id`) REFERENCES `master_code` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('Admin',0,NULL,NULL,'soliveadmin@ssafy.com',0,NULL,3,NULL,'admin','$2a$10$F1NnfGrbZJkmVRnFddT83egW0GTcsEGV9yVwtW22yIuXidA3Uhxyi','관리자','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMjU1NDIyLCJleHAiOjE2OTM0NjUwMjIsInVzZXJJZCI6MH0.2J372nNC2dshyzz7xKRzwv0KHV0rp0qTNgWMrVmpufQ','2023-08-17 15:48:03.993621',3,12,NULL,NULL,0),('Teacher',502,NULL,NULL,'hs@hs.ac.kr',100,NULL,1,NULL,'hyosik','$2a$10$fbXQEppcBy20mKLlTGg4Ne/M6WG/biu3soVXWVKEz423Cj6remdM6','hyosik','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMjc3OTkwLCJleHAiOjE2OTM0ODc1OTAsInVzZXJJZCI6NTAyfQ.Ie4IQL0xmUnMcxo2d9-73BY9Q1TPpvzt_KVVHqsCzlo','2023-08-13 08:33:03.000000',2,12,NULL,NULL,1000010),('Student',503,'image/jpeg',NULL,'a@a',0,'profile/e70762b5-73d6-4ed6-b6be-fe02109bfac7.jpg',1,'asfasasf','sis03090','$2a$10$XDMwzTY.zscpMgOFyRVR9.l6FUdiPgdnFeUsvVAb/uJLtgm412A4W','박민혁','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMjgzMjY4LCJleHAiOjE2OTM0OTI4NjgsInVzZXJJZCI6NTAzfQ.k_SY5-AaCcwrjEfRJN9N8Qnqs_p9BIfJY7MrMi4VahA','2023-08-14 00:02:24.000000',1,12,'굿2.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/e70762b5-73d6-4ed6-b6be-fe02109bfac7.jpg',50000),('Student',505,NULL,NULL,'23@23',0,NULL,1,NULL,'dydgus','$2a$10$YTTVqmAjSNp77.jNNhE8ludW8VJmMowq6D0KOFN7DbvY8E2zLCJai','용현학생','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkxOTc4NTI4LCJleHAiOjE2OTMxODgxMjgsInVzZXJJZCI6NTA1fQ.RV7B986XFrSJJ3mm7LDmWgfIsj6YVSXcNflsIA25XNQ','2023-08-14 01:39:56.000000',1,12,NULL,NULL,0),('Teacher',552,NULL,NULL,'32@32',0,NULL,1,NULL,'dldydgus','$2a$10$pmzVvQsP6PKuiwaC2UVi6uJFXMkmmykdhUKNObIISIVnoWEGJeQhi','예비군간',NULL,'2023-08-14 03:55:20.000000',2,11,NULL,NULL,0),('Student',602,NULL,NULL,'hs@hs.ac',100,NULL,1,NULL,'lhslhs','$2a$10$Y6R3Fi6Gy7FyThBH8WLktubsISiHaqvyBrERwDyeEHzS3c0EClOHe','hs','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMzAyMzczLCJleHAiOjE2OTM1MTE5NzMsInVzZXJJZCI6NjAyfQ.u6Zzfe-6r0WE-HVP26f8RSQEs8Nj1jL1ATS0MBSWfh0','2023-08-14 05:26:40.000000',1,12,NULL,NULL,999990),('Teacher',603,NULL,NULL,'ssa@ggaaa',0,NULL,1,NULL,'마이쮸','$2a$10$DFrqWw1YI2YhmWV1.kwMNuEawrx382Xwh6cNQ6kR1lqH3z91/GryO','노성호gj','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkxOTkxMjM0LCJleHAiOjE2OTMyMDA4MzQsInVzZXJJZCI6NjAzfQ.Ut7KJ-W0RtX09eBvt2K9RQdkvtlgViG8uS2xYpK_AZ0','2023-08-14 05:33:15.000000',2,12,NULL,NULL,0),('Student',604,NULL,NULL,'ssa@ggaaaaa',0,NULL,1,NULL,'노성호','$2a$10$imselR/YPoyO4bXYGalTzuNnjePVCA/0kTIaI9ISwhP5gyxMrZjRi','노성호','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkxOTkxMjIwLCJleHAiOjE2OTMyMDA4MjAsInVzZXJJZCI6NjA0fQ.2Zzjd0JcCUTG-bnrX4wpPSZbdfqWJPEGOi1IdvzDia8','2023-08-14 05:33:33.000000',1,12,NULL,NULL,0),('Student',653,'image/png',NULL,'harrykane@ssafy.com',0,'profile/d4cb1f64-4c01-4cce-8c71-6b639dce628f.png',1,'바다는 비에 젖지 않는다.','gkrtod1','$2a$10$GeIzCxHsce9zlyUywjIBtOoH3CufPG2gPLBsS90BJX9aRNuw8zwmu','몽수우끼','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMjc2MjMzLCJleHAiOjE2OTM0ODU4MzMsInVzZXJJZCI6NjUzfQ.Ed6WmDsj2V48RP8bFiTLiZcH8IBgSLEQb5LWt9CgQHo','2023-08-14 06:17:50.000000',1,12,'Untitled.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/d4cb1f64-4c01-4cce-8c71-6b639dce628f.png',0),('Student',654,'image/jpeg',NULL,'weynerooney@ssafy.com',0,'profile/42b97656-db5b-438c-904d-534fdae0e783.jpg',2,'질문은 국력이다.','gkrtod2','$2a$10$xRm4R91.6Q3wc3H./sQGuePrV8Wv8yFghPPbWRZD9BKamAesnkw9.','질문왕',NULL,'2023-08-14 06:19:42.000000',1,11,'질문왕.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/42b97656-db5b-438c-904d-534fdae0e783.jpg',0),('Student',655,'image/jpeg',NULL,'masonmount@ssafy.com',0,'profile/53e72038-efc1-4c72-bfce-6a2d7761f733.jpg',2,'안녕하세요! 저는 헤르미온느입니다, 마법과 모험을 사랑하는 마법사입니다. 지식과 용기를 소중히 여기며 친구들과 함께 성장하는 것을 즐깁니다.','gkrtod3','$2a$10$ed26VxqJkpWXF/gdkg2cIO0FsvbvSq8PS5lkEnN3o8M13jGcQYXMW','헤르미온느',NULL,'2023-08-14 06:20:12.000000',1,11,'헤르미온느.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/53e72038-efc1-4c72-bfce-6a2d7761f733.jpg',0),('Teacher',656,'image/jpeg',NULL,'philfoden@ssafy.com',800,'profile/99284018-85c3-4d2e-8b2a-f9bf28fd74fe.jpg',1,'안녕하세요 예스성호입니다.','tjstodsla1','$2a$10$J497tkmlBt6eOM.3syDVoOmhk6Ps.6LlE6VsziqT.IOfBacpZpD3i','녹차라떼원샷','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMzE2NjUxLCJleHAiOjE2OTM1MjYyNTEsInVzZXJJZCI6NjU2fQ.AaPy3970BB7hfTHK2LLC0Q2Lz8AbAYuG8g_lpIHNrhk','2023-08-14 06:21:56.000000',2,12,'녹차라떼.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/99284018-85c3-4d2e-8b2a-f9bf28fd74fe.jpg',1004280),('Teacher',657,'image/jpeg',NULL,'stevengerrard@ssafy.com',200,'profile/da6846e2-b628-421a-851c-039233de1884.jpg',0,'어묵을 극한까지 먹어','tjstodsla2','$2a$10$TlMOI7X8REX/l1N8qvKJi.may1.ehK9XtPRU3SurpVTWI5d/SwzsS','어묵이좋아',NULL,'2023-08-14 06:23:00.000000',2,11,'어묵.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/da6846e2-b628-421a-851c-039233de1884.jpg',2000),('Teacher',658,'image/jpeg',NULL,'declanrice@ssafy.com',300,'profile/a6ae37a7-2ad6-4c09-85de-e6fd6d541572.jpg',0,'통계적으로 갈색 강아지는 귀엽습니다.','tjstodsla3','$2a$10$yBhV2BuTyIyXmEvzHugixesUMcuriYGJS6PVYAkoC/QNDS1UvABnu','갈색강아지',NULL,'2023-08-14 06:24:07.000000',2,11,'갈색강아지.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/a6ae37a7-2ad6-4c09-85de-e6fd6d541572.jpg',2500),('Teacher',659,'image/jpeg',NULL,'benchilwell@ssafy.com',0,'profile/457d9a52-5530-4fc5-a83c-530a2c6c2244.jpg',1,'효식이형을 미분하면?','tjstodsla4','$2a$10$.if8QfxaAy2ldkvJd1des.e8gCQVaWcW9PpcSOEyQCcK3/I/3XIfi','효세라핌',NULL,'2023-08-14 06:25:25.000000',2,11,'다운로드.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/457d9a52-5530-4fc5-a83c-530a2c6c2244.jpg',0),('Teacher',660,'image/jpeg',NULL,'kylewalker@ssafy.com',0,'profile/2a12c053-89de-4bff-a471-3b01089f19c6.jpg',2,'저는 도토리를 싫어해요','tjstodsla5','$2a$10$8yqndU0BJQw1lWRXycy12.8cxjPMGYKtHtCHtC98wPQsnmfiFaTua','편안한다람쥐',NULL,'2023-08-14 06:26:23.000000',2,11,'편안한 다람쥐.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/2a12c053-89de-4bff-a471-3b01089f19c6.jpg',0),('Teacher',661,'image/jpeg',NULL,'johnstones@ssafy.com',0,'profile/61f784f8-a4e4-4ed0-8f69-6a0bbfb669f0.jpg',1,'뿔방향이 (1,2,5)','tjstodsla6','$2a$10$v4YTYKsH3jpVSPpXQDVUFOYRYQMBzIqyziXXhU1/3Sz0en2N2EqTC','라바콘유니콘',NULL,'2023-08-14 06:28:23.000000',2,11,'유니콘.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/61f784f8-a4e4-4ed0-8f69-6a0bbfb669f0.jpg',0),('Teacher',662,'image/jpeg',NULL,'reecejames@ssafy.com',0,'profile/bd09a354-d371-458c-ba7b-883b70ddedbe.jpg',1,'사실잘그림','tjstodsla7','$2a$10$fJ4UrQRNa0PtDX8T9ZCszePrQ61O6P6Q/GaHxxOit/.QiFtqU.IMK','대충그린피카츄',NULL,'2023-08-14 06:29:08.000000',2,11,'피카츄.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/bd09a354-d371-458c-ba7b-883b70ddedbe.jpg',0),('Teacher',663,NULL,NULL,'son@best.com',0,NULL,1,NULL,'tjstodsla8','$2a$10$Ats17rKHy15.SXxnkV4O8OsVmv079zGGM/PmvMb72wubAhRSYOCba','손흥민',NULL,'2023-08-14 06:29:42.000000',2,11,NULL,NULL,0),('Teacher',664,NULL,NULL,'nickpope@ssafy.com',0,NULL,1,NULL,'tjstodsla9','$2a$10$anOSxD90HlADs2wx2BuBluTroivc9OK9BQA/J5BBS5akT4vG3j8hK','닉포프',NULL,'2023-08-14 06:31:06.000000',2,11,NULL,NULL,0),('Teacher',665,NULL,NULL,'test@naver.co',0,NULL,1,NULL,'hi','$2a$10$Zk90iyrPR8nYVadAHBD4xebbujLkZMJwptnoXUwCQStUplqxK78Ay','tester','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkxOTk2ODQyLCJleHAiOjE2OTMyMDY0NDIsInVzZXJJZCI6NjY1fQ.SCcljg91Ux_jvqRahUEPWA93BOf73uhurQMjjsQm1Ok','2023-08-14 07:07:13.000000',2,12,NULL,NULL,0),('Student',666,NULL,NULL,'test@test123',0,NULL,1,NULL,'test123','$2a$10$Xb2d.K7vLPZA.niEHJ0FCeiKLJUYc/jMwkNB6oAzKF0SwikjaNbte','testor','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMzA5NDUyLCJleHAiOjE2OTM1MTkwNTIsInVzZXJJZCI6NjY2fQ.zzMSFHEBoNS-i1xxQQkR8YvpFSti_bppEC3np4smo24','2023-08-14 07:28:40.000000',1,12,NULL,NULL,30000),('Student',702,NULL,NULL,'tjstodsla@ga',0,NULL,1,NULL,'ganda','$2a$10$RzXxWe/2o/Mc7BEPYRrVz.xwMe3qXKPinJaTvZ0p4BTnlvpkTFx4S','간다드래프트',NULL,'2023-08-14 07:46:55.000000',1,11,NULL,NULL,0),('Student',752,NULL,NULL,'ssaa@ggaaaaa',0,NULL,1,NULL,'노호','$2a$10$9iNgBGnpxFqeMLG82vElUOfgV3mFX0IrTPeKeI/LffvUAuOrMnIOa','노호','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMTU5NzgxLCJleHAiOjE2OTMzNjkzODEsInVzZXJJZCI6NzUyfQ.SGL-vtLSMyFevWq3_46ZTDyW0f2_OMWhBvk0Y5BohwY','2023-08-15 01:33:06.000000',1,12,NULL,NULL,0),('Student',802,NULL,NULL,'sda@asdlsa',0,NULL,1,NULL,'toto','$2a$10$0mHmHX8CFyf6owf7j1nqMeJ1.yw36aX8GS8xLGAYWdd8z.wa7mrJi','sds',NULL,'2023-08-16 15:25:30.352280',1,11,NULL,NULL,490000),('Teacher',852,NULL,NULL,'sdaW@safsa',0,NULL,1,NULL,'mimi','$2a$10$5LeTBMzBHPDL5ykBJ.cY1OCX/LXgOgAFoCWUHqOVeIrGujQwfs6Vi','강사','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMTg4NzIyLCJleHAiOjE2OTMzOTgzMjIsInVzZXJJZCI6ODUyfQ.Exy0gY3fKV3d83qUFy8MSCn_xe0KUQ6S0-6Jz8yXAvk','2023-08-16 21:25:16.788498',2,12,NULL,NULL,0),('Student',853,NULL,NULL,'dasd@naver.com',0,NULL,1,NULL,'vivi','$2a$10$SKQpOJSSQkpK5naMRW.9ievUKNQ.rkejT/WGjERL9cZdJXYdNRYE2','qoqo','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMTkyOTMzLCJleHAiOjE2OTM0MDI1MzMsInVzZXJJZCI6ODUzfQ.PKO0QRCM-vziSwrMR2gowH3a3d7AjJOE0rzIlKotJLU','2023-08-16 22:35:26.898912',1,12,NULL,NULL,230000),('Student',854,NULL,NULL,'asd@nsad',0,NULL,1,NULL,'dodo','$2a$10$G4P0HACe939IV.OECcPH4ecbJ/rNJgNtEwJ.gZfKbHt9BMb8PPgeO','학생학생',NULL,'2023-08-17 09:07:16.595303',1,11,NULL,NULL,0),('Teacher',902,'image/jpeg',NULL,'whdtmql1228@gmail.com',0,'profile/be919c72-e1ef-41c2-84dd-e8421d1a01fa.jpg',1,NULL,'whdtmql1228','$2a$10$IslIB85j.IIsNIkewSp.qu.NMfSCAelOo4xf4YxV5EWCYWsOzfN.y','김코치','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMjU2MTM4LCJleHAiOjE2OTM0NjU3MzgsInVzZXJJZCI6OTAyfQ.oyfjQCuSYPoZsPLrQ3URZSZ6ZGYiLmwD2QvsmZo6rBc','2023-08-17 09:37:00.017442',2,12,'jiu.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/be919c72-e1ef-41c2-84dd-e8421d1a01fa.jpg',9000),('Teacher',903,NULL,'2023-08-17 09:41:43.532272','kyuh2002@naver.com',0,NULL,0,NULL,'kyuh2001','$2a$10$MnQt.U5mffWdaYQWNJvGAOGwuEzTJdlrdjv6hzZsSGCq1PNgpAcQe','쿠마','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMjMyODQyLCJleHAiOjE2OTM0NDI0NDIsInVzZXJJZCI6OTAzfQ._rD_jJHOFzRlW0Tz9IQiV-K_pGsiSU4Yqb4DUfrycXI','2023-08-17 09:40:16.439379',2,12,NULL,NULL,1000000),('Student',952,NULL,NULL,'sadsad@sada',600,NULL,1,NULL,'dign','$2a$10$UWjL8CJkPLN1SfP5C7WsJe8bbnxsauekBGs2leEBreAToekcDPGvi','학생123','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMzE2MDQ5LCJleHAiOjE2OTM1MjU2NDksInVzZXJJZCI6OTUyfQ.PJ78Z-QmZTFxDQgqGbjXykeW-jVxrl3lx1AJqkJW2qo','2023-08-17 10:07:15.441208',1,12,NULL,NULL,706790),('Teacher',953,'image/png',NULL,'sada@asda',300,'profile/733b6a46-ffda-4dd2-b1f7-5eed8f4e9c25.png',1,NULL,'digndign','$2a$10$iGzSIlGbBIcU70JApv/6aO0tVhmP0enYs8kjHgo7Zrf1jNmZdvWqu','강사123','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMzE2NDU1LCJleHAiOjE2OTM1MjYwNTUsInVzZXJJZCI6OTUzfQ.WY9fiCaZGkYJTJB9SwlIjH8GqycE9NwIebGg_LyFzQE','2023-08-17 10:14:30.423022',2,12,'teacher.png','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/733b6a46-ffda-4dd2-b1f7-5eed8f4e9c25.png',161200),('Student',1002,NULL,NULL,'hs@hs2.ac.kr',0,NULL,1,NULL,'hs2','$2a$10$lLP9CT939E.5HcrjKD2Y2.s29NvKlQ6tkfsTlnQhlg0h28yNM0t1.','hs2','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMjcwNDY4LCJleHAiOjE2OTM0ODAwNjgsInVzZXJJZCI6MTAwMn0._Bj6QvY-yAoHtR_hJIkudDkrnXt9Ra59j0g4m4rmUhM','2023-08-17 19:51:45.629682',1,12,NULL,NULL,1000000),('Student',1052,NULL,NULL,'shtjdgh00@g.com',300,NULL,0,NULL,'shtjdgh00','$2a$10$CKW5PFBVkEedv/kLJye03.SUr5I5oeW3VsMqqCRIW48tG6gouNZYi','성호노노',NULL,'2023-08-17 20:17:11.976500',1,11,NULL,NULL,129964),('Teacher',1102,NULL,NULL,'sh@g.com',0,NULL,1,NULL,'shtjdgh11','$2a$10$wRbvsz1HsajDfSslZ8oko./sfR2K7S86JW5oNTlbgY65bfiVAaOHu','강사성호',NULL,'2023-08-17 21:45:31.133224',2,11,NULL,NULL,0),('Teacher',1103,NULL,NULL,'test@test1234',0,NULL,0,NULL,'test1234','$2a$10$OepVk1b5YkHNEi9iFtvES.Nd59tJ3ZhVHevZ3HYS3mTvMON3Kehi.','testor2','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwiaWF0IjoxNjkyMjc2NTIzLCJleHAiOjE2OTM0ODYxMjMsInVzZXJJZCI6MTEwM30.RBW8nSgHDp90A9Uzo_pvzjinoYZl6wnBCoA3b9wTEUY','2023-08-17 21:48:33.601001',2,12,NULL,NULL,0),('Student',1104,NULL,NULL,'shsh@g',0,NULL,1,NULL,'shtjdgh22','$2a$10$1WSqrJjdoKrmQHYNlImFduoGJP4x5S4aGasF3wcp3Z/TF0OfaeBq.','김성호',NULL,'2023-08-17 21:57:57.907121',1,11,NULL,NULL,0),('Student',1105,NULL,NULL,'s@g',400,NULL,1,NULL,'wngh1698','$2a$10$oqeZl1ZtUkxajsRTfmsU7uTaaSFt2kpBzeOOdm12fDdxnGygVOp92','끼우수몽',NULL,'2023-08-18 00:07:30.420265',1,11,NULL,NULL,5766),('Student',1106,NULL,NULL,'dms@g',0,NULL,2,NULL,'dmswls12','$2a$10$d5Zkf3KiUhYz64F0Of7mP.LSq9hDgeMIe/1QJ.Yioxn2m4ivyee/G','은혜롭고참된사람',NULL,'2023-08-18 00:24:32.542934',1,11,NULL,NULL,0),('Student',1107,NULL,NULL,'s@gggg',300,NULL,1,NULL,'tjdgh12','$2a$10$jNK6c1wrvSFTqRV2HfYpbOtJVPSBGu.2ctOY/soZDys6LI.i6yjdq','성호최고',NULL,'2023-08-18 00:44:33.854838',1,11,NULL,NULL,47500),('Student',1152,NULL,NULL,'s@gg',100,NULL,0,NULL,'test10','$2a$10$gdqtBYwxGJXWNVG7NitbguxPDwlygd.AD1JWtCb7DTcjHjVy03II2','간다드래프트2',NULL,'2023-08-18 05:19:21.775404',1,11,NULL,NULL,8900),('Teacher',1153,'image/jpeg',NULL,'tjstodsla@gan',100,'profile/23b7ec17-11f5-403d-9fc8-ce954021fafc.jpg',1,NULL,'test11','$2a$10$qELePxiwcW.rAiXJFtCTjewNAUU9TtFTf1N5.S50LccZNB2WTHP7m','saf',NULL,'2023-08-18 05:20:44.204537',2,11,'wqwq.jpg','https://ssafy-solive.s3.ap-northeast-2.amazonaws.com/profile/23b7ec17-11f5-403d-9fc8-ce954021fafc.jpg',1100);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_seq`
--

DROP TABLE IF EXISTS `user_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_seq`
--

LOCK TABLES `user_seq` WRITE;
/*!40000 ALTER TABLE `user_seq` DISABLE KEYS */;
INSERT INTO `user_seq` VALUES (1251);
/*!40000 ALTER TABLE `user_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-18  9:07:43
