-- MySQL dump for Commuter Database

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
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commuting_logs` (
                                  `log_id` int    AUTO_INCREMENT,
                                  `user_id` int   ,
                                  `transportation_mode` varchar(50)   ,
                                  `date_added` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                  `duration_in_minutes` int   ,
                                  `commute_type` varchar(50) DEFAULT 'Work',
                                  `distance_in_miles` float   ,
    `cost` int,
                                  PRIMARY KEY (`log_id`),
                                  KEY `user_id` (`user_id`),
                                  CONSTRAINT `commuting_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commuting_logs`
--

LOCK TABLES `commuting_logs` WRITE;
/*!40000 ALTER TABLE `commuting_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `commuting_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cost_analyses`
--

DROP TABLE IF EXISTS `cost_analyses`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cost_analyses` (
                                 `analysis_id` int    AUTO_INCREMENT,
                                 `user_id` int   ,
                                 `commute_type` enum('Personal','Work','Business')   ,
                                 `one_year_cost` float   ,
                                 `two_year_cost` float   ,
                                 `five_year_cost` float   ,
                                 `total_cost` float   ,
                                 PRIMARY KEY (`analysis_id`),
                                 KEY `user_id` (`user_id`),
                                 CONSTRAINT `cost_analyses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cost_analyses`
--

LOCK TABLES `cost_analyses` WRITE;
/*!40000 ALTER TABLE `cost_analyses` DISABLE KEYS */;
/*!40000 ALTER TABLE `cost_analyses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cost_projections`
--

DROP TABLE IF EXISTS `cost_projections`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cost_projections` (
                                    `log_id` int AUTO_INCREMENT ,
                                    `user_id` int   ,
                                    `transportation_mode` varchar(50)   ,
                                    `date_added` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                    `duration_in_minutes` int   ,
                                    `distance_in_miles` float   ,
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

DROP TABLE IF EXISTS transportation_profiles;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transportation_costs` (
                                        `cost_id` int    AUTO_INCREMENT,
                                        `user_id` int   ,
                                        `insurance_cost` float   ,
                                        `vehicle_type` varchar(50)   ,
                                        `fuel_cost` float   ,
                                        `maintenance_cost` float   ,
                                        `public_transport_cost` float   ,
                                        `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                        PRIMARY KEY (`cost_id`),
                                        KEY `user_id` (`user_id`),
                                        CONSTRAINT `transportation_costs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transportation_costs`
--

LOCK TABLES transportation_profiles WRITE;
/*!40000 ALTER TABLE transportation_profiles DISABLE KEYS */;
/*!40000 ALTER TABLE transportation_profiles ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `id` int AUTO_INCREMENT,
                         `first_name` varchar(100),
                         `last_name` varchar(100),
                         `email` varchar(100),   -- No UNIQUE constraint
                         `password` varchar(255),
                         PRIMARY KEY (`id`),
                         INDEX (`email`)  -- Create an index for performance on the email column
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
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

-- Dump completed on 2025-03-12  9:06:53
