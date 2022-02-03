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
