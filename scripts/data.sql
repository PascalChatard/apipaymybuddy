-- ------------------------------------------------------------------------------
-- - Reconstruction de la base de données                                     ---
-- ------------------------------------------------------------------------------

DROP DATABASE IF EXISTS PayMyBuddy;
CREATE DATABASE PayMyBuddy;
USE PayMyBuddy;

-- -----------------------------------------------------------------------------
-- - Construction de la table des utilisateurs                               ---
-- -----------------------------------------------------------------------------
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,	
  `address` varchar(255) DEFAULT NULL,	
  `city` varchar(50) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `mail` varchar(255) NOT NULL UNIQUE,
  `password` varchar(100) DEFAULT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `user` VALUES 
(1, 'durand', 'jean', '56 impasse des souris', 'marseille', '0632467802', 'durand.jean@aol.com', '$2y$10$uLerot0eea8k9RKdJFoUgutlsIqu5k.Nc.SiJuGDk3Q5IbuLOtP.m', 1),
(2, 'dupont', 'louis', '15 rue du rouget', 'aix-en-provence', '0745235889', 'dupontlouis@hotmail.fr', '$2y$10$NrBCEywVmGQeqO8d5J0JXOR54J9sKQ6xNcywEkYpdIbrX6s648zsm', 1),
(3, 'LEJEUNE', 'ALAIN', '32 AVENUE LÉON BLOUM', 'PERTUIS', '0490255633', 'alejeune@outlook.com', '$2y$10$pVn8Zp1O4QrZ1u4n1BoZtOGqK6JnpGgW/XGkN33R20iY0NFAm6cuK', 1),
(4, 'Clegg', 'Johny', '17 Lotissement be bop a lula', 'Pertuis', '0632251398', 'clegg.johny@outlook.com', '$2y$10$L81x2ABzFIGiNKy2l2FbquJ9RYcdu9dh03rBc/BDP18GcgVMAOpAO', 1),
(5, 'LUC', 'GAUVIN', '12 AVENUE DE LA CHAPELLE', 'LIEUX DIT FONT JOYEUSE', '0765421236', 'lucgauvin@grd.fr', '$2y$10$xVtm5q9l1aZpUmllDOiuh.HR1NalKmqDiNpjlcnyCTaKt1JJyeBde', 1),
(6, 'LUCILE', 'VERLERON', '25 RUE DU MOULIN', 'AVIGNON', '0654896525', 'l.verleron@ddf.com', '$2y$10$PP.MFV7cc40QnnpRFQyul.Pfw9wjatfV7n6kbLLwjUXgqoF7UnFem', 1);

SELECT * FROM `user`;



-- -----------------------------------------------------------------------------
-- - Construction de la table des comptes d'utilisateur                      ---
-- -----------------------------------------------------------------------------
CREATE TABLE `account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `opendate` DATE DEFAULT CURRENT_DATE,
  `solde` DOUBLE(6,2) DEFAULT 0.0,	
  `user_id` INT DEFAULT NULL,	
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE   
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `account` VALUES 
(1, '2021-12-02', 150.45, 1),
(2, '2021-11-22', 62.16, 2),
(3, '2021-10-15', 348.72, 3),
(4, '2021-11-22', 29.19, 4),
(5, '2022-12-26', 84.15, 5),
(6, '2022-12-26', 26.78, 6);


SELECT * FROM `account`;



-- -----------------------------------------------------------------------------
-- - Construction de la table d'association utilisateur-compte               ---
-- -----------------------------------------------------------------------------
CREATE TABLE `user_account` (
  `user_id` INT NOT NULL,
  `account_id` INT NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT FOREIGN KEY (`user_id`)    REFERENCES `user`    (`id`) ON DELETE CASCADE,    
  CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE,
  PRIMARY KEY (`user_id`,`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `user_account` VALUES 
(1, 3),
(2, 1),
(2, 3),
(3, 1),
(3, 2),
(3, 5),
(4, 6),
(5, 3),
(5, 6),
(6, 2),
(6, 3);

SELECT * FROM `user_account`;



-- -----------------------------------------------------------------------------
-- - Construction de la table des taux de rémunération                       ---
-- -----------------------------------------------------------------------------
CREATE TABLE `rate` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` DOUBLE DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `rate` VALUES (1,0.5,'Taux rémunération standard');


SELECT * FROM `rate`;



-- -----------------------------------------------------------------------------
-- - Construction de la table des transfrts d'argent                         ---
-- -----------------------------------------------------------------------------
CREATE TABLE `transfer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` VARCHAR(255),
  `amount` DOUBLE NOT NULL,
  `cost` DOUBLE NOT NULL,
  `debited_account_id` int DEFAULT NULL,
  `credited_account_id` int DEFAULT NULL,
  `rate_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `debited_account_id` (`debited_account_id`),
  KEY `credited_account_id` (`credited_account_id`),
  KEY `rate_id` (`rate_id`),
  CONSTRAINT FOREIGN KEY (`debited_account_id`)  REFERENCES `account` (`id`)  ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT FOREIGN KEY (`credited_account_id`) REFERENCES `account` (`id`)  ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT FOREIGN KEY (`rate_id`)             REFERENCES `rate`    (`id`)  ON DELETE RESTRICT ON UPDATE RESTRICT    	
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `transfer` VALUES  
(1, '2022-01-01 23:00:00', 'Remboursement ciné', 15.05, 0.08, 2, 3, 1),
(2, '2022-01-01 23:00:00', 'Participation cadeau', 5.5, 0.03, 2, 1, 1),
(3, '2022-01-04 23:00:00', 'Art de vie', 22.75, 0.11, 3, 1, 1),
(4, '2022-01-06 23:00:00', 'Cafet Crous', 7.2, 0.04, 3, 2, 1),
(5, '2022-01-09 23:00:00', 'Recharge café', 5.25, 0.03, 1, 2, 1),
(6, '2022-01-10 00:25:00', 'Paiement rufus', 37.95, 0.19, 1, 3, 1),
(7, '2022-12-26 16:22:30', 'Transfert N°3', 75.27, 0.38, 3, 5, 1),
(8, '2022-12-26 16:26:33', 'Transfert N°4', 45, 0.23, 3, 6, 1),
(9, '2022-12-26 16:38:23', 'Transfert n°5', 36.66, 0.18, 3, 2, 1),
(10, '2022-12-26 17:50:12', 'Transfert N°1', 12.55, 0.06, 6, 5, 1),
(11, '2022-12-27 16:45:06', 'Transfert n°1', 5.65, 0.03, 5, 3, 1),
(12, '2022-12-27 18:01:25', 'Transfert n°2', 3.69, 0.02, 6, 4, 1),
(13, '2022-12-28 16:09:00', 'Transfert n°3', 1.98, 0.01, 6, 5, 1);


SELECT * FROM `transfer`;


-- -----------------------------------------------------------------------------
-- - Construction de la table des autorisations                              ---
-- -----------------------------------------------------------------------------
CREATE TABLE `authorities` (
  `mail` varchar(255) NOT NULL UNIQUE,	
  `authority` VARCHAR(50) NOT NULL,
  UNIQUE KEY `mail` (`mail`,`authority`) USING BTREE,
  CONSTRAINT FOREIGN KEY (`mail`)  REFERENCES `user` (`mail`)  ON DELETE CASCADE ON UPDATE RESTRICT  	
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `authorities` VALUES  ('durand.jean@aol.com','ROLE_USER'), 
                                  ('dupontlouis@hotmail.fr','ROLE_USER'), 
							      ('alejeune@outlook.com','ROLE_USER'), 
							      ('clegg.johny@outlook.com','ROLE_USER'),
                                  ('lucgauvin@grd.fr','ROLE_USER'),
							      ('l.verleron@ddf.com','ROLE_USER');


SELECT * FROM `authorities`;





