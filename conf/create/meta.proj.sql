
-- Project DB
-- This is modeling the ramp up of developers new to the company.
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (0, 0.00);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (1, 0.00);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (2, 0.00);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (3, 0.50);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (4, 0.50);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (5, 0.60);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (6, 0.75);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (7, 0.75);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (8, 0.75);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (9, 0.85);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (10, 0.85);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (11, 0.85);
INSERT INTO `EmpEfficiency` (`month`, `efficiency`) VALUES (12, 1.00);


-- This is modelling the level of certainty we have in the features on the roadmap
-- and to deal with 'surprises' coming in. the aim is if we commit to a date
-- we can keep it, even though we know surprises will come in later.
--
INSERT INTO `RoadmapSlack` (`ordering`, `efficiency`) VALUES (1, 1.00);
INSERT INTO `RoadmapSlack` (`ordering`, `efficiency`) VALUES (2, 0.95);
INSERT INTO `RoadmapSlack` (`ordering`, `efficiency`) VALUES (3, 0.90);
INSERT INTO `RoadmapSlack` (`ordering`, `efficiency`) VALUES (4, 0.75);
INSERT INTO `RoadmapSlack` (`ordering`, `efficiency`) VALUES (5, 0.65);
