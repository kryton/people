
CREATE TABLE `EmpEfficiency` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `month` int not null,
  efficiency DECIMAL (5,2),

  PRIMARY KEY (`id`),
  UNIQUE KEY (`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into  EmpEfficiency (month,efficiency) values (0,0);
insert into  EmpEfficiency (month,efficiency) values (1,0);
insert into  EmpEfficiency (month,efficiency) values (2,0);
insert into  EmpEfficiency (month,efficiency) values (3,0.5);
insert into  EmpEfficiency (month,efficiency) values (4,0.5);
insert into  EmpEfficiency (month,efficiency) values (5,0.6);
insert into  EmpEfficiency (month,efficiency) values (6,0.75);
insert into  EmpEfficiency (month,efficiency) values (7,0.75);
insert into  EmpEfficiency (month,efficiency) values (8,0.75);
insert into  EmpEfficiency (month,efficiency) values (9,0.85);
insert into  EmpEfficiency (month,efficiency) values (10,0.85);
insert into  EmpEfficiency (month,efficiency) values (11,0.85);
insert into  EmpEfficiency (month,efficiency) values (12,1);

ALTER TABLE `productfeature`
	CHANGE COLUMN `msprojectname` `msprojectname` VARCHAR(200) NULL DEFAULT NULL AFTER `stageId`;

ALTER TABLE `managedclient`
	ADD COLUMN `msprojectname` Varchar(30) NULL ;

