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

-- -----------------------------------------------------
-- Table structure for table `users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `id`                 INT AUTO_INCREMENT PRIMARY KEY,
                         `first_name`         VARCHAR(100),
                         `last_name`          VARCHAR(100),
                         `email`              VARCHAR(100) UNIQUE,
                         `password`           VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table structure for table `commuting_logs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `commuting_logs`;
CREATE TABLE `commuting_logs` (
                                  `log_id`             INT AUTO_INCREMENT PRIMARY KEY,
                                  `user_id`            INT,
                                  `transportation_mode` VARCHAR(50),
                                  `date_added`         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  `duration_in_minutes` INT,
                                  `commute_type`       VARCHAR(50) DEFAULT 'Work',
                                  `distance_in_miles`  FLOAT,
                                  `cost`               INT,
                                  KEY (`user_id`),
                                  CONSTRAINT `commuting_logs_ibfk_1`
                                      FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
                                          ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table structure for table `cost_analyses`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cost_analyses`;
CREATE TABLE `cost_analyses` (
                                 `analysis_id`        INT AUTO_INCREMENT PRIMARY KEY,
                                 `user_id`            INT,
                                 `commute_type`       VARCHAR(50),
                                 `one_year_cost`      FLOAT,
                                 `two_year_cost`      FLOAT,
                                 `five_year_cost`     FLOAT,
                                 `total_cost`         FLOAT,
                                 KEY (`user_id`),
                                 CONSTRAINT `cost_analyses_ibfk_1`
                                     FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
                                         ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table structure for table `transportation_costs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `transportation_costs`;
CREATE TABLE `transportation_costs` (
                                        `profile_id`         INT AUTO_INCREMENT PRIMARY KEY,
                                        `user_id`            INT,
                                        `insurance_cost`     DOUBLE,
                                        `vehicle_type`       VARCHAR(50),
                                        `fuel_cost`          DOUBLE DEFAULT 3.5,
                                        `maintenance_cost`   DOUBLE,
                                        `public_transport_cost` FLOAT,
                                        `created_date`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        `miles_per_gallon`   DOUBLE DEFAULT 25,
                                        `monthly_payment`    DOUBLE DEFAULT 200,
                                        KEY (`user_id`),
                                        CONSTRAINT `transportation_costs_ibfk_1`
                                            FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
                                                ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table structure for table `cost_projections`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cost_projections`;
CREATE TABLE `cost_projections` (
                                    `log_id`             INT,
                                    `user_id`            INT NOT NULL,
                                    `transportation_mode` VARCHAR(50),
                                    `date_added`         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    `duration_in_minutes` INT,
                                    `distance_in_miles`  FLOAT,
                                    PRIMARY KEY (`log_id`,`user_id`),
                                    KEY (`user_id`),
                                    CONSTRAINT `cost_projections_ibfk_1`
                                        FOREIGN KEY (`log_id`) REFERENCES `commuting_logs` (`log_id`)
                                            ON DELETE CASCADE,
                                    CONSTRAINT `cost_projections_ibfk_2`
                                        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
                                            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Dump completed
-- -----------------------------------------------------

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
