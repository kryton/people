drop table aceawardnominationto;
CREATE TABLE `awardnominationto` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `FromPerson` VARCHAR(254) NOT NULL,
  `ToPerson` VARCHAR(254) NOT NULL,
  `DateAdded` DATE NOT NULL,
  `Awarded` INT NOT NULL,
  `AwardActionBy` VARCHAR(254) NULL DEFAULT NULL,
  `AwardActionOn` DATE NULL DEFAULT NULL,
  `ManagersFeedback` TEXT NULL,
  `nominationFeedback` TEXT NULL,
  `HRApproved` INT NOT NULL,
  `HRActionBy` VARCHAR(254) NULL DEFAULT NULL,
  `HRActionOn` DATE NULL DEFAULT NULL,
  `Rejected` TINYINT(1) NOT NULL,
  `RejectedBy` VARCHAR(254) NULL DEFAULT NULL,
  `RejectedOn` DATE NULL DEFAULT NULL,
  `RejectedReason` VARCHAR(254) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `ToPerson` (`ToPerson`),
  INDEX `FromPerson` (`FromPerson`)
)
  COLLATE='utf8_general_ci'
  ENGINE=InnoDB
;
