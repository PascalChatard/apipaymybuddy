USE PayMyBuddy;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,	
  `address` varchar(255) DEFAULT NULL,	
  `city` varchar(50) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `mail` varchar(255) NOT NULL UNIQUE,	
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ALTER TABLE `user` DISABLE KEYS 
LOCK TABLES `user` WRITE;

INSERT INTO `user` VALUES (1,'durand','jean','56 impasse des souris','marseille', '0632467802','durand.jean@aol.com'),
                                 (2,'dupont','louis','15 rue du rouget','aix-en-provence', '0745235889','dupontlouis@hotmail.fr'),
								 (3,'Lejeune','alain','32 avenue léon bloum', 'pertuis', '0490255633','alejeune@outlook.com');

-- ALTER TABLE `user` ENABLE KEYS
UNLOCK TABLES;



DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `opendate` DATE DEFAULT '1970-01-01',
  `solde` DOUBLE DEFAULT NULL,	
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ALTER TABLE `account` DISABLE KEYS 
LOCK TABLES `account` WRITE;

INSERT INTO `account` VALUES (1,'2021-12-02',150.45),
                             (2,'2021-11-22',25.50),
							 (3,'2021-10-15',500);

-- ALTER TABLE `account` ENABLE KEYS
UNLOCK TABLES;



DROP TABLE IF EXISTS `rate`;
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



DROP TABLE IF EXISTS `transfer`;
CREATE TABLE `transfer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL DEFAULT '1970-01-01',
  `description` VARCHAR(255),
  `amount` DOUBLE NOT NULL,	
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ALTER TABLE `transfer` DISABLE KEYS 
LOCK TABLES `transfer` WRITE;

INSERT INTO `transfer` VALUES  (1, '2022-01-02','Remboursement ciné',15.05),
                               (2, '2022-01-02','Participation cadeau',5.50),
							   (3, '2022-01-05','Art de vie',22.75),
							   (4, '2022-01-07','Cafet Crous',7.20),
                               (5, '2022-01-10','Recharge café',5.25),
							   (6, '2022-01-10','Paiement rufus',37.95);

-- ALTER TABLE `transfer` ENABLE KEYS
UNLOCK TABLES;
