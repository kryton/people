ALTER TABLE `ProjectDependency`
  ADD COLUMN `dependencytype` CHAR(2) NOT NULL DEFAULT 'FS' AFTER `toProject`;
ALTER TABLE `project`
  ADD COLUMN `constraintType` CHAR(4) NOT NULL DEFAULT 'ASAP' AFTER `msprojectname`,
  ADD COLUMN `constraintDate` DATE NULL DEFAULT NULL AFTER `constraintType`;
