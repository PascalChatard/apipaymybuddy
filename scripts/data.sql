USE PayMyBuddy;

SET FOREIGN_KEY_CHECKS = 0; -- to disable FK checks (ex: for delete purpose)
DROP TABLE IF EXISTS `user`;
SET FOREIGN_KEY_CHECKS = 1; -- to re-enable FK checks


CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,	
  `address` varchar(255) DEFAULT NULL,	
  `city` varchar(50) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `mail` varchar(255) NOT NULL UNIQUE,
  `password` varchar(64) DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT  FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE  
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ALTER TABLE `user` DISABLE KEYS 
LOCK TABLES `user` WRITE;

INSERT INTO `user` VALUES (1,'durand','jean','56 impasse des souris','marseille', '0632467802','durand.jean@aol.com',"jean2022",1),
                          (2,'dupont','louis','15 rue du rouget','aix-en-provence', '0745235889','dupontlouis@hotmail.fr',"louis2022",2),
						  (3,'Lejeune','alain','32 avenue léon bloum', 'pertuis', '0490255633','alejeune@outlook.com',"alain2022",3);

-- ALTER TABLE `user` ENABLE KEYS
UNLOCK TABLES;



SET FOREIGN_KEY_CHECKS = 0; -- to disable FK checks (ex: for delete purpose)
DROP TABLE IF EXISTS `account`;
SET FOREIGN_KEY_CHECKS = 1; -- to re-enable FK checks


CREATE TABLE `account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `opendate` DATE DEFAULT '1970-01-01',
  `solde` DOUBLE DEFAULT NULL,	
  `user_id` int DEFAULT NULL,	
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE    
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ALTER TABLE `account` DISABLE KEYS 
LOCK TABLES `account` WRITE;

INSERT INTO `account` VALUES (1,'2021-12-02',150.45,1),
                             (2,'2021-11-22',25.50,2),
							 (3,'2021-10-15',500,3);

-- ALTER TABLE `account` ENABLE KEYS
UNLOCK TABLES;



SET FOREIGN_KEY_CHECKS = 0; -- to disable FK checks (ex: for delete purpose)
DROP TABLE IF EXISTS `user_account`;
SET FOREIGN_KEY_CHECKS = 1; -- to re-enable FK checks


CREATE TABLE `user_account` (
  `user_id` INT NOT NULL,
  `account_id` INT NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,    
  CONSTRAINT FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON UPDATE CASCADE,
  PRIMARY KEY (`user_id`,`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ALTER TABLE `user_account` DISABLE KEYS 
LOCK TABLES `user_account` WRITE;

INSERT INTO `user_account` VALUES (2,1),
                                  (3,1),
								  (3,2),
								  (1,3);

-- ALTER TABLE `user_account` ENABLE KEYS
UNLOCK TABLES;


SET FOREIGN_KEY_CHECKS = 0; -- to disable FK checks (ex: for delete purpose)
DROP TABLE IF EXISTS `transfer`;
SET FOREIGN_KEY_CHECKS = 1; -- to re-enable FK checks


CREATE TABLE `transfer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL DEFAULT '1970-01-01',
  `description` VARCHAR(255),
  `amount` DOUBLE NOT NULL,
  `debited_account_id` int DEFAULT NULL,
  `credited_account_id` int DEFAULT NULL,
  `rate_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `debited_account_id` (`debited_account_id`),
  KEY `credited_account_id` (`credited_account_id`),
  KEY `rate_id` (`rate_id`),
  CONSTRAINT  FOREIGN KEY (`debited_account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`credited_account_id`) REFERENCES `account` (`id`) ON DELETE SET NULL,
  CONSTRAINT  FOREIGN KEY (`rate_id`) REFERENCES `rate` (`id`) ON DELETE SET NULL    	
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ALTER TABLE `transfer` DISABLE KEYS 
LOCK TABLES `transfer` WRITE;

INSERT INTO `transfer` VALUES  (1, '2022-01-02','Remboursement ciné',15.05,2,3,1),
                               (2, '2022-01-02','Participation cadeau',5.50,2,1,1),
							   (3, '2022-01-05','Art de vie',22.75,3,1,1),
							   (4, '2022-01-07','Cafet Crous',7.20,3,2,1),
                               (5, '2022-01-10','Recharge café',5.25,1,2,1),
							   (6, '2022-01-10','Paiement rufus',37.95,1,3,1);

-- ALTER TABLE `transfer` ENABLE KEYS
UNLOCK TABLES;



SET FOREIGN_KEY_CHECKS = 0; -- to disable FK checks (ex: for delete purpose)
DROP TABLE IF EXISTS `rate`;
SET FOREIGN_KEY_CHECKS = 1; -- to re-enable FK checks


CREATE TABLE `rate` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` DOUBLE DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ALTER TABLE `rate` DISABLE KEYS 
LOCK TABLES `rate` WRITE;

INSERT INTO `rate` VALUES (1,0.5);

-- ALTER TABLE `rate` ENABLE KEYS
UNLOCK TABLES;

