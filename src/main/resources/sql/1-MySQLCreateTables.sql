DROP TABLE schedule;
DROP TABLE user_role;
DROP TABLE user_speciality;
DROP TABLE user;
DROP TABLE role;
DROP TABLE speciality;
DROP TABLE medicine;
DROP TABLE diagnostic_test;
DROP TABLE contraceptive;
DROP TABLE settings;

CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) COLLATE latin1_bin NOT NULL,
    username VARCHAR(60) COLLATE latin1_bin NOT NULL,
    password VARCHAR(60) COLLATE latin1_bin NOT NULL,
    email VARCHAR(60) COLLATE latin1_bin NOT NULL,
    enabled TINYINT(4) DEFAULT NULL,
    postal_address VARCHAR(100) COLLATE latin1_bin NOT NULL,
    location VARCHAR(60) COLLATE latin1_bin NOT NULL,
    dni VARCHAR(9) COLLATE latin1_bin NOT NULL,
    phone_number VARCHAR(9) COLLATE latin1_bin NOT NULL,
    discharge_date TIMESTAMP NOT NULL,
    collegiate_number VARCHAR(9) DEFAULT NULL,
    CONSTRAINT UserPK PRIMARY KEY (id),
    CONSTRAINT NameUniqueKey UNIQUE (username)
) ENGINE = InnoDB;

CREATE INDEX UserIndexByUserName ON user (username);

CREATE TABLE role (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(60) NOT NULL,
  CONSTRAINT RolePK PRIMARY KEY (id),
  CONSTRAINT RoleUniqueKey UNIQUE (name)
) ENGINE = InnoDB;

CREATE INDEX RoleIndexByName ON role (name);

CREATE TABLE user_role (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  CONSTRAINT UserRolePK PRIMARY KEY(user_id, role_id),
  CONSTRAINT RoleFK FOREIGN KEY (role_id) REFERENCES role (id),
  CONSTRAINT UserFK FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE schedule (
  user_id BIGINT NOT NULL,
  weekday ENUM('monday', 'tuesday', 'wednesday', 'thursday', 'friday') NOT NULL,
  initial_hour TIME NOT NULL,
  final_hour TIME NOT NULL,
  CONSTRAINT SchedulePK PRIMARY KEY (user_id, weekday),
  CONSTRAINT InitialHourRange CHECK (initial_hour >= '00:00:00' AND initial_hour <= '23:59:59'),
  CONSTRAINT FinalHourRange CHECK (final_hour >= '00:00:00' AND final_hour <= '23:59:59'),
  CONSTRAINT UserScheduleFK FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE INDEX ScheduleIndexByUserIdAndWeekday ON schedule (user_id, weekday);

CREATE TABLE speciality (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	enabled TINYINT(4) DEFAULT NULL,
	CONSTRAINT SpecialityPK PRIMARY KEY (id),
	CONSTRAINT NameSpecialityUniqueKey UNIQUE (name)
);

CREATE TABLE user_speciality (
	user_id BIGINT NOT NULL,
	speciality_id BIGINT NOT NULL,
	CONSTRAINT UserSpecialityPK PRIMARY KEY (user_id, speciality_id),
	CONSTRAINT SpecialityFK FOREIGN KEY (speciality_id) REFERENCES speciality (id),
	CONSTRAINT UserSpecialityFK FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE medicine (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	enabled TINYINT(4) DEFAULT NULL,
	CONSTRAINT MedicinePK PRIMARY KEY (id),
	CONSTRAINT NameMedicineUniqueKey UNIQUE (name)
);

CREATE TABLE diagnostic_test (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	enabled TINYINT(4) DEFAULT NULL,
	CONSTRAINT DiagnosticTestPK PRIMARY KEY (id),
	CONSTRAINT NameDiagnosticTestUniqueKey UNIQUE (name)
);

CREATE TABLE contraceptive (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	enabled TINYINT(4) DEFAULT NULL,
	CONSTRAINT ContraceptivePK PRIMARY KEY (id),
	CONSTRAINT NameContraceptiveUniqueKey UNIQUE (name)
);

CREATE TABLE settings (
	name VARCHAR(60) NOT NULL,
	value LONGBLOB,
	CONSTRAINT SettingsPK PRIMARY KEY (name)
);

