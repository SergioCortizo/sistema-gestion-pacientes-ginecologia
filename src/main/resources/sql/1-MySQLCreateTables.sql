DROP TABLE IF EXISTS notice;
DROP TABLE IF EXISTS common_task_user;
DROP TABLE IF EXISTS grupal_message;
DROP TABLE IF EXISTS common_task;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS recipe_medicine;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS calendar_entry;
DROP TABLE IF EXISTS patients_of_interest;
DROP TABLE IF EXISTS complementary_test;
DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS meeting;
DROP TABLE IF EXISTS patient_contraceptive;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS user_speciality;
DROP TABLE IF EXISTS speciality;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS settings;
DROP TABLE IF EXISTS medicine;
DROP TABLE IF EXISTS diagnostic_test;
DROP TABLE IF EXISTS contraceptive;

DROP TABLE IF EXISTS application_logs;

CREATE TABLE application_logs (
	id BIGINT NOT NULL AUTO_INCREMENT,
	level VARCHAR(10) NOT NULL,
	timestamp TIMESTAMP NOT NULL,
	thread VARCHAR(100),
	logger VARCHAR(100),
	message LONGBLOB,
	CONSTRAINT ApplicationLogsPK PRIMARY KEY (id)
);

CREATE TABLE user
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR (60) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   username VARCHAR (60) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   password VARCHAR (60) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   email VARCHAR (60) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   enabled TINYINT (4) DEFAULT NULL,
   postal_address VARCHAR (100) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   location VARCHAR (60) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   dni VARCHAR (9) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   phone_number VARCHAR (9) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   discharge_date TIMESTAMP NOT NULL,
   last_time_seen_notices DATETIME DEFAULT NULL,
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
   CONSTRAINT RoleFK FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT UserFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
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
   CONSTRAINT UserScheduleFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
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
   CONSTRAINT SpecialityFK FOREIGN KEY (speciality_id) REFERENCES speciality (id) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT UserSpecialityFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
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
   name VARCHAR (60) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   DNI_NIF VARCHAR (9) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   mobile_phone VARCHAR (9) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   landline VARCHAR (9) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   birthday DATETIME NOT NULL,
   hist_numsergas VARCHAR (14) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   postal_address VARCHAR (100) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   location VARCHAR (60) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   email VARCHAR (60) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
   allergies VARCHAR (1000) CHARACTER SET utf8 COLLATE utf8_spanish_ci,
   diseases VARCHAR (1000) CHARACTER SET utf8 COLLATE utf8_spanish_ci,
   interventions VARCHAR (1000) CHARACTER SET utf8 COLLATE utf8_spanish_ci,
   family_background VARCHAR (1000) CHARACTER SET utf8 COLLATE utf8_spanish_ci,
   smoker TINYINT (4) DEFAULT NULL,
   menarche BIGINT,
   menopause BIGINT,
   last_menstruation_date DATETIME,
   discharge_date TIMESTAMP,
   pregnancies BIGINT,
   childbirths BIGINT,
   cesarean_sections BIGINT,
   misbirths BIGINT,
   menstrual_type VARCHAR (60) CHARACTER SET utf8 COLLATE utf8_spanish_ci,
   user_id BIGINT NOT NULL,
   enabled TINYINT (4) DEFAULT NULL,
   CONSTRAINT PatientPK PRIMARY KEY (id),
   CONSTRAINT DNI_NIFPatientUniqueKey UNIQUE (DNI_NIF),
   CONSTRAINT HistNumSERGASPatientUniqueKey UNIQUE (hist_numsergas),
   CONSTRAINT PatientFromUserFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
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
   CONSTRAINT PatientFK FOREIGN KEY (patient_id) REFERENCES patient (id) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT ContraceptiveFK FOREIGN KEY (contraceptive_id) REFERENCES contraceptive (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE meeting (
	id BIGINT NOT NULL AUTO_INCREMENT,
	activity VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
	comments LONGBLOB,
	meeting_date TIMESTAMP NOT NULL,
	patient_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	CONSTRAINT MeetingPK PRIMARY KEY(id),
	CONSTRAINT PatientMeetingFK FOREIGN KEY (patient_id) REFERENCES patient (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT PatientUserFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE question (
	id BIGINT NOT NULL AUTO_INCREMENT,
	question VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
	CONSTRAINT QuestionPK PRIMARY KEY(id)
);

CREATE TABLE answer (
	id BIGINT NOT NULL AUTO_INCREMENT,
	answer VARCHAR(1000) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
	question_id BIGINT NOT NULL,
	meeting_id BIGINT NOT NULL,
	CONSTRAINT AnswerPK PRIMARY KEY(id),
	CONSTRAINT AnswerQuestionFK FOREIGN KEY (question_id) REFERENCES question (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT AnswerMeetingFK FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE complementary_test (
	id VARCHAR(255) NOT NULL,
	data LONGBLOB NOT NULL,
	file_name VARCHAR(255) NOT NULL,
	file_type VARCHAR(255) NOT NULL,
	diagnostic_test_id BIGINT NOT NULL,
	meeting_id BIGINT NOT NULL,
	CONSTRAINT ComplementaryTestPK PRIMARY KEY (id),
	CONSTRAINT ComplementaryTestDiagnosticTestFK FOREIGN KEY (diagnostic_test_id) REFERENCES diagnostic_test (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT ComplementaryTestMeetingFK FOREIGN KEY (meeting_id) REFERENCES meeting (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE patients_of_interest (
	user_id BIGINT NOT NULL,
	patient_id BIGINT NOT NULL,
	CONSTRAINT PatientsOfInterestPK PRIMARY KEY (user_id, patient_id),
	CONSTRAINT PatientsOfInterestUserFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT PatientsOfInterestPatientFK FOREIGN KEY (patient_id) REFERENCES patient (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE calendar_entry (
	id BIGINT NOT NULL AUTO_INCREMENT,
	entry_date TIMESTAMP NOT NULL,
	state ENUM ('opened', 'closed', 'cancelled') NOT NULL,
	reason VARCHAR(255) NOT NULL,
	user_id BIGINT NOT NULL,
	patient_id BIGINT NOT NULL,
	CONSTRAINT CalendarEntryPK PRIMARY KEY (id),
	CONSTRAINT CalendarEntryUserFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT CalendarEntryPatientFK FOREIGN KEY (patient_id) REFERENCES patient (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE recipe (
	id BIGINT NOT NULL AUTO_INCREMENT,
	dispensing_date DATETIME NOT NULL DEFAULT NOW(),
	clarifications LONGBLOB,
	meeting_id BIGINT NOT NULL,
	CONSTRAINT RecipePK PRIMARY KEY (id),
	CONSTRAINT RecipeMeetingFK FOREIGN KEY (meeting_id) REFERENCES meeting (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE recipe_medicine (
	recipe_id BIGINT NOT NULL,
	medicine_id BIGINT NOT NULL,
	denomination VARCHAR(100),
	dosification VARCHAR(200),
	form_of_administration VARCHAR(200),
	format BIGINT DEFAULT 0,
	units BIGINT DEFAULT 0,
	posology VARCHAR(100),
	CONSTRAINT RecipeMedicinePK PRIMARY KEY (recipe_id, medicine_id),
	CONSTRAINT RecipeMedicineRecipeFK FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT RecipeMedicineMedicineFK FOREIGN KEY (medicine_id) REFERENCES medicine (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE message (
	id BIGINT NOT NULL AUTO_INCREMENT,
	subject VARCHAR(100) NOT NULL,
	message_body LONGBLOB,
	message_date TIMESTAMP NOT NULL,
	message_read TINYINT (4) DEFAULT NULL,
	sender_id BIGINT NOT NULL,
	receiver_id BIGINT NOT NULL,
	replied_message_id BIGINT DEFAULT NULL,
	interconsultation_meeting_id BIGINT DEFAULT NULL,
	CONSTRAINT MessagePK PRIMARY KEY (id),
	CONSTRAINT MessageSenderFK FOREIGN KEY (sender_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT MessageReceiverFK FOREIGN KEY (receiver_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT RepliedMessageFK FOREIGN KEY (replied_message_id) REFERENCES message (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT InterconsultationMeetingFK FOREIGN KEY (interconsultation_meeting_id) REFERENCES meeting (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE common_task (
	id BIGINT NOT NULL AUTO_INCREMENT,
	title VARCHAR(100) NOT NULL,
	description LONGBLOB,
	CONSTRAINT CommonTaskPK PRIMARY KEY (id)
);

CREATE TABLE grupal_message (
	id BIGINT NOT NULL AUTO_INCREMENT,
	message_body LONGBLOB,
	datetime TIMESTAMP NOT NULL,
	common_task_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	INDEX datetime_desc (datetime DESC),
	CONSTRAINT GrupalMessagePK PRIMARY KEY (id),
	CONSTRAINT GrupalMessageCommonTaskFK FOREIGN KEY (common_task_id) REFERENCES common_task (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT GrupalMessageUserFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE common_task_user (
	common_task_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	last_time_read TIMESTAMP,
	CONSTRAINT CommonTaskUserPK PRIMARY KEY (common_task_id, user_id),
	CONSTRAINT CommonTaskUserCommonTaskFK FOREIGN KEY (common_task_id) REFERENCES common_task (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT CommonTaskUserUserFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE notice (
	id BIGINT NOT NULL AUTO_INCREMENT,
	notice VARCHAR(1000),
	datetime TIMESTAMP NOT NULL,
	user_id BIGINT NOT NULL,
	CONSTRAINT NoticePK PRIMARY KEY (id),
	CONSTRAINT NoticeUserFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);
