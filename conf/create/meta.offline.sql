-- Offline DB
INSERT INTO `AuthRole` (`role`, `description`, `isAdmin`) VALUES ('HR', 'Human Resources', b'0');
INSERT INTO `AuthRole` (`role`, `description`, `isAdmin`) VALUES ('AwardCommittee', 'Members of the Award Committee', b'0');
INSERT INTO `AuthPermission` (`permission`, `description`) VALUES ('AuthorizeAwardsHR', 'used to authorize awards at the HR level');
INSERT INTO `AuthPermission` (`permission`, `description`) VALUES ('AuthorizeAwards', 'After HR Authorizes, allows award to be \'public\'');
INSERT INTO `AuthRolePermission` (`permissionID`, `roleID`) VALUES (2, 2);
INSERT INTO `AuthRolePermission` (`permissionID`, `roleID`) VALUES (1, 1);
