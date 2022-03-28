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
  `password` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `user` VALUES (1,'durand','jean','56 impasse des souris','marseille', '0632467802','durand.jean@aol.com',"jean2022"),
                          (2,'dupont','louis','15 rue du rouget','aix-en-provence', '0745235889','dupontlouis@hotmail.fr',"louis2022"),
						  (3,'Lejeune','alain','32 avenue léon bloum', 'pertuis', '0490255633','alejeune@outlook.com',"alain2022"),
						  (4,'Clegg', 'Johny','17 Lotissement be bop a lula', 'Pertuis','0632251398','clegg.johny@outlook.com','johny2022');


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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `account` VALUES (1,'2021-12-02',150.45,1),
                             (2,'2021-11-22',25.50,2),
							 (3,'2021-10-15',500,3);


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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `user_account` VALUES (2,1),
                                  (3,1),
								  (3,2),
								  (1,3);

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `transfer` VALUES  (1, '2022-01-02 00:00:00','Remboursement ciné',15.05, 0.08,2,3,1), 
                               (2, '2022-01-02 00:00:00','Participation cadeau',5.50, 0.03,2,1,1), 
							   (3, '2022-01-05 00:00:00','Art de vie',22.75,0.11,3,1,1), 
							   (4, '2022-01-07 00:00:00','Cafet Crous',7.20,0.04,3,2,1),
                               (5, '2022-01-10 00:00:00','Recharge café',5.25,0.03,1,2,1),
							   (6, '2022-01-10 01:25:00','Paiement rufus',37.95,0.19,1,3,1);


SELECT * FROM `transfer`;




