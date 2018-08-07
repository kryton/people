ALTER TABLE `AuthPermission`
  ADD COLUMN `description` TEXT NULL AFTER `permission`;
ALTER TABLE `AuthRole`
  ADD COLUMN `description` TEXT NULL AFTER `role`;
ALTER TABLE `AuthRole`
  ADD COLUMN `isAdmin` BIT NOT NULL DEFAULT b'0' AFTER `description`;

ALTER TABLE `AuthRolePermission`
  ADD CONSTRAINT `authPerm` FOREIGN KEY (`permissionID`) REFERENCES `AuthPermission` (`id`),
  ADD CONSTRAINT `authRole` FOREIGN KEY (`roleID`) REFERENCES `AuthRole` (`id`);
