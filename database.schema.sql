-- MySQL dump 10.16  Distrib 10.2.12-MariaDB, for osx10.13 (x86_64)
--
-- Host: 127.0.0.1    Database: offline
-- ------------------------------------------------------
-- Server version	10.3.8-MariaDB-1:10.3.8+maria~bionic

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
  `description` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission` (`permission`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AuthPreference`
--

DROP TABLE IF EXISTS `AuthPreference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AuthPreference` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `code` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AuthRole`
--

DROP TABLE IF EXISTS `AuthRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AuthRole` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` varchar(20) NOT NULL,
  `description` text DEFAULT NULL,
  `isAdmin` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AuthUserPreference`
--

DROP TABLE IF EXISTS `AuthUserPreference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AuthUserPreference` (
  `authPreferenceid` bigint(20) NOT NULL DEFAULT 0,
  `login` varchar(20) NOT NULL,
  PRIMARY KEY (`authPreferenceid`,`login`),
  CONSTRAINT `AuthUserPreference_ibfk_1` FOREIGN KEY (`authPreferenceid`) REFERENCES `AuthPreference` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `IC` tinyint(1) NOT NULL DEFAULT 0,
  `NonExempt` tinyint(1) NOT NULL DEFAULT 0,
  `archetypeID` bigint(20) DEFAULT NULL,
  `scope` text DEFAULT NULL,
  `complexity` text DEFAULT NULL,
  `supervision` text DEFAULT NULL,
  `knowledge` text DEFAULT NULL,
  `yearsOfExperience` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ARCHETYPE_FK` (`archetypeID`),
  CONSTRAINT `ARCHETYPE_FK` FOREIGN KEY (`archetypeID`) REFERENCES `ManagerArchetype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `FunctionalAreaID` bigint(20) DEFAULT NULL,
  `ProfitCenterID` bigint(20) DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CostCenter`),
  KEY `FunctionalAreaID` (`FunctionalAreaID`),
  KEY `ProfitCenterID` (`ProfitCenterID`),
  CONSTRAINT `CostCenter_ibfk_1` FOREIGN KEY (`FunctionalAreaID`) REFERENCES `FunctionalArea` (`functionalArea`),
  CONSTRAINT `CostCenter_ibfk_2` FOREIGN KEY (`ProfitCenterID`) REFERENCES `ProfitCenter` (`profitCenter`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EmpBio`
--

DROP TABLE IF EXISTS `EmpBio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmpBio` (
  `Login` varchar(254) NOT NULL DEFAULT '',
  `bio` text DEFAULT NULL,
  `lastUpdated` date DEFAULT NULL,
  PRIMARY KEY (`Login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EmpEfficiency`
--

DROP TABLE IF EXISTS `EmpEfficiency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmpEfficiency` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `month` int(11) NOT NULL,
  `efficiency` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `month` (`month`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `Duration` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `FunctionalArea`
--

DROP TABLE IF EXISTS `FunctionalArea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FunctionalArea` (
  `functionalArea` bigint(20) NOT NULL,
  `department` varchar(100) DEFAULT NULL,
  `shortname` varchar(100) DEFAULT NULL,
  `PandLCategory` varchar(10) DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`functionalArea`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=1206 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `MatrixTeam`
--

DROP TABLE IF EXISTS `MatrixTeam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MatrixTeam` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(254) NOT NULL,
  `isPE` bit(1) NOT NULL DEFAULT b'0',
  `owner` varchar(254) DEFAULT NULL,
  `parent` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name` (`Name`),
  KEY `PARENT` (`parent`),
  CONSTRAINT `PARENT` FOREIGN KEY (`parent`) REFERENCES `MatrixTeam` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `Description` text DEFAULT NULL,
  `Score` int(11) DEFAULT NULL,
  `completed` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2465 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `retired` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1099 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=838 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ProfitCenter`
--

DROP TABLE IF EXISTS `ProfitCenter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProfitCenter` (
  `profitCenter` bigint(20) NOT NULL,
  `shortname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`profitCenter`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ProjectDependency`
--

DROP TABLE IF EXISTS `ProjectDependency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProjectDependency` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fromProject` int(11) NOT NULL,
  `toProject` int(11) NOT NULL,
  `dependencytype` char(2) NOT NULL DEFAULT 'FS',
  PRIMARY KEY (`id`),
  KEY `fromProject` (`fromProject`),
  KEY `toProject` (`toProject`),
  CONSTRAINT `ProjectDependency_ibfk_1` FOREIGN KEY (`fromProject`) REFERENCES `project` (`id`),
  CONSTRAINT `ProjectDependency_ibfk_2` FOREIGN KEY (`toProject`) REFERENCES `project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1740 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `ReleaseAuthorization`
--

DROP TABLE IF EXISTS `ReleaseAuthorization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReleaseAuthorization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ReleaseId` int(11) NOT NULL,
  `ReleaseAuthorityId` int(11) NOT NULL,
  `login` varchar(20) NOT NULL,
  `approvedDate` datetime DEFAULT NULL,
  `rejectedDate` datetime DEFAULT NULL,
  `isApproved` tinyint(1) DEFAULT NULL,
  `notes` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ReleaseAuth_rel` (`ReleaseId`),
  KEY `ReleaseAuth_type` (`ReleaseAuthorityId`),
  CONSTRAINT `ReleaseAuth_rel` FOREIGN KEY (`ReleaseId`) REFERENCES `projectrelease` (`id`),
  CONSTRAINT `ReleaseAuth_type` FOREIGN KEY (`ReleaseAuthorityId`) REFERENCES `ReleaseAuthorizationType` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ReleaseAuthorizationType`
--

DROP TABLE IF EXISTS `ReleaseAuthorizationType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReleaseAuthorizationType` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ReleaseType`
--

DROP TABLE IF EXISTS `ReleaseType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReleaseType` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `emailAlias` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ReleaseTypeAuthorizationPeople`
--

DROP TABLE IF EXISTS `ReleaseTypeAuthorizationPeople`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReleaseTypeAuthorizationPeople` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ReleaseTypeId` int(11) NOT NULL,
  `ReleaseAuthorityId` int(11) NOT NULL,
  `login` varchar(20) NOT NULL,
  `isPrimary` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ReleaseAuthorizationPPL_fk1` (`ReleaseTypeId`),
  KEY `ReleaseAuthorizationType_fk1` (`ReleaseAuthorityId`),
  CONSTRAINT `ReleaseAuthorizationPPL_fk1` FOREIGN KEY (`ReleaseTypeId`) REFERENCES `ReleaseType` (`id`),
  CONSTRAINT `ReleaseAuthorizationType_fk1` FOREIGN KEY (`ReleaseAuthorityId`) REFERENCES `ReleaseAuthorizationType` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ResourceTeamProject`
--

DROP TABLE IF EXISTS `ResourceTeamProject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ResourceTeamProject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `featureSize` int(10) unsigned NOT NULL DEFAULT 0,
  `maxDevs` int(10) unsigned NOT NULL DEFAULT 0,
  `resourceTeamId` int(11) NOT NULL,
  `projectId` int(11) NOT NULL,
  `featureSizeRemaining` decimal(9,2) DEFAULT 0.00,
  PRIMARY KEY (`id`),
  KEY `resourceTeamId` (`resourceTeamId`),
  KEY `projectFeature` (`projectId`),
  CONSTRAINT `resourceteamprojectfeature_ibfk_1` FOREIGN KEY (`resourceTeamId`) REFERENCES `resourceteam` (`id`),
  CONSTRAINT `resourceteamprojectfeature_ibfk_2` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9161 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RoadmapSlack`
--

DROP TABLE IF EXISTS `RoadmapSlack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RoadmapSlack` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ordering` int(11) NOT NULL,
  `efficiency` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ordering` (`ordering`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `desc` text DEFAULT NULL,
  `managerRatio` double NOT NULL,
  `spanAvg` double NOT NULL,
  `employeeCount` bigint(20) NOT NULL,
  `offshoreCount` bigint(20) NOT NULL,
  `contractorCount` bigint(20) NOT NULL,
  `maxLayers` bigint(20) NOT NULL,
  `isPublic` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `disabled` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `sc_setFK` (`set`),
  CONSTRAINT `sc_setFK` FOREIGN KEY (`set`) REFERENCES `SurveySet` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `disabled` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `sq_catFK` (`category`),
  CONSTRAINT `sq_catFK` FOREIGN KEY (`category`) REFERENCES `SurveyCategory` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `disabled` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `closed` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `si_setFK` (`set`),
  CONSTRAINT `si_setFK` FOREIGN KEY (`set`) REFERENCES `SurveySet` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `awardnominationto`
--

DROP TABLE IF EXISTS `awardnominationto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `awardnominationto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `FromPerson` varchar(254) NOT NULL,
  `ToPerson` varchar(254) NOT NULL,
  `DateAdded` date NOT NULL,
  `Awarded` int(11) NOT NULL,
  `AwardActionBy` varchar(254) DEFAULT NULL,
  `AwardActionOn` date DEFAULT NULL,
  `ManagersFeedback` text DEFAULT NULL,
  `nominationFeedback` text DEFAULT NULL,
  `HRApproved` int(11) NOT NULL,
  `HRActionBy` varchar(254) DEFAULT NULL,
  `HRActionOn` date DEFAULT NULL,
  `Rejected` tinyint(1) NOT NULL,
  `RejectedBy` varchar(254) DEFAULT NULL,
  `RejectedOn` date DEFAULT NULL,
  `RejectedReason` varchar(254) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ToPerson` (`ToPerson`),
  KEY `FromPerson` (`FromPerson`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `featureflag`
--

DROP TABLE IF EXISTS `featureflag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `featureflag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `allocation` decimal(5,2) unsigned NOT NULL DEFAULT 1.00,
  PRIMARY KEY (`id`),
  KEY `productFeatureId` (`productFeatureId`),
  KEY `managedClientId` (`managedClientId`),
  CONSTRAINT `ManagedClientProductFeature_ibfk_1` FOREIGN KEY (`productFeatureId`) REFERENCES `productfeature` (`id`),
  CONSTRAINT `ManagedClientProductFeature_ibfk_2` FOREIGN KEY (`managedClientId`) REFERENCES `managedclient` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `play_evolutions`
--

DROP TABLE IF EXISTS `play_evolutions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `play_evolutions` (
  `id` int(11) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `applied_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `apply_script` text DEFAULT NULL,
  `revert_script` text DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `last_problem` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `ordering` int(10) unsigned NOT NULL DEFAULT 0,
  `stageId` int(11) NOT NULL DEFAULT 1,
  `msprojectname` varchar(200) DEFAULT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `isCID` bit(1) DEFAULT b'0',
  `isAnchor` bit(1) DEFAULT b'0',
  `msPriority` int(11) DEFAULT 0,
  `start` date DEFAULT NULL,
  `finish` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=735 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `producttrack`
--

DROP TABLE IF EXISTS `producttrack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producttrack` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Ordering` int(10) unsigned NOT NULL DEFAULT 0,
  `msprojectname` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=416 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `ExecSummary` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `currentStatusId` int(11) NOT NULL,
  `started` date DEFAULT NULL,
  `finished` date DEFAULT NULL,
  `isActive` bit(1) NOT NULL DEFAULT b'1',
  `productFeatureId` int(11) NOT NULL DEFAULT 1,
  `msprojectname` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `constraintType` char(4) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'ASAP',
  `constraintDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Name` (`Name`),
  KEY `currentStatusId` (`currentStatusId`),
  CONSTRAINT `Project_ibfk_1` FOREIGN KEY (`currentStatusId`) REFERENCES `statuscolor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=760 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `projectpersontype`
--

DROP TABLE IF EXISTS `projectpersontype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectpersontype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `projectrelease`
--

DROP TABLE IF EXISTS `projectrelease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectrelease` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Deployed` datetime DEFAULT NULL,
  `Planned` datetime DEFAULT NULL,
  `ReleaseTypeId` int(11) NOT NULL,
  `changeticket` varchar(50) DEFAULT NULL,
  `releasefileURL` varchar(255) DEFAULT NULL,
  `releasenotes` text DEFAULT NULL,
  `RolledBack` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ReleaseTypeId` (`ReleaseTypeId`),
  CONSTRAINT `projectrelease_ibfk_1` FOREIGN KEY (`ReleaseTypeId`) REFERENCES `ReleaseType` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `resourcepool`
--

DROP TABLE IF EXISTS `resourcepool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcepool` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT 0,
  `poolsize` int(10) unsigned NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resourceteam`
--

DROP TABLE IF EXISTS `resourceteam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourceteam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT 0,
  `teamsize` int(10) unsigned NOT NULL DEFAULT 0,
  `resourcepoolId` int(11) DEFAULT NULL,
  `minimum` int(10) unsigned NOT NULL DEFAULT 0,
  `maximum` int(10) unsigned DEFAULT NULL,
  `msprojectname` varchar(30) NOT NULL DEFAULT '',
  `pplteamname` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`),
  KEY `resourcepoolId` (`resourcepoolId`),
  CONSTRAINT `resourceteam_ibfk_1` FOREIGN KEY (`resourcepoolId`) REFERENCES `resourcepool` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resourceteamproductfeature`
--

DROP TABLE IF EXISTS `resourceteamproductfeature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourceteamproductfeature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `featureSize` int(10) unsigned NOT NULL DEFAULT 0,
  `maxDevs` int(10) unsigned NOT NULL DEFAULT 0,
  `resourceTeamId` int(11) NOT NULL,
  `productFeatureId` int(11) NOT NULL,
  `featureSizeRemaining` decimal(9,2) DEFAULT 0.00,
  PRIMARY KEY (`id`),
  KEY `resourceTeamId` (`resourceTeamId`),
  KEY `productFeatureId` (`productFeatureId`),
  CONSTRAINT `resourceteamproductfeature_ibfk_1` FOREIGN KEY (`resourceTeamId`) REFERENCES `resourceteam` (`id`),
  CONSTRAINT `resourceteamproductfeature_ibfk_2` FOREIGN KEY (`productFeatureId`) REFERENCES `productfeature` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8652 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stage`
--

DROP TABLE IF EXISTS `stage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ordering` int(10) unsigned NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `comments` text DEFAULT NULL,
  `issues` text DEFAULT NULL,
  `risks` text DEFAULT NULL,
  `dependencies` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `projectId` (`projectId`),
  KEY `statusId` (`statusId`),
  CONSTRAINT `StatusUpdate_ibfk_1` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`),
  CONSTRAINT `StatusUpdate_ibfk_2` FOREIGN KEY (`statusId`) REFERENCES `statuscolor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `systemrole`
--

DROP TABLE IF EXISTS `systemrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `systemrole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Ordering` int(10) unsigned NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-07 10:54:11
