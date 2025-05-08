-- MySQL dump 10.13  Distrib 9.2.0, for macos14.7 (arm64)
--
-- Host: localhost    Database: commuter
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `commuting_logs`
--

DROP TABLE IF EXISTS `commuting_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commuting_logs` (
                                  `log_id` int NOT NULL AUTO_INCREMENT,
                                  `user_id` int DEFAULT NULL,
                                  `transportation_mode` varchar(50) DEFAULT NULL,
                                  `date_added` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                  `duration_in_minutes` int DEFAULT NULL,
                                  `commute_type` varchar(50) DEFAULT 'Work',
                                  `distance_in_miles` float DEFAULT NULL,
                                  `cost` int DEFAULT NULL,
                                  PRIMARY KEY (`log_id`),
                                  KEY `user_id` (`user_id`),
                                  CONSTRAINT `commuting_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=488 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commuting_logs`
--

LOCK TABLES `commuting_logs` WRITE;
/*!40000 ALTER TABLE `commuting_logs` DISABLE KEYS */;
INSERT INTO `commuting_logs` VALUES (472,947,NULL,'2025-05-01 20:28:38',1,'Car',1,1),(473,947,NULL,'2025-05-01 20:55:09',13,'car',13,1),(474,947,NULL,'2025-05-01 20:55:23',234,'car',2,2),(475,947,NULL,'2025-05-01 21:06:09',10,'car',2,2),(476,947,NULL,'2025-05-01 22:12:13',9,'bike',1,1),(477,947,NULL,'2025-05-01 22:12:20',1,'car',1,1),(478,947,NULL,'2025-05-01 22:27:06',1,'car',1,1),(479,947,NULL,'2025-05-01 22:43:52',10,'Trike',10,1),(480,947,NULL,'2025-05-01 22:44:52',1,'Micah Subaru ',1,1),(481,947,NULL,'2025-05-07 00:12:19',1,'walk',1,1),(482,947,NULL,'2025-05-07 00:13:43',2,'walk',1,1),(483,947,NULL,'2025-05-07 18:02:02',12,'walk',1,1),(484,947,NULL,'2025-05-07 23:49:28',12,'Car',1,0),(485,947,NULL,'2025-05-07 23:49:59',12,'Car',12,12),(486,947,NULL,'2025-05-07 23:50:14',12,'Car',12,0),(487,947,NULL,'2025-05-07 23:50:59',12,'Car',12,0);
/*!40000 ALTER TABLE `commuting_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cost_analyses`
--

DROP TABLE IF EXISTS `cost_analyses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cost_analyses` (
                                 `analysis_id` int NOT NULL AUTO_INCREMENT,
                                 `user_id` int DEFAULT NULL,
                                 `commute_type` varchar(50) DEFAULT NULL,
                                 `one_year_cost` float DEFAULT NULL,
                                 `two_year_cost` float DEFAULT NULL,
                                 `five_year_cost` float DEFAULT NULL,
                                 `total_cost` float DEFAULT NULL,
                                 PRIMARY KEY (`analysis_id`),
                                 KEY `user_id` (`user_id`),
                                 CONSTRAINT `cost_analyses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=455 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cost_analyses`
--

LOCK TABLES `cost_analyses` WRITE;
/*!40000 ALTER TABLE `cost_analyses` DISABLE KEYS */;
INSERT INTO `cost_analyses` VALUES (451,947,'walk',0.42,0.84,2.1,3.36),(452,947,'bike',0.14,0.28,0.7,1.12),(453,947,'bus',0,0,0,0),(454,947,'Car',254.5,509,1272.5,2036);
/*!40000 ALTER TABLE `cost_analyses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cost_projections`
--

DROP TABLE IF EXISTS `cost_projections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cost_projections` (
                                    `log_id` int NOT NULL AUTO_INCREMENT,
                                    `user_id` int NOT NULL,
                                    `transportation_mode` varchar(50) DEFAULT NULL,
                                    `date_added` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                    `duration_in_minutes` int DEFAULT NULL,
                                    `distance_in_miles` float DEFAULT NULL,
                                    PRIMARY KEY (`log_id`,`user_id`),
                                    KEY `user_id` (`user_id`),
                                    CONSTRAINT `cost_projections_ibfk_1` FOREIGN KEY (`log_id`) REFERENCES `commuting_logs` (`log_id`) ON DELETE CASCADE,
                                    CONSTRAINT `cost_projections_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cost_projections`
--

LOCK TABLES `cost_projections` WRITE;
/*!40000 ALTER TABLE `cost_projections` DISABLE KEYS */;
/*!40000 ALTER TABLE `cost_projections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transportation_costs`
--

DROP TABLE IF EXISTS `transportation_costs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transportation_costs` (
                                        `profile_id` int NOT NULL AUTO_INCREMENT,
                                        `user_id` int DEFAULT NULL,
                                        `insurance_cost` double DEFAULT NULL,
                                        `vehicle_type` varchar(50) DEFAULT NULL,
                                        `fuel_cost` double DEFAULT '3.5',
                                        `maintenance_cost` double DEFAULT NULL,
                                        `public_transport_cost` float DEFAULT NULL,
                                        `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                        `miles_per_gallon` double DEFAULT '25',
                                        `monthly_payment` double DEFAULT '200',
                                        `column_name` int DEFAULT NULL,
                                        PRIMARY KEY (`profile_id`),
                                        KEY `user_id` (`user_id`),
                                        CONSTRAINT `transportation_costs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transportation_costs`
--

LOCK TABLES `transportation_costs` WRITE;
/*!40000 ALTER TABLE `transportation_costs` DISABLE KEYS */;
INSERT INTO `transportation_costs` VALUES (21,947,45,'Car',3.5,10,1,'2025-05-08 00:01:45',28,187,NULL);
/*!40000 ALTER TABLE `transportation_costs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `first_name` varchar(100) DEFAULT NULL,
                         `last_name` varchar(100) DEFAULT NULL,
                         `email` varchar(100) DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=956 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Jane','Doe','jane.doe@example.com','password123'),(2,'John','Doe','john.doe@example.com','password123'),(4,'Alice','Wonderland','alice@example.com','password123'),(5,'Bob','Builder','bob@example.com','password123'),(6,'John Updated','Smith','john.smith@example.com','password123'),(935,NULL,NULL,'micahjdarling@gmail.com',NULL),(947,NULL,NULL,'darlingmicah@gmail.com',NULL),(953,NULL,NULL,'mdarling@madisoncollege.edu',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-07 19:13:31
