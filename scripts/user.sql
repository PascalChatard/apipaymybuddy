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
