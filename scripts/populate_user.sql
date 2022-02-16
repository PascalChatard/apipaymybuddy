-- ALTER TABLE `user` DISABLE KEYS 
LOCK TABLES `user` WRITE;

INSERT INTO `user` VALUES (1,'durand','jean','56 impasse des souris','marseille', '0632467802','durand.jean@aol.com'),
                          (2,'dupont','louis','15 rue du rouget','aix-en-provence', '0745235889','dupontlouis@hotmail.fr'),
						  (3,'Lejeune','alain','32 avenue léon bloum', 'pertuis', '0490255633','alejeune@outlook.com');

-- ALTER TABLE `user` ENABLE KEYS
UNLOCK TABLES;
