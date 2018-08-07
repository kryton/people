CREATE TABLE `RoadmapSlack` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `ordering` INT(11) NOT NULL,
  `efficiency` DECIMAL(5,2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ordering` (`ordering`)
)
  COLLATE='utf8_general_ci'
  ENGINE=InnoDB
;
