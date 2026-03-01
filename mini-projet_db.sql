-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 28 fév. 2026 à 18:20
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `mini-projet_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `id` int(11) NOT NULL,
  `nom_client` varchar(32) NOT NULL,
  `email` varchar(32) NOT NULL,
  `ville` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id`, `nom_client`, `email`, `ville`) VALUES
(1, 'Claude', 'claude@gmail.com', 'Cherbourg'),
(2, 'jean', 'jean@gmail.com', 'Caen'),
(3, 'Durand', 'durand@gmail.com', 'Paris'),
(4, 'Dupuis', 'dupuis@gmail.com', 'Nantes'),
(5, 'Martin', 'martin@gmail.com', 'Rennes'),
(6, 'Morel', 'morel@mail.com', 'Paris');

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

CREATE TABLE `commande` (
  `id` varchar(32) NOT NULL,
  `idClient` int(11) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`id`, `idClient`, `date`) VALUES
('C1', 3, '2022-05-25'),
('C10', 3, '2022-05-25'),
('C2', 4, '2026-02-02'),
('C3', 2, '2017-02-01'),
('C4', 5, '2016-06-08'),
('C5', 5, '2026-02-28'),
('C6', 6, '2021-09-23'),
('C7', 3, '2023-04-14'),
('C8', 4, '2025-10-24'),
('C9', 1, '2019-12-18');

-- --------------------------------------------------------

--
-- Structure de la table `lignes_commande`
--

CREATE TABLE `lignes_commande` (
  `id` int(11) NOT NULL,
  `idCommande` varchar(32) NOT NULL,
  `idProduit` int(11) NOT NULL,
  `prixAchat` double DEFAULT NULL,
  `quantité` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `lignes_commande`
--

INSERT INTO `lignes_commande` (`id`, `idCommande`, `idProduit`, `prixAchat`, `quantité`) VALUES
(1, 'C1', 3, 60, 3),
(2, 'C1', 4, 40, 4),
(3, 'C1', 5, 100, 1),
(5, 'C2', 8, 1500, 5),
(6, 'C2', 4, 90, 9),
(7, 'C6', 3, 20, 1),
(8, 'C3', 4, 30, 3),
(9, 'C4', 5, 900, 9),
(10, 'C4', 3, 460, 23),
(11, 'C4', 1, 4000, 8),
(12, 'C7', 6, 1200, 6),
(13, 'C6', 4, 40, 4),
(14, 'C8', 2, 167500, 67),
(15, 'C9', 8, 1200, 4),
(16, 'C9', 2, 12500, 5),
(17, 'C9', 4, 70, 7),
(18, 'C3', 5, 1000, 10),
(19, 'C5', 6, 2600, 13),
(20, 'C1', 6, 700, 7),
(21, 'C10', 3, 60, 3),
(22, 'C10', 4, 40, 4),
(23, 'C10', 5, 100, 1);


-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE `produit` (
  `id` int(11) NOT NULL,
  `nom` varchar(32) NOT NULL,
  `prix` double NOT NULL,
  `quantité` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `produit`
--

INSERT INTO `produit` (`id`, `nom`, `prix`, `quantité`) VALUES
(1, 'telephone', 500, 1),
(2, 'PC', 2500, 5),
(3, 'Clavier', 20, 394),
(4, 'Souris', 10, 392),
(5, 'Écran', 100, 398),
(6, 'Télévision', 200, 400),
(7, 'Frigo', 200, 200),
(8, 'Machine à laver', 300, 200),
(9, 'Chaise', 50, 250),
(10, 'Four', 600, 50),
(11, 'Table', 600, 20),
(12, 'Cafetière', 60, 200),
(13, 'PC portable', 5000, 20),
(14, 'Fauteuil', 460, 10),
(15, 'Lit', 900, 5),
(16, 'Meuble de rangement', 180, 120),
(17, 'Commode', 480, 40);

--
-- Déclencheurs `lignes_commande`
--
DELIMITER $$
CREATE TRIGGER `CALCULE_PRIX_ACHAT` BEFORE INSERT ON `lignes_commande` FOR EACH ROW BEGIN
    DECLARE prixFournisseur DOUBLE;

    SELECT prix
    INTO prixFournisseur
    FROM produit
    WHERE id = NEW.idProduit;

    SET NEW.prixAchat = prixFournisseur * NEW.quantité;
END
$$
DELIMITER ;


--
-- Index pour les tables déchargées
--

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Index pour la table `commande`
--
ALTER TABLE `commande`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_commande_idClient` (`idClient`);

--
-- Index pour la table `lignes_commande`
--
ALTER TABLE `lignes_commande`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_ligne_commande_idCommande` (`idCommande`),
  ADD KEY `fk_ligne_commande_idProduit` (`idProduit`);

--
-- Index pour la table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nom` (`nom`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `lignes_commande`
--
ALTER TABLE `lignes_commande`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `commande`
--
ALTER TABLE `commande`
  ADD CONSTRAINT `fk_commande_idClient` FOREIGN KEY (`idClient`) REFERENCES `client` (`id`);

--
-- Contraintes pour la table `lignes_commande`
--
ALTER TABLE `lignes_commande`
  ADD CONSTRAINT `fk_ligne_commande_idCommande` FOREIGN KEY (`idCommande`) REFERENCES `commande` (`id`),
  ADD CONSTRAINT `fk_ligne_commande_idProduit` FOREIGN KEY (`idProduit`) REFERENCES `produit` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
