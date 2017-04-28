drop table AuthUserPreference;
drop table AuthPreference;

CREATE TABLE AuthPreference (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name      VARCHAR(100) not null,
  code varchar(10) not null,
  UNIQUE (name),
  unique(code)
) engine=innodb DEFAULT CHARSET=utf8;
CREATE TABLE AuthUserPreference (
  authPreferenceid BIGINT ,
  login      VARCHAR(20) not null,
  PRIMARY KEY (authPreferenceid,login)

) engine=innodb DEFAULT CHARSET=utf8;

Alter table AuthUserPreference add FOREIGN KEY  (authPreferenceid) REFERENCES AuthPreference(id);
INSERT INTO `AuthPreference` (name,code) VALUES ('Lighter Colors', 'light');
INSERT INTO `AuthPreference` (name,code) VALUES ('Less Fluff more Stuff ', 'compact');
