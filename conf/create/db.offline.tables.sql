-- MySQL dump 10.16  Distrib 10.1.22-MariaDB, for Win64 (AMD64)
--
-- Host: currentdbprd031001.c031.digitalriverws.net    Database: offline
-- ------------------------------------------------------
-- Server version	5.6.21-log

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
-- Table structure for table `AuthPermission`
--

DROP TABLE IF EXISTS `AuthPermission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AuthPermission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission` (`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuthPermission`
--

LOCK TABLES `AuthPermission` WRITE;
/*!40000 ALTER TABLE `AuthPermission` DISABLE KEYS */;
/*!40000 ALTER TABLE `AuthPermission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AuthRole`
--

DROP TABLE IF EXISTS `AuthRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AuthRole` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuthRole`
--

LOCK TABLES `AuthRole` WRITE;
/*!40000 ALTER TABLE `AuthRole` DISABLE KEYS */;
/*!40000 ALTER TABLE `AuthRole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AuthRolePermission`
--

DROP TABLE IF EXISTS `AuthRolePermission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AuthRolePermission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permissionID` bigint(20) NOT NULL,
  `roleID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `permission` (`permissionID`),
  KEY `role` (`roleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuthRolePermission`
--

LOCK TABLES `AuthRolePermission` WRITE;
/*!40000 ALTER TABLE `AuthRolePermission` DISABLE KEYS */;
/*!40000 ALTER TABLE `AuthRolePermission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AuthUser`
--

DROP TABLE IF EXISTS `AuthUser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AuthUser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `roleID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user` (`username`),
  KEY `role` (`roleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuthUser`
--

LOCK TABLES `AuthUser` WRITE;
/*!40000 ALTER TABLE `AuthUser` DISABLE KEYS */;
/*!40000 ALTER TABLE `AuthUser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BusinessUnit`
--

DROP TABLE IF EXISTS `BusinessUnit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BusinessUnit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(254) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BusinessUnit`
--

LOCK TABLES `BusinessUnit` WRITE;
/*!40000 ALTER TABLE `BusinessUnit` DISABLE KEYS */;
/*!40000 ALTER TABLE `BusinessUnit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CorpLevel`
--

DROP TABLE IF EXISTS `CorpLevel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CorpLevel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level` bigint(20) NOT NULL,
  `name` varchar(254) NOT NULL,
  `admin` tinyint(1) NOT NULL,
  `IC` tinyint(1) NOT NULL DEFAULT '0',
  `NonExempt` tinyint(1) NOT NULL DEFAULT '0',
  `archetypeID` bigint(20) DEFAULT NULL,
  `scope` text,
  `complexity` text,
  `supervision` text,
  `knowledge` text,
  `yearsOfExperience` text,
  PRIMARY KEY (`id`),
  KEY `ARCHETYPE_FK` (`archetypeID`),
  CONSTRAINT `ARCHETYPE_FK` FOREIGN KEY (`archetypeID`) REFERENCES `ManagerArchetype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CorpLevel`
--

LOCK TABLES `CorpLevel` WRITE;
/*!40000 ALTER TABLE `CorpLevel` DISABLE KEYS */;
/*!40000 ALTER TABLE `CorpLevel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CostCenter`
--

DROP TABLE IF EXISTS `CostCenter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CostCenter` (
  `CostCenter` bigint(20) NOT NULL,
  `CostCenterText` varchar(254) DEFAULT NULL,
  `account` varchar(254) DEFAULT NULL,
  `functionalArea` varchar(254) DEFAULT NULL,
  `businessUnit` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`CostCenter`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CostCenter`
--

LOCK TABLES `CostCenter` WRITE;
/*!40000 ALTER TABLE `CostCenter` DISABLE KEYS */;
/*!40000 ALTER TABLE `CostCenter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EmpBio`
--

DROP TABLE IF EXISTS `EmpBio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmpBio` (
  `Login` varchar(254) NOT NULL DEFAULT '',
  `bio` text,
  `lastUpdated` date DEFAULT NULL,
  PRIMARY KEY (`Login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EmpBio`
--

LOCK TABLES `EmpBio` WRITE;
/*!40000 ALTER TABLE `EmpBio` DISABLE KEYS */;
/*!40000 ALTER TABLE `EmpBio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EmpHistory`
--

DROP TABLE IF EXISTS `EmpHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmpHistory` (
  `PersonNumber` bigint(20) NOT NULL,
  `Login` varchar(254) DEFAULT NULL,
  `Firstname` varchar(254) NOT NULL,
  `NickName` varchar(254) DEFAULT NULL,
  `LastName` varchar(254) NOT NULL,
  `ManagerID` varchar(254) DEFAULT NULL,
  `CostCenter` bigint(20) NOT NULL,
  `OfficeID` bigint(20) NOT NULL,
  `EmployeeType` varchar(254) DEFAULT NULL,
  `HireRehireDate` date DEFAULT NULL,
  `LastSeen` date DEFAULT NULL,
  UNIQUE KEY `EmpHistory_login` (`Login`),
  KEY `EmpHistory_Office_FK` (`OfficeID`),
  CONSTRAINT `EmpHistory_Office_FK` FOREIGN KEY (`OfficeID`) REFERENCES `Office` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EmpHistory`
--

LOCK TABLES `EmpHistory` WRITE;
/*!40000 ALTER TABLE `EmpHistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `EmpHistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EmpPayroll`
--

DROP TABLE IF EXISTS `EmpPayroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmpPayroll` (
  `Login` varchar(255) NOT NULL,
  `crypted` text NOT NULL,
  PRIMARY KEY (`Login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EmpPayroll`
--

LOCK TABLES `EmpPayroll` WRITE;
/*!40000 ALTER TABLE `EmpPayroll` DISABLE KEYS */;
/*!40000 ALTER TABLE `EmpPayroll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EmpRelations`
--

DROP TABLE IF EXISTS `EmpRelations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmpRelations` (
  `PersonNumber` bigint(20) NOT NULL,
  `Login` varchar(254) NOT NULL DEFAULT '',
  `Firstname` varchar(254) NOT NULL,
  `NickName` varchar(254) DEFAULT NULL,
  `LastName` varchar(254) NOT NULL,
  `ManagerID` varchar(254) DEFAULT NULL,
  `Directs` bigint(20) NOT NULL,
  `Reports` bigint(20) NOT NULL,
  `ReportsContractor` bigint(20) NOT NULL,
  `CompanyCode` int(11) NOT NULL,
  `CompanyCodeName` varchar(254) NOT NULL,
  `CostCenter` bigint(20) NOT NULL,
  `PersonalArea` varchar(254) NOT NULL,
  `PersonalSubArea` varchar(254) NOT NULL,
  `EmployeeGroup` varchar(254) NOT NULL,
  `Position` varchar(254) NOT NULL,
  `Agency` varchar(254) NOT NULL,
  `ExecutiveName` varchar(254) DEFAULT NULL,
  `OfficeLocation` varchar(254) DEFAULT NULL,
  `OfficeLocation2` varchar(254) DEFAULT NULL,
  `OfficeID` bigint(20) DEFAULT NULL,
  `EmployeeType` varchar(254) DEFAULT NULL,
  PRIMARY KEY (`Login`),
  KEY `EMPOFFICE_FK` (`OfficeID`),
  KEY `COSTCENTER_FK` (`CostCenter`),
  KEY `EmpCostCenter` (`CostCenter`),
  CONSTRAINT `CostCenter_FK` FOREIGN KEY (`CostCenter`) REFERENCES `CostCenter` (`CostCenter`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `EMPOFFICE_FK` FOREIGN KEY (`OfficeID`) REFERENCES `Office` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EmpRelations`
--

LOCK TABLES `EmpRelations` WRITE;
/*!40000 ALTER TABLE `EmpRelations` DISABLE KEYS */;
/*!40000 ALTER TABLE `EmpRelations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EmpTag`
--

DROP TABLE IF EXISTS `EmpTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmpTag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Login` varchar(254) NOT NULL,
  `DateAdded` date NOT NULL,
  `TagText` varchar(254) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `EMP_TAGTEXT` (`TagText`),
  KEY `EMP_Login` (`Login`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EmpTag`
--

LOCK TABLES `EmpTag` WRITE;
/*!40000 ALTER TABLE `EmpTag` DISABLE KEYS */;
/*!40000 ALTER TABLE `EmpTag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EmployeeMilestone`
--

DROP TABLE IF EXISTS `EmployeeMilestone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmployeeMilestone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `MilestoneID` bigint(20) NOT NULL,
  `login` varchar(20) NOT NULL,
  `CompletedOn` date NOT NULL,
  `EnteredOn` date NOT NULL,
  `Comments` text NOT NULL,
  `Duration` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EmployeeMilestone`
--

LOCK TABLES `EmployeeMilestone` WRITE;
/*!40000 ALTER TABLE `EmployeeMilestone` DISABLE KEYS */;
/*!40000 ALTER TABLE `EmployeeMilestone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EmployeeRoster`
--

DROP TABLE IF EXISTS `EmployeeRoster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmployeeRoster` (
  `login` varchar(254) NOT NULL,
  `corpLevelID` bigint(20) NOT NULL,
  PRIMARY KEY (`login`),
  KEY `EMPROSTER_FK` (`corpLevelID`),
  CONSTRAINT `EMPROSTER_FK` FOREIGN KEY (`corpLevelID`) REFERENCES `CorpLevel` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EmployeeRoster`
--

LOCK TABLES `EmployeeRoster` WRITE;
/*!40000 ALTER TABLE `EmployeeRoster` DISABLE KEYS */;
/*!40000 ALTER TABLE `EmployeeRoster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GitCommit`
--

DROP TABLE IF EXISTS `GitCommit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GitCommit` (
  `sha` varchar(30) NOT NULL,
  `message` varchar(254) NOT NULL,
  `committer` varchar(20) DEFAULT NULL,
  `author` varchar(20) DEFAULT NULL,
  `commitDate` date DEFAULT NULL,
  `authorDate` date DEFAULT NULL,
  PRIMARY KEY (`sha`),
  KEY `committer` (`committer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GitCommit`
--

LOCK TABLES `GitCommit` WRITE;
/*!40000 ALTER TABLE `GitCommit` DISABLE KEYS */;
/*!40000 ALTER TABLE `GitCommit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GitIssue`
--

DROP TABLE IF EXISTS `GitIssue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GitIssue` (
  `id` bigint(20) NOT NULL,
  `issueNumber` bigint(20) NOT NULL,
  `user` varchar(20) NOT NULL,
  `title` varchar(254) NOT NULL,
  `state` varchar(20) NOT NULL,
  `assignee` varchar(20) DEFAULT NULL,
  `createdAt` date DEFAULT NULL,
  `updatedAt` date DEFAULT NULL,
  `closedAt` date DEFAULT NULL,
  `pullURL` varchar(254) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GitIssue`
--

LOCK TABLES `GitIssue` WRITE;
/*!40000 ALTER TABLE `GitIssue` DISABLE KEYS */;
/*!40000 ALTER TABLE `GitIssue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IndividualArchetype`
--

DROP TABLE IF EXISTS `IndividualArchetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IndividualArchetype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(254) NOT NULL,
  `archetypeID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ARCHETYPEI_FK` (`archetypeID`),
  CONSTRAINT `ARCHETYPEI_FK` FOREIGN KEY (`archetypeID`) REFERENCES `ManagerArchetype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IndividualArchetype`
--

LOCK TABLES `IndividualArchetype` WRITE;
/*!40000 ALTER TABLE `IndividualArchetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `IndividualArchetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IndividualBusinessUnit`
--

DROP TABLE IF EXISTS `IndividualBusinessUnit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IndividualBusinessUnit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(254) NOT NULL,
  `businessUnitID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `BUSUNIT_FK` (`businessUnitID`),
  CONSTRAINT `BUSUNIT_FK` FOREIGN KEY (`businessUnitID`) REFERENCES `BusinessUnit` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IndividualBusinessUnit`
--

LOCK TABLES `IndividualBusinessUnit` WRITE;
/*!40000 ALTER TABLE `IndividualBusinessUnit` DISABLE KEYS */;
/*!40000 ALTER TABLE `IndividualBusinessUnit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JiraIssue`
--

DROP TABLE IF EXISTS `JiraIssue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JiraIssue` (
  `id` varchar(20) NOT NULL,
  `self` varchar(2540) NOT NULL,
  `jirakey` varchar(20) NOT NULL,
  `issueType` varchar(20) NOT NULL,
  `summary` text NOT NULL,
  `parent` varchar(20) DEFAULT NULL,
  `timeSpent` int(11) DEFAULT NULL,
  `timeEstimate` int(11) DEFAULT NULL,
  `progress` int(11) DEFAULT NULL,
  `progressTotal` int(11) DEFAULT NULL,
  `progressPercent` int(11) DEFAULT NULL,
  `assignee` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `projectName` varchar(100) DEFAULT NULL,
  `createdAt` date NOT NULL,
  `updatedAt` date DEFAULT NULL,
  `resolvedAt` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `jiraKey` (`jirakey`),
  KEY `parent` (`parent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JiraIssue`
--

LOCK TABLES `JiraIssue` WRITE;
/*!40000 ALTER TABLE `JiraIssue` DISABLE KEYS */;
/*!40000 ALTER TABLE `JiraIssue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JiraParentIssue`
--

DROP TABLE IF EXISTS `JiraParentIssue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JiraParentIssue` (
  `id` varchar(20) NOT NULL,
  `jirakey` varchar(20) NOT NULL,
  `self` varchar(254) NOT NULL,
  `summary` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `jira` (`jirakey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JiraParentIssue`
--

LOCK TABLES `JiraParentIssue` WRITE;
/*!40000 ALTER TABLE `JiraParentIssue` DISABLE KEYS */;
/*!40000 ALTER TABLE `JiraParentIssue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KudosTo`
--

DROP TABLE IF EXISTS `KudosTo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KudosTo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `FromPerson` varchar(254) NOT NULL,
  `ToPerson` varchar(254) NOT NULL,
  `DateAdded` date NOT NULL,
  `Feedback` varchar(254) NOT NULL,
  `Rejected` tinyint(1) NOT NULL,
  `RejectedBy` varchar(254) DEFAULT NULL,
  `RejectedOn` date DEFAULT NULL,
  `RejectedReason` varchar(254) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=834 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KudosTo`
--

LOCK TABLES `KudosTo` WRITE;
/*!40000 ALTER TABLE `KudosTo` DISABLE KEYS */;
/*!40000 ALTER TABLE `KudosTo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ManagerArchetype`
--

DROP TABLE IF EXISTS `ManagerArchetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ManagerArchetype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(254) NOT NULL,
  `minSpan` bigint(20) NOT NULL,
  `maxSpan` bigint(20) DEFAULT NULL,
  `maturity` varchar(254) DEFAULT NULL,
  `timeSpent` varchar(254) DEFAULT NULL,
  `taskRepeatability` varchar(254) DEFAULT NULL,
  `subordinatesSkills` varchar(254) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ManagerArchetype`
--

LOCK TABLES `ManagerArchetype` WRITE;
/*!40000 ALTER TABLE `ManagerArchetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `ManagerArchetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MatrixTeam`
--

DROP TABLE IF EXISTS `MatrixTeam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MatrixTeam` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(254) NOT NULL,
  `isPE` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MatrixTeam`
--

LOCK TABLES `MatrixTeam` WRITE;
/*!40000 ALTER TABLE `MatrixTeam` DISABLE KEYS */;
/*!40000 ALTER TABLE `MatrixTeam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MatrixTeamMember`
--

DROP TABLE IF EXISTS `MatrixTeamMember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MatrixTeamMember` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `matrixTeamMemberId` bigint(20) NOT NULL,
  `Login` varchar(254) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `matrixTeamMember_FK` (`matrixTeamMemberId`),
  CONSTRAINT `matrixTeamMember_FK` FOREIGN KEY (`matrixTeamMemberId`) REFERENCES `MatrixTeam` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MatrixTeamMember`
--

LOCK TABLES `MatrixTeamMember` WRITE;
/*!40000 ALTER TABLE `MatrixTeamMember` DISABLE KEYS */;
/*!40000 ALTER TABLE `MatrixTeamMember` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Milestone`
--

DROP TABLE IF EXISTS `Milestone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Milestone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Order` int(11) NOT NULL,
  `ShortText` varchar(10) NOT NULL,
  `LongText` text NOT NULL,
  `IsAutomated` bit(1) NOT NULL,
  `StarLevel` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ShortText` (`ShortText`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Milestone`
--

LOCK TABLES `Milestone` WRITE;
/*!40000 ALTER TABLE `Milestone` DISABLE KEYS */;
/*!40000 ALTER TABLE `Milestone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OKRKeyResult`
--

DROP TABLE IF EXISTS `OKRKeyResult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OKRKeyResult` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `objectiveId` bigint(20) NOT NULL,
  `DateAdded` date NOT NULL,
  `Objective` mediumtext NOT NULL,
  `Description` text,
  `Score` int(11) DEFAULT NULL,
  `completed` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2182 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OKRKeyResult`
--

LOCK TABLES `OKRKeyResult` WRITE;
/*!40000 ALTER TABLE `OKRKeyResult` DISABLE KEYS */;
/*!40000 ALTER TABLE `OKRKeyResult` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OKRObjective`
--

DROP TABLE IF EXISTS `OKRObjective`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OKRObjective` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Login` varchar(254) NOT NULL,
  `DateAdded` date NOT NULL,
  `Objective` mediumtext NOT NULL,
  `QuarterDate` date DEFAULT NULL,
  `Score` int(11) DEFAULT NULL,
  `completed` tinyint(1) NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=979 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OKRObjective`
--

LOCK TABLES `OKRObjective` WRITE;
/*!40000 ALTER TABLE `OKRObjective` DISABLE KEYS */;
/*!40000 ALTER TABLE `OKRObjective` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Office`
--

DROP TABLE IF EXISTS `Office`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Office` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(254) DEFAULT NULL,
  `street` varchar(254) DEFAULT NULL,
  `POBox` varchar(254) DEFAULT NULL,
  `region` varchar(254) DEFAULT NULL,
  `zipCode` varchar(254) DEFAULT NULL,
  `country` varchar(254) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Office`
--

LOCK TABLES `Office` WRITE;
/*!40000 ALTER TABLE `Office` DISABLE KEYS */;
/*!40000 ALTER TABLE `Office` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PositionType`
--

DROP TABLE IF EXISTS `PositionType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PositionType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `position` varchar(254) NOT NULL,
  `positiontype` varchar(20) NOT NULL DEFAULT 'UNKNOWN',
  PRIMARY KEY (`id`),
  UNIQUE KEY `position` (`position`)
) ENGINE=InnoDB AUTO_INCREMENT=627 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PositionType`
--

LOCK TABLES `PositionType` WRITE;
/*!40000 ALTER TABLE `PositionType` DISABLE KEYS */;
/*!40000 ALTER TABLE `PositionType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RateCard`
--

DROP TABLE IF EXISTS `RateCard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RateCard` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CompanyName` varchar(254) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RateCard`
--

LOCK TABLES `RateCard` WRITE;
/*!40000 ALTER TABLE `RateCard` DISABLE KEYS */;
/*!40000 ALTER TABLE `RateCard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RateCardRate`
--

DROP TABLE IF EXISTS `RateCardRate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RateCardRate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleID` bigint(20) NOT NULL,
  `country` varchar(254) NOT NULL,
  `monthlyRate` double NOT NULL,
  `hourlyRate` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ROLE_FK` (`roleID`),
  CONSTRAINT `ROLE_FK` FOREIGN KEY (`roleID`) REFERENCES `RateCardRole` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RateCardRate`
--

LOCK TABLES `RateCardRate` WRITE;
/*!40000 ALTER TABLE `RateCardRate` DISABLE KEYS */;
/*!40000 ALTER TABLE `RateCardRate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RateCardRole`
--

DROP TABLE IF EXISTS `RateCardRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RateCardRole` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rateCardID` bigint(20) NOT NULL,
  `level` varchar(254) NOT NULL,
  `corpLevelID` bigint(20) DEFAULT NULL,
  `yearsMin` bigint(20) NOT NULL,
  `yearsMax` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `RATE_FK` (`rateCardID`),
  KEY `CORPLEVLRC_FK` (`corpLevelID`),
  CONSTRAINT `CORPLEVLRC_FK` FOREIGN KEY (`corpLevelID`) REFERENCES `CorpLevel` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `RATE_FK` FOREIGN KEY (`rateCardID`) REFERENCES `RateCard` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RateCardRole`
--

LOCK TABLES `RateCardRole` WRITE;
/*!40000 ALTER TABLE `RateCardRole` DISABLE KEYS */;
/*!40000 ALTER TABLE `RateCardRole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ResourcePool`
--

DROP TABLE IF EXISTS `ResourcePool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ResourcePool` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `poolName` varchar(254) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `poolName` (`poolName`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ResourcePool`
--

LOCK TABLES `ResourcePool` WRITE;
/*!40000 ALTER TABLE `ResourcePool` DISABLE KEYS */;
/*!40000 ALTER TABLE `ResourcePool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ResourcePoolTeam`
--

DROP TABLE IF EXISTS `ResourcePoolTeam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ResourcePoolTeam` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `poolId` bigint(20) NOT NULL,
  `teamDescription` varchar(254) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `teamDescription` (`teamDescription`),
  KEY `poolId` (`poolId`),
  CONSTRAINT `ResourcePoolTeam_ibfk_1` FOREIGN KEY (`poolId`) REFERENCES `ResourcePool` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ResourcePoolTeam`
--

LOCK TABLES `ResourcePoolTeam` WRITE;
/*!40000 ALTER TABLE `ResourcePoolTeam` DISABLE KEYS */;
/*!40000 ALTER TABLE `ResourcePoolTeam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Scenario`
--

DROP TABLE IF EXISTS `Scenario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Scenario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(254) NOT NULL,
  `parent` bigint(20) DEFAULT NULL,
  `name` varchar(254) NOT NULL,
  `desc` text,
  `managerRatio` double NOT NULL,
  `spanAvg` double NOT NULL,
  `employeeCount` bigint(20) NOT NULL,
  `offshoreCount` bigint(20) NOT NULL,
  `contractorCount` bigint(20) NOT NULL,
  `maxLayers` bigint(20) NOT NULL,
  `isPublic` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Scenario`
--

LOCK TABLES `Scenario` WRITE;
/*!40000 ALTER TABLE `Scenario` DISABLE KEYS */;
/*!40000 ALTER TABLE `Scenario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ScenarioDetail`
--

DROP TABLE IF EXISTS `ScenarioDetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScenarioDetail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scenario` bigint(20) NOT NULL,
  `login` varchar(254) NOT NULL,
  `managerId` varchar(254) DEFAULT NULL,
  `isCurrent` tinyint(1) NOT NULL,
  `isContractor` tinyint(1) NOT NULL,
  `isOffshore` tinyint(1) NOT NULL,
  `staffNumber` bigint(20) NOT NULL,
  `corpLevel` bigint(20) DEFAULT NULL,
  `businessUnit` bigint(20) DEFAULT NULL,
  `archeTypeID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ScenarioDetail`
--

LOCK TABLES `ScenarioDetail` WRITE;
/*!40000 ALTER TABLE `ScenarioDetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `ScenarioDetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ScenarioLevel`
--

DROP TABLE IF EXISTS `ScenarioLevel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScenarioLevel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scenario` bigint(20) NOT NULL,
  `layer` bigint(20) NOT NULL,
  `managerCount` bigint(20) NOT NULL,
  `employeeCount` bigint(20) NOT NULL,
  `offshoreCount` bigint(20) NOT NULL,
  `contractorCount` bigint(20) NOT NULL,
  `spanPerLayerAvg` double NOT NULL,
  `spanPerLayerMin` double NOT NULL,
  `spanPerLayerMax` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ScenarioLevel`
--

LOCK TABLES `ScenarioLevel` WRITE;
/*!40000 ALTER TABLE `ScenarioLevel` DISABLE KEYS */;
/*!40000 ALTER TABLE `ScenarioLevel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SurveyAnswer`
--

DROP TABLE IF EXISTS `SurveyAnswer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SurveyAnswer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `set` bigint(20) NOT NULL,
  `setInstancePerson` bigint(20) NOT NULL,
  `question` bigint(20) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sa_quFK` (`question`),
  KEY `sa_setFK` (`set`),
  KEY `sa_siFK` (`setInstancePerson`),
  CONSTRAINT `sa_quFK` FOREIGN KEY (`question`) REFERENCES `SurveyQuestion` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sa_setFK` FOREIGN KEY (`set`) REFERENCES `SurveySet` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sa_siFK` FOREIGN KEY (`setInstancePerson`) REFERENCES `SurveySetPerson` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SurveyAnswer`
--

LOCK TABLES `SurveyAnswer` WRITE;
/*!40000 ALTER TABLE `SurveyAnswer` DISABLE KEYS */;
/*!40000 ALTER TABLE `SurveyAnswer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SurveyCategory`
--

DROP TABLE IF EXISTS `SurveyCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SurveyCategory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `set` bigint(20) NOT NULL,
  `name` varchar(254) NOT NULL,
  `description` text NOT NULL,
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sc_setFK` (`set`),
  CONSTRAINT `sc_setFK` FOREIGN KEY (`set`) REFERENCES `SurveySet` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SurveyCategory`
--

LOCK TABLES `SurveyCategory` WRITE;
/*!40000 ALTER TABLE `SurveyCategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `SurveyCategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SurveyQuestion`
--

DROP TABLE IF EXISTS `SurveyQuestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SurveyQuestion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` bigint(20) NOT NULL,
  `name` varchar(254) NOT NULL,
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sq_catFK` (`category`),
  CONSTRAINT `sq_catFK` FOREIGN KEY (`category`) REFERENCES `SurveyCategory` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SurveyQuestion`
--

LOCK TABLES `SurveyQuestion` WRITE;
/*!40000 ALTER TABLE `SurveyQuestion` DISABLE KEYS */;
/*!40000 ALTER TABLE `SurveyQuestion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SurveySet`
--

DROP TABLE IF EXISTS `SurveySet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SurveySet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(254) NOT NULL,
  `description` text NOT NULL,
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SurveySet`
--

LOCK TABLES `SurveySet` WRITE;
/*!40000 ALTER TABLE `SurveySet` DISABLE KEYS */;
/*!40000 ALTER TABLE `SurveySet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SurveySetInstance`
--

DROP TABLE IF EXISTS `SurveySetInstance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SurveySetInstance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `set` bigint(20) NOT NULL,
  `forPerson` varchar(254) NOT NULL,
  `askedOn` date NOT NULL,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `si_setFK` (`set`),
  CONSTRAINT `si_setFK` FOREIGN KEY (`set`) REFERENCES `SurveySet` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SurveySetInstance`
--

LOCK TABLES `SurveySetInstance` WRITE;
/*!40000 ALTER TABLE `SurveySetInstance` DISABLE KEYS */;
/*!40000 ALTER TABLE `SurveySetInstance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SurveySetPerson`
--

DROP TABLE IF EXISTS `SurveySetPerson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SurveySetPerson` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `personRole` bigint(20) NOT NULL,
  `setInstance` bigint(20) NOT NULL,
  `answeredOn` date NOT NULL,
  `fromPersonANON` varchar(254) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ssp_siFK` (`setInstance`),
  CONSTRAINT `ssp_siFK` FOREIGN KEY (`setInstance`) REFERENCES `SurveySetInstance` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SurveySetPerson`
--

LOCK TABLES `SurveySetPerson` WRITE;
/*!40000 ALTER TABLE `SurveySetPerson` DISABLE KEYS */;
/*!40000 ALTER TABLE `SurveySetPerson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TeamDescription`
--

DROP TABLE IF EXISTS `TeamDescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TeamDescription` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Login` varchar(254) NOT NULL,
  `DateAdded` date NOT NULL,
  `TagText` varchar(254) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `TD_Login` (`Login`)
) ENGINE=InnoDB AUTO_INCREMENT=167 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TeamDescription`
--

LOCK TABLES `TeamDescription` WRITE;
/*!40000 ALTER TABLE `TeamDescription` DISABLE KEYS */;
/*!40000 ALTER TABLE `TeamDescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aceawardnominationto`
--

DROP TABLE IF EXISTS `aceawardnominationto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aceawardnominationto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `FromPerson` varchar(254) NOT NULL,
  `ToPerson` varchar(254) NOT NULL,
  `DateAdded` date NOT NULL,
  `ManagersFeedback` text,
  `valueRespect` text,
  `valueIntegrity` text,
  `valueCustomerCentricity` text,
  `valueInnovation` text,
  `valueGlobalPerspective` text,
  `valueAccountability` text,
  `valueOther` text,
  `Rejected` tinyint(1) NOT NULL,
  `RejectedBy` varchar(254) DEFAULT NULL,
  `RejectedOn` date DEFAULT NULL,
  `RejectedReason` varchar(254) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ToPerson` (`ToPerson`),
  KEY `FromPerson` (`FromPerson`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aceawardnominationto`
--

LOCK TABLES `aceawardnominationto` WRITE;
/*!40000 ALTER TABLE `aceawardnominationto` DISABLE KEYS */;
/*!40000 ALTER TABLE `aceawardnominationto` ENABLE KEYS */;
UNLOCK TABLES;

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
  `apply_script` mediumtext,
  `revert_script` mediumtext,
  `state` varchar(255) DEFAULT NULL,
  `last_problem` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `play_evolutions`
--

LOCK TABLES `play_evolutions` WRITE;
/*!40000 ALTER TABLE `play_evolutions` DISABLE KEYS */;
/*!40000 ALTER TABLE `play_evolutions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-19  7:57:30
