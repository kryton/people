-- MySQL dump 10.16  Distrib 10.1.22-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: project_db
-- ------------------------------------------------------
-- Server version	10.1.22-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `empefficiency`
--

DROP TABLE IF EXISTS `empefficiency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `empefficiency` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `month` int(11) NOT NULL,
  `efficiency` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `month` (`month`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empefficiency`
--

/*!40000 ALTER TABLE `empefficiency` DISABLE KEYS */;
/*!40000 ALTER TABLE `empefficiency` ENABLE KEYS */;

--
-- Table structure for table `featureflag`
--

DROP TABLE IF EXISTS `featureflag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `featureflag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `featureflag`
--

/*!40000 ALTER TABLE `featureflag` DISABLE KEYS */;
/*!40000 ALTER TABLE `featureflag` ENABLE KEYS */;

--
-- Table structure for table `managedclient`
--

DROP TABLE IF EXISTS `managedclient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `managedclient` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `isManaged` bit(1) DEFAULT b'1',
  `msprojectname` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `managedclient`
--

/*!40000 ALTER TABLE `managedclient` DISABLE KEYS */;
/*!40000 ALTER TABLE `managedclient` ENABLE KEYS */;

--
-- Table structure for table `managedclientproductfeature`
--

DROP TABLE IF EXISTS `managedclientproductfeature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `managedclientproductfeature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productFeatureId` int(11) NOT NULL,
  `managedClientId` int(11) NOT NULL,
  `allocation` decimal(5,2) unsigned NOT NULL DEFAULT '1.00',
  PRIMARY KEY (`id`),
  KEY `productFeatureId` (`productFeatureId`),
  KEY `managedClientId` (`managedClientId`),
  CONSTRAINT `ManagedClientProductFeature_ibfk_1` FOREIGN KEY (`productFeatureId`) REFERENCES `productfeature` (`id`),
  CONSTRAINT `ManagedClientProductFeature_ibfk_2` FOREIGN KEY (`managedClientId`) REFERENCES `managedclient` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `managedclientproductfeature`
--

/*!40000 ALTER TABLE `managedclientproductfeature` DISABLE KEYS */;
/*!40000 ALTER TABLE `managedclientproductfeature` ENABLE KEYS */;

--
-- Table structure for table `play_evolutions`
--

DROP TABLE IF EXISTS `play_evolutions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `play_evolutions` (
  `id` int(11) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `applied_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `apply_script` text,
  `revert_script` text,
  `state` varchar(255) DEFAULT NULL,
  `last_problem` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `play_evolutions`
--

/*!40000 ALTER TABLE `play_evolutions` DISABLE KEYS */;
/*!40000 ALTER TABLE `play_evolutions` ENABLE KEYS */;

--
-- Table structure for table `productfeature`
--

DROP TABLE IF EXISTS `productfeature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productfeature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `execSummary` text NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT '0',
  `stageId` int(11) NOT NULL DEFAULT '1',
  `msprojectname` varchar(200) DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `isCID` bit(1) DEFAULT b'0',
  `isAnchor` bit(1) DEFAULT b'0',
  `msPriority` int(11) DEFAULT '0',
  `start` date DEFAULT NULL,
  `finish` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=640 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productfeature`
--

/*!40000 ALTER TABLE `productfeature` DISABLE KEYS */;
/*!40000 ALTER TABLE `productfeature` ENABLE KEYS */;

--
-- Table structure for table `productfeatureflag`
--

DROP TABLE IF EXISTS `productfeatureflag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productfeatureflag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productFeatureId` int(11) NOT NULL,
  `featureFlagId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `productFeatureId` (`productFeatureId`),
  KEY `featureFlagId` (`featureFlagId`),
  CONSTRAINT `productfeatureflag_ibfk_1` FOREIGN KEY (`productFeatureId`) REFERENCES `productfeature` (`id`),
  CONSTRAINT `productfeatureflag_ibfk_2` FOREIGN KEY (`featureFlagId`) REFERENCES `featureflag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productfeatureflag`
--

/*!40000 ALTER TABLE `productfeatureflag` DISABLE KEYS */;
/*!40000 ALTER TABLE `productfeatureflag` ENABLE KEYS */;

--
-- Table structure for table `producttrack`
--

DROP TABLE IF EXISTS `producttrack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producttrack` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Ordering` int(10) unsigned NOT NULL DEFAULT '0',
  `msprojectname` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producttrack`
--

/*!40000 ALTER TABLE `producttrack` DISABLE KEYS */;
/*!40000 ALTER TABLE `producttrack` ENABLE KEYS */;

--
-- Table structure for table `producttrackfeature`
--

DROP TABLE IF EXISTS `producttrackfeature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producttrackfeature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productFeatureId` int(11) NOT NULL,
  `productTrackId` int(11) NOT NULL,
  `allocation` decimal(5,2) unsigned NOT NULL,
  `priority` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `productFeatureId` (`productFeatureId`),
  KEY `productTrackId` (`productTrackId`),
  CONSTRAINT `producttrackfeature_ibfk_1` FOREIGN KEY (`productFeatureId`) REFERENCES `productfeature` (`id`),
  CONSTRAINT `producttrackfeature_ibfk_2` FOREIGN KEY (`productTrackId`) REFERENCES `producttrack` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1185 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producttrackfeature`
--

/*!40000 ALTER TABLE `producttrackfeature` DISABLE KEYS */;
/*!40000 ALTER TABLE `producttrackfeature` ENABLE KEYS */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `ExecSummary` text NOT NULL,
  `currentStatusId` int(11) NOT NULL,
  `started` date DEFAULT NULL,
  `finished` date DEFAULT NULL,
  `isActive` bit(1) NOT NULL DEFAULT b'1',
  `productFeatureId` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `Name` (`Name`),
  KEY `currentStatusId` (`currentStatusId`),
  CONSTRAINT `Project_ibfk_1` FOREIGN KEY (`currentStatusId`) REFERENCES `statuscolor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;

--
-- Table structure for table `projectarea`
--

DROP TABLE IF EXISTS `projectarea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectarea` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `areaId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `projectId` (`projectId`),
  KEY `areaId` (`areaId`),
  CONSTRAINT `ProjectArea_ibfk_1` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`),
  CONSTRAINT `ProjectArea_ibfk_2` FOREIGN KEY (`areaId`) REFERENCES `area` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectarea`
--

/*!40000 ALTER TABLE `projectarea` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectarea` ENABLE KEYS */;

--
-- Table structure for table `projectfeature`
--

DROP TABLE IF EXISTS `projectfeature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectfeature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `releaseId` int(11) NOT NULL,
  `parentFeatureId` int(11) DEFAULT NULL,
  `feature` varchar(45) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `timeLine` date DEFAULT NULL,
  `timeLineString` varchar(45) DEFAULT NULL,
  `hidden` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `projectId` (`projectId`),
  KEY `releaseId` (`releaseId`),
  KEY `parentFeatureId` (`parentFeatureId`),
  CONSTRAINT `ProjectFeature_ibfk_1` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`),
  CONSTRAINT `ProjectFeature_ibfk_2` FOREIGN KEY (`releaseId`) REFERENCES `projectrelease` (`id`),
  CONSTRAINT `ProjectFeature_ibfk_3` FOREIGN KEY (`parentFeatureId`) REFERENCES `projectfeature` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectfeature`
--

/*!40000 ALTER TABLE `projectfeature` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectfeature` ENABLE KEYS */;

--
-- Table structure for table `projecthighlight`
--

DROP TABLE IF EXISTS `projecthighlight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projecthighlight` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `updated` date NOT NULL,
  `memo` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `projectId` (`projectId`),
  CONSTRAINT `ProjectHighlight_ibfk_1` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projecthighlight`
--

/*!40000 ALTER TABLE `projecthighlight` DISABLE KEYS */;
/*!40000 ALTER TABLE `projecthighlight` ENABLE KEYS */;

--
-- Table structure for table `projectperson`
--

DROP TABLE IF EXISTS `projectperson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectperson` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `projectPersonTypeId` int(11) NOT NULL,
  `alias` varchar(20) NOT NULL,
  `started` date DEFAULT NULL,
  `finished` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `projectId` (`projectId`),
  KEY `projectPersonTypeId` (`projectPersonTypeId`),
  CONSTRAINT `ProjectPerson_ibfk_1` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`),
  CONSTRAINT `ProjectPerson_ibfk_2` FOREIGN KEY (`projectPersonTypeId`) REFERENCES `projectpersontype` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectperson`
--

/*!40000 ALTER TABLE `projectperson` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectperson` ENABLE KEYS */;

--
-- Table structure for table `projectpersontype`
--

DROP TABLE IF EXISTS `projectpersontype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectpersontype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectpersontype`
--

/*!40000 ALTER TABLE `projectpersontype` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectpersontype` ENABLE KEYS */;

--
-- Table structure for table `projectrelease`
--

DROP TABLE IF EXISTS `projectrelease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectrelease` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Deployed` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectrelease`
--

/*!40000 ALTER TABLE `projectrelease` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectrelease` ENABLE KEYS */;

--
-- Table structure for table `projectrole`
--

DROP TABLE IF EXISTS `projectrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectrole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  `updated` date NOT NULL,
  `headCount` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `projectId` (`projectId`),
  KEY `roleId` (`roleId`),
  CONSTRAINT `ProjectRole_ibfk_1` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`),
  CONSTRAINT `ProjectRole_ibfk_2` FOREIGN KEY (`roleId`) REFERENCES `systemrole` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectrole`
--

/*!40000 ALTER TABLE `projectrole` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectrole` ENABLE KEYS */;

--
-- Table structure for table `resourcepool`
--

DROP TABLE IF EXISTS `resourcepool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcepool` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT '0',
  `poolsize` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcepool`
--

/*!40000 ALTER TABLE `resourcepool` DISABLE KEYS */;
/*!40000 ALTER TABLE `resourcepool` ENABLE KEYS */;

--
-- Table structure for table `resourceteam`
--

DROP TABLE IF EXISTS `resourceteam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourceteam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT '0',
  `teamsize` int(10) unsigned NOT NULL DEFAULT '0',
  `resourcepoolId` int(11) DEFAULT NULL,
  `minimum` int(10) unsigned NOT NULL DEFAULT '0',
  `maximum` int(10) unsigned DEFAULT NULL,
  `msprojectname` varchar(30) NOT NULL DEFAULT '',
  `pplteamname` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`),
  KEY `resourcepoolId` (`resourcepoolId`),
  CONSTRAINT `resourceteam_ibfk_1` FOREIGN KEY (`resourcepoolId`) REFERENCES `resourcepool` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourceteam`
--

/*!40000 ALTER TABLE `resourceteam` DISABLE KEYS */;
/*!40000 ALTER TABLE `resourceteam` ENABLE KEYS */;

--
-- Table structure for table `resourceteamproductfeature`
--

DROP TABLE IF EXISTS `resourceteamproductfeature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourceteamproductfeature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `featureSize` int(10) unsigned NOT NULL DEFAULT '0',
  `maxDevs` int(10) unsigned NOT NULL DEFAULT '0',
  `resourceTeamId` int(11) NOT NULL,
  `productFeatureId` int(11) NOT NULL,
  `featureSizeRemaining` decimal(5,2) DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `resourceTeamId` (`resourceTeamId`),
  KEY `productFeatureId` (`productFeatureId`),
  CONSTRAINT `resourceteamproductfeature_ibfk_1` FOREIGN KEY (`resourceTeamId`) REFERENCES `resourceteam` (`id`),
  CONSTRAINT `resourceteamproductfeature_ibfk_2` FOREIGN KEY (`productFeatureId`) REFERENCES `productfeature` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9019 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourceteamproductfeature`
--

/*!40000 ALTER TABLE `resourceteamproductfeature` DISABLE KEYS */;
/*!40000 ALTER TABLE `resourceteamproductfeature` ENABLE KEYS */;

--
-- Table structure for table `stage`
--

DROP TABLE IF EXISTS `stage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stage`
--

/*!40000 ALTER TABLE `stage` DISABLE KEYS */;
/*!40000 ALTER TABLE `stage` ENABLE KEYS */;

--
-- Table structure for table `statuscolor`
--

DROP TABLE IF EXISTS `statuscolor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statuscolor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `cssClassName` varchar(20) NOT NULL,
  `IsOnTrack` bit(1) DEFAULT b'0',
  `IsComplete` bit(1) DEFAULT b'0',
  `HasIssues` bit(1) DEFAULT b'0',
  `IsAtRisk` bit(1) DEFAULT b'0',
  `IsOnHold` bit(1) DEFAULT b'0',
  `HasNotStarted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statuscolor`
--

/*!40000 ALTER TABLE `statuscolor` DISABLE KEYS */;
/*!40000 ALTER TABLE `statuscolor` ENABLE KEYS */;

--
-- Table structure for table `statusupdate`
--

DROP TABLE IF EXISTS `statusupdate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statusupdate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `statusId` int(11) NOT NULL,
  `updated` date NOT NULL,
  `comments` text,
  `issues` text,
  `risks` text,
  `dependencies` text,
  PRIMARY KEY (`id`),
  KEY `projectId` (`projectId`),
  KEY `statusId` (`statusId`),
  CONSTRAINT `StatusUpdate_ibfk_1` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`),
  CONSTRAINT `StatusUpdate_ibfk_2` FOREIGN KEY (`statusId`) REFERENCES `statuscolor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statusupdate`
--

/*!40000 ALTER TABLE `statusupdate` DISABLE KEYS */;
/*!40000 ALTER TABLE `statusupdate` ENABLE KEYS */;

--
-- Table structure for table `systemrole`
--

DROP TABLE IF EXISTS `systemrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `systemrole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Ordering` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `systemrole`
--

/*!40000 ALTER TABLE `systemrole` DISABLE KEYS */;
/*!40000 ALTER TABLE `systemrole` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-19  8:00:02
