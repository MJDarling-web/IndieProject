-- cleandb.sql for commuter_test schema
USE `commuter_test`;
SET FOREIGN_KEY_CHECKS = 0;

-- 1) Drop tables in reverse‑dependency order
DROP TABLE IF EXISTS `transportation_costs`;
DROP TABLE IF EXISTS `cost_analyses`;
DROP TABLE IF EXISTS `commuting_logs`;
DROP TABLE IF EXISTS `users`;

-- 2) Recreate USERS first
CREATE TABLE `users` (
                         `id` INT AUTO_INCREMENT PRIMARY KEY,
                         `first_name` VARCHAR(100),
                         `last_name`  VARCHAR(100),
                         `email`      VARCHAR(100),
                         `password`   VARCHAR(255),
                         UNIQUE KEY `users_email_unique` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) Recreate COMMUTING_LOGS (no more transportation_mode)
CREATE TABLE `commuting_logs` (
                                  `log_id`            INT AUTO_INCREMENT PRIMARY KEY,
                                  `user_id`           INT,
                                  `date_added`        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
                                  `duration_in_minutes` INT,
                                  `commute_type`      VARCHAR(50),
                                  `distance_in_miles` FLOAT,
                                  `cost`              INT,
                                  INDEX `idx_commuting_logs_user_id` (`user_id`),
                                  CONSTRAINT `commuting_logs_ibfk_1`
                                      FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
                                          ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4) Recreate COST_ANALYSES
CREATE TABLE `cost_analyses` (
                                 `analysis_id`   INT AUTO_INCREMENT PRIMARY KEY,
                                 `user_id`       INT,
                                 `commute_type`  VARCHAR(50),
                                 `one_year_cost` FLOAT,
                                 `two_year_cost` FLOAT,
                                 `five_year_cost`FLOAT,
                                 `total_cost`    FLOAT,
                                 INDEX `idx_cost_analyses_user_id` (`user_id`),
                                 CONSTRAINT `cost_analyses_ibfk_1`
                                     FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
                                         ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5) Recreate TRANSPORTATION_COSTS (if needed)
CREATE TABLE `transportation_costs` (
                                        `profile_id`            INT AUTO_INCREMENT PRIMARY KEY,
                                        `user_id`               INT,
                                        `insurance_cost`        DOUBLE,
                                        `vehicle_type`          VARCHAR(50),
                                        `fuel_cost`             DOUBLE,
                                        `maintenance_cost`      DOUBLE,
                                        `public_transport_cost` DOUBLE,
                                        `created_date`          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        `miles_per_gallon`      DOUBLE,
                                        `monthly_payment`       DOUBLE,
                                        INDEX `idx_transportation_costs_user_id` (`user_id`),
                                        CONSTRAINT `transportation_costs_ibfk_1`
                                            FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
                                                ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6) Seed data for DAO tests (only the columns your entity needs)
INSERT INTO users (first_name, last_name, email, password) VALUES
                                                               ('Alice', 'Wonder',  'alice@example.com', 'pw1'),
                                                               ('Bob',   'Builder', 'bob@example.com',   'pw2');

INSERT INTO commuting_logs (user_id, duration_in_minutes, commute_type, distance_in_miles, cost) VALUES
                                                                                                     (1, 30, 'Car',  10.5, 5),
                                                                                                     (1, 45, 'Bus',  12.0, 3),
                                                                                                     (2, 20, 'Bike',  5.2, 0);

SET FOREIGN_KEY_CHECKS = 1;
