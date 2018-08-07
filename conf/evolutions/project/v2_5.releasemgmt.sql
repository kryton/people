create table ReleaseType (
  id int(11) not null AUTO_INCREMENT,
  name VARCHAR(255) not null,
  emailAlias varchar(255) not null,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into ReleaseType (name,emailAlias) values ('Default','Release@example.com');

create table ReleaseAuthorizationType (
  id int(11) not null AUTO_INCREMENT,
  name VARCHAR(255) not null,
  description text null,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE ProjectRelease
  CHANGE COLUMN `Deployed` `Deployed` DATETIME NULL DEFAULT NULL AFTER `Name`,
  ADD COLUMN `Planned` DATETIME NULL DEFAULT NULL AFTER `Deployed`,
  ADD COLUMN `RolledBack` DATETIME NULL DEFAULT NULL AFTER `Planned`,
  ADD COLUMN `ReleaseTypeId` INT not NULL default 1 AFTER `Planned`,
  ADD COLUMN `changeticket` VARCHAR(50) NULL DEFAULT NULL AFTER `ReleaseTypeId`,
  ADD COLUMN `releasefileURL` varchar(255) NULL DEFAULT NULL AFTER `changeticket`,
  ADD COLUMN `releasenotes` TEXT NULL DEFAULT NULL AFTER `releasefileURL`;

alter table ProjectRelease add FOREIGN KEY  (ReleaseTypeId) REFERENCES ReleaseType(id) on DELETE restrict;

create table ReleaseTypeAuthorizationPeople (
  id int(11) not null AUTO_INCREMENT,
  ReleaseTypeId int(11) not null,
  ReleaseAuthorityId int(11) not null,
  login varchar(20) not null,
  isPrimary boolean not null,
  PRIMARY KEY (`id`),
  CONSTRAINT `ReleaseAuthorizationPPL_fk1` FOREIGN KEY (`ReleaseTypeId`) REFERENCES `ReleaseType` (`id`),
  CONSTRAINT `ReleaseAuthorizationType_fk1` FOREIGN KEY (`ReleaseAuthorityId`) REFERENCES `ReleaseAuthorizationType` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table ReleaseAuthorization (
  id int(11) not null AUTO_INCREMENT,
  ReleaseId int(11) not null,
  ReleaseAuthorityId int(11) NOT NULL,
  login varchar(20) not null,
  approvedDate datetime null,
  rejectedDate datetime null,
  isApproved boolean null,
  notes text null,
  PRIMARY KEY (id),
  CONSTRAINT ReleaseAuth_rel FOREIGN KEY (ReleaseId) REFERENCES projectrelease (id),
  CONSTRAINT ReleaseAuth_type FOREIGN KEY (ReleaseAuthorityId) REFERENCES ReleaseAuthorizationType (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
