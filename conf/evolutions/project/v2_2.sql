create table ProjectDependency (
  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  fromProject INT not null,
  toProject INT not NULL

) engine = innodb default CHARSET=utf8;

Alter table ProjectDependency add FOREIGN KEY  (fromProject) REFERENCES project(id);
Alter table ProjectDependency add FOREIGN KEY  (toProject) REFERENCES project(id);
alter table project add  `msprojectname` VARCHAR(200) NOT NULL AFTER `productFeatureId`;
ALTER TABLE `project` CHANGE COLUMN `Name` `Name` VARCHAR(200) NOT NULL AFTER `id`;

CREATE TABLE `ResourceTeamProject` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `featureSize` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `maxDevs` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `resourceTeamId` INT(11) NOT NULL,
  `projectId` INT(11) NOT NULL,
  `featureSizeRemaining` DECIMAL(9,2) NULL DEFAULT '0.00',
  PRIMARY KEY (`id`),
  INDEX `resourceTeamId` (`resourceTeamId`),
  INDEX `projectFeature` (`projectId`),
  CONSTRAINT `resourceteamprojectfeature_ibfk_1` FOREIGN KEY (`resourceTeamId`) REFERENCES `resourceteam` (`id`),
  CONSTRAINT `resourceteamprojectfeature_ibfk_2` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`)
)
  default CHARSET =utf8
  ENGINE=InnoDB
;
alter table project convert to character set utf8 collate utf8_unicode_ci;
ALTER TABLE `resourceteamproductfeature`
  CHANGE COLUMN `featureSizeRemaining` `featureSizeRemaining` DECIMAL(9,2) NULL DEFAULT '0.00' AFTER `productFeatureId`;
