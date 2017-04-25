CREATE TABLE FunctionalArea (
  functionalArea BIGINT PRIMARY KEY,
  department     VARCHAR(100),
  shortname      VARCHAR(100),
  PandLCategory  VARCHAR(10),
  company        VARCHAR(100)

) engine=innodb DEFAULT CHARSET=utf8;

CREATE TABLE ProfitCenter (
  profitCenter BIGINT PRIMARY KEY,
  shortname          VARCHAR(50)
)  engine=innodb DEFAULT CHARSET=utf8;

ALTER TABLE CostCenter
  ADD COLUMN `FunctionalAreaID` BIGINT  NULL ;
ALTER TABLE CostCenter
  ADD COLUMN `ProfitCenterID` BIGINT  NULL;

Alter table CostCenter add FOREIGN KEY  (FunctionalAreaID) REFERENCES FunctionalArea(functionalArea);
Alter table CostCenter add FOREIGN KEY  (ProfitCenterID) REFERENCES ProfitCenter(profitCenter);
