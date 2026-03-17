USE `commuter_test`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `transportation_costs`;
DROP TABLE IF EXISTS `cost_analyses`;
DROP TABLE IF EXISTS `commuting_logs`;
DROP TABLE IF EXISTS `profiles`;

CREATE TABLE `profiles` (
                            `id` VARCHAR(255) NOT NULL,
                            `first_name` VARCHAR(100) DEFAULT NULL,
                            `last_name` VARCHAR(100) DEFAULT NULL,
                            `email` VARCHAR(100) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `profiles_email_unique` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `commuting_logs` (
                                  `log_id` INT AUTO_INCREMENT PRIMARY KEY,
                                  `user_id` VARCHAR(255) DEFAULT NULL,
                                  `transportation_mode` VARCHAR(50) DEFAULT NULL,
                                  `date_added` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  `duration_in_minutes` INT DEFAULT NULL,
                                  `commute_type` VARCHAR(50) DEFAULT 'Work',
                                  `distance_in_miles` DOUBLE DEFAULT NULL,
                                  `cost` DOUBLE DEFAULT NULL,
                                  INDEX `idx_commuting_logs_user_id` (`user_id`),
                                  CONSTRAINT `commuting_logs_ibfk_1`
                                      FOREIGN KEY (`user_id`) REFERENCES `profiles`(`id`)
                                          ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `cost_analyses` (
                                 `analysis_id` INT AUTO_INCREMENT PRIMARY KEY,
                                 `user_id` VARCHAR(255) DEFAULT NULL,
                                 `commute_type` VARCHAR(50) DEFAULT NULL,
                                 `one_year_cost` DOUBLE DEFAULT NULL,
                                 `two_year_cost` DOUBLE DEFAULT NULL,
                                 `five_year_cost` DOUBLE DEFAULT NULL,
                                 `total_cost` DOUBLE DEFAULT NULL,
                                 INDEX `idx_cost_analyses_user_id` (`user_id`),
                                 CONSTRAINT `cost_analyses_ibfk_1`
                                     FOREIGN KEY (`user_id`) REFERENCES `profiles`(`id`)
                                         ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `transportation_costs` (
                                        `profile_id` INT AUTO_INCREMENT PRIMARY KEY,
                                        `user_id` VARCHAR(255) DEFAULT NULL,
                                        `insurance_cost` DOUBLE DEFAULT NULL,
                                        `vehicle_type` VARCHAR(50) DEFAULT NULL,
                                        `fuel_cost` DOUBLE DEFAULT NULL,
                                        `maintenance_cost` DOUBLE DEFAULT NULL,
                                        `public_transport_cost` DOUBLE DEFAULT NULL,
                                        `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        `miles_per_gallon` DOUBLE DEFAULT NULL,
                                        `monthly_payment` DOUBLE DEFAULT NULL,
                                        INDEX `idx_transportation_costs_user_id` (`user_id`),
                                        CONSTRAINT `transportation_costs_ibfk_1`
                                            FOREIGN KEY (`user_id`) REFERENCES `profiles`(`id`)
                                                ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;