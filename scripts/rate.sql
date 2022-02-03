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
