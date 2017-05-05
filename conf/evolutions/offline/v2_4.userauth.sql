ALTER TABLE `authpermission`
  ADD COLUMN `description` TEXT NULL AFTER `permission`;
ALTER TABLE `authrole`
  ADD COLUMN `description` TEXT NULL AFTER `role`;
ALTER TABLE `authrole`
  ADD COLUMN `isAdmin` BIT NOT NULL DEFAULT b'0' AFTER `description`;
