DROP TABLE complementary_test;
DROP TABLE answer;
DROP TABLE question;
DROP TABLE meeting;
DROP TABLE patient_contraceptive;
DROP TABLE patient;
DROP TABLE user_speciality;
DROP TABLE speciality;
DROP TABLE user_role;
DROP TABLE schedule;
DROP TABLE role;
DROP TABLE user;
DROP TABLE settings;
DROP TABLE medicine;
DROP TABLE diagnostic_test;
DROP TABLE contraceptive;

CREATE TABLE user
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR (60) COLLATE latin1_bin NOT NULL,
   username VARCHAR (60) COLLATE latin1_bin NOT NULL,
   password VARCHAR (60) COLLATE latin1_bin NOT NULL,
   email VARCHAR (60) COLLATE latin1_bin NOT NULL,
   enabled TINYINT (4) DEFAULT NULL,
   postal_address VARCHAR (100) COLLATE latin1_bin NOT NULL,
   location VARCHAR (60) COLLATE latin1_bin NOT NULL,
   dni VARCHAR (9) COLLATE latin1_bin NOT NULL,
   phone_number VARCHAR (9) COLLATE latin1_bin NOT NULL,
   discharge_date TIMESTAMP NOT NULL,
   collegiate_number VARCHAR (9) DEFAULT NULL,
   CONSTRAINT UserPK PRIMARY KEY (id),
   CONSTRAINT NameUniqueKey UNIQUE (username)
)
ENGINE = InnoDB;
CREATE INDEX UserIndexByUserName ON user (username);

CREATE TABLE role
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR (60) NOT NULL,
   CONSTRAINT RolePK PRIMARY KEY (id),
   CONSTRAINT RoleUniqueKey UNIQUE (name)
)
ENGINE = InnoDB;
CREATE INDEX RoleIndexByName ON role (name);

CREATE TABLE user_role
(
   user_id BIGINT NOT NULL,
   role_id BIGINT NOT NULL,
   CONSTRAINT UserRolePK PRIMARY KEY
   (
      user_id,
      role_id
   ),
   CONSTRAINT RoleFK FOREIGN KEY (role_id) REFERENCES role (id),
   CONSTRAINT UserFK FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE schedule
(
   user_id BIGINT NOT NULL,
   weekday ENUM
   (
      'monday',
      'tuesday',
      'wednesday',
      'thursday',
      'friday'
   )
   NOT NULL,
   initial_hour TIME NOT NULL,
   final_hour TIME NOT NULL,
   CONSTRAINT SchedulePK PRIMARY KEY
   (
      user_id,
      weekday
   ),
   CONSTRAINT InitialHourRange CHECK
   (
      initial_hour >= '00:00:00'
      AND
         initial_hour <= '23:59:59'
   ),
   CONSTRAINT FinalHourRange CHECK
   (
      final_hour >= '00:00:00'
      AND
         final_hour <= '23:59:59'
   ),
   CONSTRAINT UserScheduleFK FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE INDEX ScheduleIndexByUserIdAndWeekday ON schedule
(
   user_id,
   weekday
);

CREATE TABLE speciality
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR (100) NOT NULL,
   enabled TINYINT (4) DEFAULT NULL,
   CONSTRAINT SpecialityPK PRIMARY KEY (id),
   CONSTRAINT NameSpecialityUniqueKey UNIQUE (name)
);

CREATE TABLE user_speciality
(
   user_id BIGINT NOT NULL,
   speciality_id BIGINT NOT NULL,
   CONSTRAINT UserSpecialityPK PRIMARY KEY
   (
      user_id,
      speciality_id
   ),
   CONSTRAINT SpecialityFK FOREIGN KEY (speciality_id) REFERENCES speciality (id),
   CONSTRAINT UserSpecialityFK FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE medicine
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR (100) NOT NULL,
   enabled TINYINT (4) DEFAULT NULL,
   CONSTRAINT MedicinePK PRIMARY KEY (id),
   CONSTRAINT NameMedicineUniqueKey UNIQUE (name)
);

CREATE TABLE diagnostic_test
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR (100) NOT NULL,
   enabled TINYINT (4) DEFAULT NULL,
   CONSTRAINT DiagnosticTestPK PRIMARY KEY (id),
   CONSTRAINT NameDiagnosticTestUniqueKey UNIQUE (name)
);

CREATE TABLE contraceptive
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR (100) NOT NULL,
   enabled TINYINT (4) DEFAULT NULL,
   CONSTRAINT ContraceptivePK PRIMARY KEY (id),
   CONSTRAINT NameContraceptiveUniqueKey UNIQUE (name)
);

CREATE TABLE settings
(
   name VARCHAR (60) NOT NULL,
   value LONGBLOB,
   CONSTRAINT SettingsPK PRIMARY KEY (name)
);

CREATE TABLE patient
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR (60) COLLATE latin1_bin NOT NULL,
   DNI_NIF VARCHAR (9) COLLATE latin1_bin NOT NULL,
   mobile_phone VARCHAR (9) COLLATE latin1_bin NOT NULL,
   landline VARCHAR (9) COLLATE latin1_bin NOT NULL,
   birthday DATETIME NOT NULL,
   hist_numsergas VARCHAR (14) COLLATE latin1_bin NOT NULL,
   postal_address VARCHAR (100) COLLATE latin1_bin NOT NULL,
   location VARCHAR (60) COLLATE latin1_bin NOT NULL,
   email VARCHAR (60) COLLATE latin1_bin NOT NULL,
   allergies VARCHAR (1000) COLLATE latin1_bin,
   diseases VARCHAR (1000) COLLATE latin1_bin,
   interventions VARCHAR (1000) COLLATE latin1_bin,
   family_background VARCHAR (1000) COLLATE latin1_bin,
   smoker TINYINT (4) DEFAULT NULL,
   menarche BIGINT,
   menopause BIGINT,
   last_menstruation_date DATETIME,
   discharge_date TIMESTAMP,
   pregnancies BIGINT,
   childbirths BIGINT,
   cesarean_sections BIGINT,
   misbirths BIGINT,
   menstrual_type VARCHAR (60) COLLATE latin1_bin,
   user_id BIGINT NOT NULL,
   enabled TINYINT (4) DEFAULT NULL,
   CONSTRAINT PatientPK PRIMARY KEY (id),
   CONSTRAINT DNI_NIFPatientUniqueKey UNIQUE (DNI_NIF),
   CONSTRAINT HistNumSERGASPatientUniqueKey UNIQUE (hist_numsergas)
);

CREATE TABLE patient_contraceptive
(
   patient_id BIGINT NOT NULL,
   contraceptive_id BIGINT NOT NULL,
   CONSTRAINT PatientContraceptivePK PRIMARY KEY
   (
      patient_id,
      contraceptive_id
   ),
   CONSTRAINT PatientFK FOREIGN KEY (patient_id) REFERENCES patient (id),
   CONSTRAINT ContraceptiveFK FOREIGN KEY (contraceptive_id) REFERENCES contraceptive (id)
);

CREATE TABLE meeting (
	id BIGINT NOT NULL AUTO_INCREMENT,
	activity VARCHAR(100) COLLATE latin1_bin NOT NULL,
	comments LONGBLOB,
	meeting_date TIMESTAMP NOT NULL,
	patient_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	CONSTRAINT MeetingPK PRIMARY KEY(id),
	CONSTRAINT PatientMeetingFK FOREIGN KEY (patient_id) REFERENCES patient (id),
	CONSTRAINT PatientUserFK FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE question (
	id BIGINT NOT NULL AUTO_INCREMENT,
	question VARCHAR(100) COLLATE latin1_bin NOT NULL,
	CONSTRAINT QuestionPK PRIMARY KEY(id)
);

CREATE TABLE answer (
	id BIGINT NOT NULL AUTO_INCREMENT,
	answer VARCHAR(1000) COLLATE latin1_bin NOT NULL,
	question_id BIGINT NOT NULL,
	meeting_id BIGINT NOT NULL,
	CONSTRAINT AnswerPK PRIMARY KEY(id),
	CONSTRAINT AnswerQuestionFK FOREIGN KEY (question_id) REFERENCES question (id),
	CONSTRAINT AnswerMeetingFK FOREIGN KEY (meeting_id) REFERENCES meeting(id)
);

CREATE TABLE complementary_test (
	id VARCHAR(255) NOT NULL,
	data LONGBLOB NOT NULL,
	file_name VARCHAR(255) NOT NULL,
	file_type VARCHAR(255) NOT NULL,
	diagnostic_test_id BIGINT NOT NULL,
	meeting_id BIGINT NOT NULL,
	CONSTRAINT ComplementaryTestPK PRIMARY KEY (id),
	CONSTRAINT ComplementaryTestDiagnosticTestFK FOREIGN KEY (diagnostic_test_id) REFERENCES diagnostic_test (id),
	CONSTRAINT ComplementaryTestMeetingFK FOREIGN KEY (meeting_id) REFERENCES meeting (id)
);








