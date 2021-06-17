INSERT INTO user (name, username, password, email, enabled, postal_address, location, DNI, phone_number, discharge_date, collegiate_number, last_time_seen_notices) 
	VALUES ('Usuario administrador', 'admin', '$2y$10$UgB6dXrhUVwubG8O7lKFz.dzAh12obZGMzKOg1gR.YdWJW9pXmchW', 'admin@example.com', 1, 'Alameda Lorem ipsum dolor, 210A 5ºG', 'A Coruña', '61861134N', '649383350', '2019-01-09 15:48:23', '253011225', '2021-03-22 09:32:14');
		   
INSERT INTO role (id, name) 
	VALUES (1, 'ROLE_FACULTATIVE'), 
		   (2, 'ROLE_ADMIN'),
		   (3, 'ROLE_CITATIONS');

INSERT INTO user_role (user_id, role_id)
	VALUES (1, 1),
		   (1, 2),
		   (1, 3);
		   
INSERT INTO schedule (user_id, weekday, initial_hour, final_hour)
	VALUES (1, 'monday', '10:00:00', '14:00:00'),
		   (1, 'tuesday', '17:00:00', '20:00:00'),
		   (1, 'friday', '10:00:00', '14:00:00');

INSERT INTO speciality (name, enabled)
	VALUES ('Obstetricia general', 1),
	       ('Ginecología general', 1),
	       ('Alto riesgo obstétrico', 1),
	       ('Ecografía', 1),
	       ('Medicina reproductiva', 1),
	       ('Ginecología oncológica', 1),
	       ('Fisiopatología mamaria', 1),
	       ('Urgencias en Obstetricia y Ginecología', 1);
	       
INSERT INTO user_speciality (user_id, speciality_id)
	VALUES (1, 1),
		   (1, 7);
		   
INSERT INTO medicine (name, enabled)
	VALUES ('Ácido fólico', 1),	   	       
	       ('Amonio', 1),	   	       
	       ('Amoxicilina', 1),	   	       
	       ('Bisacodil', 1),	   	       
	       ('Cefalosporinas', 1),	   	       
	       ('Clindamicina', 1),	   	       
	       ('Clorfeniramina', 1),	   	       
	       ('Cloridio de amonio', 1),	   	       
	       ('Clorhexidina', 1),	   	       
	       ('Digoxina', 1),	   	       
	       ('Doxilamina', 1),	   	       
	       ('Fenoterol', 1),	   	       
	       ('Insulina', 1),	   	       
	       ('Levotiroxina', 1),	   	       
	       ('Paracetamol', 1),	   	       
	       ('Tiamina', 1);   	       
	       
INSERT INTO diagnostic_test (name, enabled)	       
	VALUES ('Citología', 1),
		   ('Colposcopia', 1),
		   ('Biopsia endometrial', 1),
		   ('Ecografía ginecológica', 1),
		   ('Ecografía mamaria', 1),
		   ('Histeroscopia', 1),
		   ('Monitorización fetal', 1),
		   ('Amniocentesis', 1),
		   ('Ecografía en 5D', 1),
		   ('Mamografía', 1),
		   ('Paponicolau', 1),
		   ('Histerosalpingografia', 1);
		   
INSERT INTO contraceptive (name, enabled)	       
	VALUES ('Métodos hormonales', 1),
		   ('Implantes', 1),
		   ('DIU', 1),
		   ('Métodos barrera', 1),
		   ('Vasectomía', 1),
		   ('Ligadura de trompas', 1),
		   ('Planificación familiar natural', 1);
		   	       
INSERT INTO settings (name, value)
	VALUES ('enterpriseName', 'La Ginecología de Doña Paquita'),
		   ('logo', 'data:image/jpeg;base64,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');	       

INSERT INTO question(question)
	VALUES ('¿Es activa sexualmente?'),
		   ('¿Cuándo fue su última relación sexual?'),
		   ('¿Utiliza métodos anticonceptivos?'),
		   ('¿Realiza prácticas sexuales de riesgo?'),
		   ('¿Ha tenido algún tipo de ETS?'),
		   ('¿Cuándo se realizó la última citología?');




	       