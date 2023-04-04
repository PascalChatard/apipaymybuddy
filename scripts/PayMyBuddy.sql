-- phpMyAdmin SQL Dump
-- version 4.9.7
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : mar. 04 avr. 2023 à 17:16
-- Version du serveur :  10.3.29-MariaDB
-- Version de PHP : 7.4.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `PayMyBuddy`
--

-- --------------------------------------------------------

--
-- Structure de la table `account`
--

CREATE TABLE `account` (
  `id` int(11) NOT NULL,
  `opendate` date DEFAULT curdate(),
  `solde` double(6,2) DEFAULT 0.00,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `account`
--

INSERT INTO `account` (`id`, `opendate`, `solde`, `user_id`) VALUES
(1, '2021-12-02', 150.45, 1),
(2, '2021-11-22', 62.16, 2),
(3, '2021-10-15', 348.72, 3),
(4, '2021-11-22', 29.19, 4),
(5, '2022-12-26', 84.15, 5),
(6, '2022-12-26', 26.78, 6);

-- --------------------------------------------------------

--
-- Structure de la table `authorities`
--

CREATE TABLE `authorities` (
  `mail` varchar(255) NOT NULL,
  `authority` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `authorities`
--

INSERT INTO `authorities` (`mail`, `authority`) VALUES
('alejeune@outlook.com', 'ROLE_USER'),
('clegg.johny@outlook.com', 'ROLE_USER'),
('dupontlouis@hotmail.fr', 'ROLE_USER'),
('durand.jean@aol.com', 'ROLE_USER'),
('l.verleron@ddf.com', 'ROLE_USER'),
('lucgauvin@grd.fr', 'ROLE_USER');

-- --------------------------------------------------------

--
-- Structure de la table `rate`
--

CREATE TABLE `rate` (
  `id` int(11) NOT NULL,
  `value` double DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `rate`
--

INSERT INTO `rate` (`id`, `value`, `description`) VALUES
(1, 0.5, 'Taux rémunération standard');

-- --------------------------------------------------------

--
-- Structure de la table `transfer`
--

CREATE TABLE `transfer` (
  `id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp(),
  `description` varchar(255) DEFAULT NULL,
  `amount` double NOT NULL,
  `cost` double NOT NULL,
  `debited_account_id` int(11) DEFAULT NULL,
  `credited_account_id` int(11) DEFAULT NULL,
  `rate_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `transfer`
--

INSERT INTO `transfer` (`id`, `date`, `description`, `amount`, `cost`, `debited_account_id`, `credited_account_id`, `rate_id`) VALUES
(1, '2022-01-01 22:00:00', 'Remboursement ciné', 15.05, 0.08, 2, 3, 1),
(2, '2022-01-01 22:00:00', 'Participation cadeau', 5.5, 0.03, 2, 1, 1),
(3, '2022-01-04 22:00:00', 'Art de vie', 22.75, 0.11, 3, 1, 1),
(4, '2022-01-06 22:00:00', 'Cafet Crous', 7.2, 0.04, 3, 2, 1),
(5, '2022-01-09 22:00:00', 'Recharge café', 5.25, 0.03, 1, 2, 1),
(6, '2022-01-09 23:25:00', 'Paiement rufus', 37.95, 0.19, 1, 3, 1),
(7, '2022-12-26 15:22:30', 'Transfert N°3', 75.27, 0.38, 3, 5, 1),
(8, '2022-12-26 15:26:33', 'Transfert N°4', 45, 0.23, 3, 6, 1),
(9, '2022-12-26 15:38:23', 'Transfert n°5', 36.66, 0.18, 3, 2, 1),
(10, '2022-12-26 16:50:12', 'Transfert N°1', 12.55, 0.06, 6, 5, 1),
(11, '2022-12-27 15:45:06', 'Transfert n°1', 5.65, 0.03, 5, 3, 1),
(12, '2022-12-27 17:01:25', 'Transfert n°2', 3.69, 0.02, 6, 4, 1),
(13, '2022-12-28 15:09:00', 'Transfert n°3', 1.98, 0.01, 6, 5, 1);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `mail` varchar(255) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `firstname`, `lastname`, `address`, `city`, `phone`, `mail`, `password`, `enabled`) VALUES
(1, 'durand', 'jean', '56 impasse des souris', 'marseille', '0632467802', 'durand.jean@aol.com', '$2y$10$uLerot0eea8k9RKdJFoUgutlsIqu5k.Nc.SiJuGDk3Q5IbuLOtP.m', 1),
(2, 'dupont', 'louis', '15 rue du rouget', 'aix-en-provence', '0745235889', 'dupontlouis@hotmail.fr', '$2y$10$NrBCEywVmGQeqO8d5J0JXOR54J9sKQ6xNcywEkYpdIbrX6s648zsm', 1),
(3, 'LEJEUNE', 'ALAIN', '32 AVENUE LÉON BLOUM', 'PERTUIS', '0490255633', 'alejeune@outlook.com', '$2y$10$pVn8Zp1O4QrZ1u4n1BoZtOGqK6JnpGgW/XGkN33R20iY0NFAm6cuK', 1),
(4, 'Clegg', 'Johny', '17 Lotissement be bop a lula', 'Pertuis', '0632251398', 'clegg.johny@outlook.com', '$2y$10$L81x2ABzFIGiNKy2l2FbquJ9RYcdu9dh03rBc/BDP18GcgVMAOpAO', 1),
(5, 'LUC', 'GAUVIN', '12 AVENUE DE LA CHAPELLE', 'LIEUX DIT FONT JOYEUSE', '0765421236', 'lucgauvin@grd.fr', '$2y$10$xVtm5q9l1aZpUmllDOiuh.HR1NalKmqDiNpjlcnyCTaKt1JJyeBde', 1),
(6, 'LUCILE', 'VERLERON', '25 RUE DU MOULIN', 'AVIGNON', '0654896525', 'l.verleron@ddf.com', '$2y$10$PP.MFV7cc40QnnpRFQyul.Pfw9wjatfV7n6kbLLwjUXgqoF7UnFem', 1);

-- --------------------------------------------------------

--
-- Structure de la table `user_account`
--

CREATE TABLE `user_account` (
  `user_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `user_account`
--

INSERT INTO `user_account` (`user_id`, `account_id`) VALUES
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

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Index pour la table `authorities`
--
ALTER TABLE `authorities`
  ADD UNIQUE KEY `mail` (`mail`,`authority`) USING BTREE;

--
-- Index pour la table `rate`
--
ALTER TABLE `rate`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `transfer`
--
ALTER TABLE `transfer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `debited_account_id` (`debited_account_id`),
  ADD KEY `credited_account_id` (`credited_account_id`),
  ADD KEY `rate_id` (`rate_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `mail` (`mail`);

--
-- Index pour la table `user_account`
--
ALTER TABLE `user_account`
  ADD PRIMARY KEY (`user_id`,`account_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `account_id` (`account_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `account`
--
ALTER TABLE `account`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `rate`
--
ALTER TABLE `rate`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `transfer`
--
ALTER TABLE `transfer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `authorities`
--
ALTER TABLE `authorities`
  ADD CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`mail`) REFERENCES `user` (`mail`) ON DELETE CASCADE;

--
-- Contraintes pour la table `transfer`
--
ALTER TABLE `transfer`
  ADD CONSTRAINT `transfer_ibfk_1` FOREIGN KEY (`debited_account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `transfer_ibfk_2` FOREIGN KEY (`credited_account_id`) REFERENCES `account` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `transfer_ibfk_3` FOREIGN KEY (`rate_id`) REFERENCES `rate` (`id`);

--
-- Contraintes pour la table `user_account`
--
ALTER TABLE `user_account`
  ADD CONSTRAINT `user_account_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `user_account_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
