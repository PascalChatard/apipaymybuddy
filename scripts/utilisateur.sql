DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE `utilisateur` (
  `utilisateur_id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,	
  `adresse` varchar(255) DEFAULT NULL,	
  `ville` varchar(50) DEFAULT NULL,
  `telephone` varchar(10) DEFAULT NULL,
  `email` varchar(255) NOT NULL UNIQUE,	
  PRIMARY KEY (`utilisateur_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ALTER TABLE `utilisateur` DISABLE KEYS 
LOCK TABLES `utilisateur` WRITE;

INSERT INTO `utilisateur` VALUES (1,'durand','jean','56 impasse des souris','marseille', '0632467802','durand.jean@aol.com'),
                                 (2,'dupont','louis','15 rue du rouget','aix-en-provence', '0745235889','dupontlouis@hotmail.fr'),
								 (3,'Lejeune','alain','32 avenue léon bloum', 'pertuis', '0490255633','alejeune@outlook.com');

-- ALTER TABLE `utilisateur` ENABLE KEYS
UNLOCK TABLES;
