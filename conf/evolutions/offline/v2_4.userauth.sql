ALTER TABLE `AuthPermission`
  ADD COLUMN `description` TEXT NULL AFTER `permission`;
ALTER TABLE `AuthRole`
  ADD COLUMN `description` TEXT NULL AFTER `role`;
ALTER TABLE `AuthRole`
  ADD COLUMN `isAdmin` BIT NOT NULL DEFAULT b'0' AFTER `description`;
