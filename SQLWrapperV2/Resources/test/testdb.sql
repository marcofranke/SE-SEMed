-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.5.16 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2013-02-10 22:22:49
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping database structure for test
DROP DATABASE IF EXISTS `test`;
CREATE DATABASE IF NOT EXISTS `test` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `test`;


-- Dumping structure for table test.artikel
DROP TABLE IF EXISTS `artikel`;
CREATE TABLE IF NOT EXISTS `artikel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `artikelnr` varchar(20) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '0',
  `beschreibung` varchar(250) DEFAULT NULL,
  `preis` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table test.artikel: ~4 rows (approximately)
/*!40000 ALTER TABLE `artikel` DISABLE KEYS */;
INSERT INTO `artikel` (`id`, `artikelnr`, `name`, `beschreibung`, `preis`) VALUES
	(1, 'A0000001', 'Artikel Nr. 1', 'Artikelbeschreibung Nr 1', 1.11),
	(2, 'A0000002', 'Artikel Nr. 2', 'Artikelbeschreibung Nr 2', 2.22),
	(3, 'A0000003', 'Artikel Nr. 3', 'Artikelbeschreibung Nr 3', 3.33),
	(4, 'A0000004', 'Artikel Nr. 4', 'Artikelbeschreibung Nr 4', 4.44);
/*!40000 ALTER TABLE `artikel` ENABLE KEYS */;


-- Dumping structure for table test.artikelkategorie
DROP TABLE IF EXISTS `artikelkategorie`;
CREATE TABLE IF NOT EXISTS `artikelkategorie` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bezeichnung` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table test.artikelkategorie: ~5 rows (approximately)
/*!40000 ALTER TABLE `artikelkategorie` DISABLE KEYS */;
INSERT INTO `artikelkategorie` (`id`, `bezeichnung`) VALUES
	(1, 'Kategorie1'),
	(2, 'Kategorie2'),
	(3, 'Kategorie3'),
	(4, 'Kategorie4'),
	(5, 'Kategorie5');
/*!40000 ALTER TABLE `artikelkategorie` ENABLE KEYS */;


-- Dumping structure for table test.artikel_artikelkategorie
DROP TABLE IF EXISTS `artikel_artikelkategorie`;
CREATE TABLE IF NOT EXISTS `artikel_artikelkategorie` (
  `artikel` bigint(20) NOT NULL,
  `kategorie` bigint(20) NOT NULL,
  PRIMARY KEY (`artikel`,`kategorie`),
  KEY `FK_artikel_artikelkategorie_artikelkategorie` (`kategorie`),
  CONSTRAINT `FK_artikel_artikelkategorie_artikelkategorie` FOREIGN KEY (`kategorie`) REFERENCES `artikelkategorie` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_artikel_artikelkategorie_artikel` FOREIGN KEY (`artikel`) REFERENCES `artikel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table test.artikel_artikelkategorie: ~0 rows (approximately)
/*!40000 ALTER TABLE `artikel_artikelkategorie` DISABLE KEYS */;
INSERT INTO `artikel_artikelkategorie` (`artikel`, `kategorie`) VALUES
	(1, 1),
	(4, 1),
	(1, 2),
	(2, 2),
	(4, 4),
	(4, 5);
/*!40000 ALTER TABLE `artikel_artikelkategorie` ENABLE KEYS */;


-- Dumping structure for table test.bestellungen
DROP TABLE IF EXISTS `bestellungen`;
CREATE TABLE IF NOT EXISTS `bestellungen` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `kunde` bigint(20) NOT NULL,
  `artikel` bigint(20) NOT NULL,
  `lieferadresse` bigint(20) NOT NULL,
  `menge` int(11) NOT NULL,
  `preis` float NOT NULL,
  `datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_bestellungen_kunde` (`kunde`),
  KEY `FK_bestellungen_artikel` (`artikel`),
  KEY `FK_bestellungen_lieferadressen` (`lieferadresse`),
  CONSTRAINT `FK_bestellungen_artikel` FOREIGN KEY (`artikel`) REFERENCES `artikel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_bestellungen_kunde` FOREIGN KEY (`kunde`) REFERENCES `kunde` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_bestellungen_lieferadressen` FOREIGN KEY (`lieferadresse`) REFERENCES `lieferadressen` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table test.bestellungen: ~4 rows (approximately)
/*!40000 ALTER TABLE `bestellungen` DISABLE KEYS */;
INSERT INTO `bestellungen` (`id`, `kunde`, `artikel`, `lieferadresse`, `menge`, `preis`, `datetime`) VALUES
	(1, 1, 1, 1, 1, 1.11, '2013-02-08 21:34:53'),
	(2, 2, 2, 2, 2, 2.22, '2013-02-08 21:36:37'),
	(3, 1, 2, 1, 2, 2.22, '2013-02-08 21:34:53'),
	(4, 3, 4, 3, 2, 6.43, '2013-02-08 21:36:37');
/*!40000 ALTER TABLE `bestellungen` ENABLE KEYS */;


-- Dumping structure for table test.enummern
DROP TABLE IF EXISTS `enummern`;
CREATE TABLE IF NOT EXISTS `enummern` (
  `artikel_id` bigint(20) NOT NULL,
  `enummer` varchar(10) NOT NULL,
  KEY `FK__artikel` (`artikel_id`),
  CONSTRAINT `FK__artikel` FOREIGN KEY (`artikel_id`) REFERENCES `artikel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table test.enummern: ~6 rows (approximately)
/*!40000 ALTER TABLE `enummern` DISABLE KEYS */;
INSERT INTO `enummern` (`artikel_id`, `enummer`) VALUES
	(1, 'e1111'),
	(1, 'e3333'),
	(1, 'e2222'),
	(2, 'e4444'),
	(2, 'e5555'),
	(3, 'e6666');
/*!40000 ALTER TABLE `enummern` ENABLE KEYS */;


-- Dumping structure for table test.kunde
DROP TABLE IF EXISTS `kunde`;
CREATE TABLE IF NOT EXISTS `kunde` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `vorname` varchar(50) NOT NULL,
  `strasse` varchar(50) NOT NULL,
  `plz` int(11) NOT NULL,
  `ort` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table test.kunde: ~3 rows (approximately)
/*!40000 ALTER TABLE `kunde` DISABLE KEYS */;
INSERT INTO `kunde` (`id`, `name`, `vorname`, `strasse`, `plz`, `ort`) VALUES
	(1, 'Name1', 'Vorname1', 'Strasse1', 11111, 'Ort1'),
	(2, 'Name2', 'Vorname2', 'Strasse2', 22222, 'Ort2'),
	(3, 'Name3', 'Vorname3', 'Strasse3', 33333, 'Ort3');
/*!40000 ALTER TABLE `kunde` ENABLE KEYS */;


-- Dumping structure for table test.lieferadressen
DROP TABLE IF EXISTS `lieferadressen`;
CREATE TABLE IF NOT EXISTS `lieferadressen` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `strasse` varchar(50) NOT NULL DEFAULT '0',
  `ort` varchar(50) NOT NULL DEFAULT '0',
  `plz` int(11) NOT NULL DEFAULT '0',
  `vorname` varchar(50) NOT NULL DEFAULT '0',
  `nachname` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table test.lieferadressen: ~3 rows (approximately)
/*!40000 ALTER TABLE `lieferadressen` DISABLE KEYS */;
INSERT INTO `lieferadressen` (`id`, `strasse`, `ort`, `plz`, `vorname`, `nachname`) VALUES
	(1, 'lieferstrasse1', 'lieferort1', 11111, 'liefervorname1', 'liefernachname1'),
	(2, 'lieferstrasse2', 'lieferort2', 22222, 'liefervorname2', 'liefernachname2'),
	(3, 'lieferstrasse3', 'lieferort3', 33333, 'liefervorname3', 'liefernachname3');
/*!40000 ALTER TABLE `lieferadressen` ENABLE KEYS */;
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
