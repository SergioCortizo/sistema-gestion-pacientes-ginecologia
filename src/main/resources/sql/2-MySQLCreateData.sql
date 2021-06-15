INSERT INTO user (name, username, password, email, enabled, postal_address, location, DNI, phone_number, discharge_date, collegiate_number, last_time_seen_notices) 
	VALUES ('User 1', 'user1', '$2y$10$UgB6dXrhUVwubG8O7lKFz.dzAh12obZGMzKOg1gR.YdWJW9pXmchW', 'user1@example.com', 1, 'Alameda Lorem ipsum dolor, 210A 5ºG', 'A Coruña', '61861134N', '649383350', '2019-01-09 15:48:23', '253011225', '2021-03-22 09:32:14'),                                        
		   ('User 2', 'user2', '$2y$10$/ZMcrTQzB9RWYxLR52zDFOFqOJulWKr/w3WbowKn0vjfdvuiGT5l2', 'user2@example.com', 1, 'Pasaje Lorem ipsum dolor sit, 275B', 'Madrid', '88878494P', '880721492', '2020-10-09 16:30:12', NULL, '2021-03-23 09:32:14'),
		   ('User 3', 'user3', '$2y$10$oitT6WNJErvZDxDHNNwMbu7WPn5IfklOuaZdaDA7OVQvwKXg1BZmK', 'user3@example.com', 1, 'C. Comercial Lorem ipsum, 7A', 'Granada', '83722842J', '779519883', '2020-11-21 12:00:48', '282998654', '2021-03-24 09:32:14'),
		   ('User 4', 'user4', '$2y$10$aya2t4UFLfac8R/RS37LRupBaLCsL5DmUA4vTj/Ctf5a54pJ0WDb.', 'user4@example.com', 0, 'Pasadizo Lorem ipsum, 265A 5ºE', 'Gijón', '54638037L', '626504814', '2021-01-20 17:54:47', NULL, '2021-03-25 09:32:14'),
		   ('User 5', 'user5', '$2y$10$BsgELu9MSqAZG65NE20nj.O7iz6weIUgXA8nwwE9tr4m/8aFg9Yc.', 'user5@example.com', 1, 'Vía Lorem, 209B', 'Ourense', '88440299D', '714877694', '2021-01-30 15:48:23', '152755566', '2021-03-26 09:32:14'),
		   ('User 6', 'user6', '$2y$10$rZcDlGBAu3ik8yEpsIjCAuWRNKv.Iy6WKhh7ZdvPCHkQiKt4Oh9Qu', 'user6@example.com', 1, 'Pasadizo Lorem ipsum, 265A 5ºE', 'Gijón', '17415911N', '626504814', '2021-01-20 17:54:47', NULL, '2021-03-25 09:32:14');
		   
INSERT INTO role (id, name) 
	VALUES (1, 'ROLE_FACULTATIVE'), 
		   (2, 'ROLE_ADMIN'),
		   (3, 'ROLE_CITATIONS');

INSERT INTO user_role (user_id, role_id)
	VALUES (1, 1),
		   (1, 2),
		   (1, 3),
		   (2, 2),
		   (3, 1),
		   (4, 2),
		   (5, 1), 
		   (6, 3);
		   
INSERT INTO schedule (user_id, weekday, initial_hour, final_hour)
	VALUES (1, 'monday', '10:00:00', '14:00:00'),
		   (1, 'tuesday', '17:00:00', '20:00:00'),
		   (1, 'friday', '10:00:00', '14:00:00'),
		   (2, 'monday', '17:00:00', '20:00:00'),
		   (2, 'thursday', '10:00:00', '12:00:00'),
		   (3, 'wednesday', '11:00:00', '14:00:00'),
		   (4, 'friday', '10:00:00', '20:00:00');

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
		   (1, 7),
		   (3, 1),
		   (5, 6),
		   (5, 8);
		   
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
	VALUES ('enterpriseName', 'Nombre de empresa de muestra'),
		   ('logo', 'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wgARCAHCAcIDASIAAhEBAxEB/8QAGwABAAMBAQEBAAAAAAAAAAAAAAUGBwQDAgH/xAAaAQEAAwEBAQAAAAAAAAAAAAAAAwQFAgYB/9oADAMBAAIQAxAAAAHVAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAfkXz0e3m3/AK8175YNGGfsgAAD5OPnofxo4mhyWVaLBbkBV0AAAAAAAAAAAAAAAAAAAIGkXekamBJ2TktNa6FTSAAAArVd0ek3smA0TO9E7ikRnbYAAAAAAAAAAAAAAAAAAEDSLvSNTAtlpq1ppag84LXoh5Xrj7HMgACk3ak2aMBomd6JYpSIztsAAAAAAAAAAAAAAAAAACBpF3pGpgWy01a00tRWLPx8SZtdI+z28/qg5zypafJHeXZPUkekrXlJu1Js0oDRM70SxSkRnbYAAAAAAAAAAAAAAAAAAEDSLvSNTAtlpq1ppahDeXEk8I5wACtyUkElSbtSZYIDRM70SxSkRnbYAAAAAAAAAAAAAAAAAAEDSLzRtTAtlpqVtpadE45qsX8jVHH2ZXoQ+dPn6huo6LZaxdNPCsNJu1Gp6UHomd6NYp94ztsA5ODuKaRUnz19D52AAAAAAAAAAAAAABwZzqua38jsv2WaPz0znUoHiWtXnNv23n6koPzVv3Gh8/3azvTR+CUpajNLnQrFT60+i36OUKep8U/0rOhjfv5YZ6WCgdloqnUd5k8x0ahrdAr3gAAAAAAAAAAAAAFYs/n3Dl0zzR+v53VP2l3LJ9Dw1q6vvOc/WiJ6tPsvWr23l+Ujrnl4U3qYNgmjH9I8/Ti+dZ1Mwdz1fP2IZPokZJvvGVW6rzmrgXMZPogAAAAAAAAAAAAAAOOgaXzWKWZyHRD6WJeZnLfute1JnH5FPocHTfmat1cv7P2KfDff31y90Ibby9T5llg+YDX85qirzuZu9fDz06WDitVY0azQ6xm7oAAAAAAAAAAAAAAACHmHXFIjNKWaGVv3r0cVK3Nm7cbJFa8HzsADxpF9S1sqXCn6mC9ZW6RWIyaM3bDmQAAAAAAAAAAAAAAAAADNPGYg9ry+p/URL4/pA5kAAAArtMm4TX85bbREy2btBFZAAAAAAAAAAAAAAAAAAAg6Nqme38f0v+VWr78tYz9kAABHdVAnpcXRy3TRx5/6Mf0oAAAAAAAAAAAAAAAAAAADg733nMPHQqNq+em7Zl/txNp6myNPSsKC5eerPGVSJs0u3hTtzN/Lz5+mV6AIrAAAAAAAAAAAAAAAAAAAAADn6D5TIDUvG3m5gvXFZpVJa/b78p3bdpKGzAT5S1A5kAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8QAKxAAAQQBAgUEAQUBAAAAAAAAAwABAgQFEDQREhMwUBUgITJAFCIxM5Ak/9oACAEBAAEFAv8AS171Zn9QrIJxmbtGsiC/qFZDuAJLxGXm8aulGTxt9l/4nJ5y0pTedXw+Z2iqVHstToMCXasYxpzt13rkVDZ+HzG0WE+vczO6VDZ+HzG0WE+vczO6VDZ+HzG0WE+ukpNCPqNfmZ2k3vzO6VDZ+HzG0WE+umak7DWGd3rq/YLXQLQjRuZBhoDzcWmZ3SobPw+Y2iwn10sgjYH6VPmAKIRqcWnGeKg71qAwvrmd0qGz8PmNosJ9e5md0qGz8PmNosJ9VdutWVK+x5+6zkmGSnZjZgszulQ2fh8xtFhPqss3/ZSfhb9r/Cf5fCfdZndKhs/D5duNRYR/lZsfz/D1SscPsyRulWWHhy11l3421SbhU9hDiEnyNdDvV5Jn4t+fdhz1ViycltWgscE4vCVK09aYTQNHSzZHXjYNI5K4nMWEWhBWSdWxFuZ4tyx0k7Ra3kZTfUFggHp2o2Y/n2h9Gwz8HqlY4VkKfXaTPF4SlB45CwynfsSTu7uMcizpVWrQWSP0ayxYue1rl7PGSqY55s1CuyNjBSY4ZgmEkhECRii/OzAOMVjbPQJpYqjOi4skU9OwyjSsSQcXJBBAMVOTQjcO9gyxgOiDScuWM5POeKB1Ta3gdcCwpOMPzpM0mu13rlVC900zs7e4k4jjeuPYdYyt1ia3NqsLt/YX+3Cf3/n2QRsDsAmAirWyAQMgEii7SbSc4waxkhwRzkPJU6srExwiOGpI843bg+HM0Sa2zMEKwsPjwBgwNC3RIDWMpRTWjsntHdO7vrUx0iKEIwj7ctX5Ct8KrkmTWBOjXgDa1ZlYnCLznWEwQ+CsUQmRcYWKlXNDRvlQqmmhYsjqvUEDsEgxIXKUwPqMciSo02rt4Z/5pbvuZOuKNdYyvA8xjgOPh7MeWwOXLNn4t28zLhXWFj+3xGXHy2ljS9Wr28qXqWVjB9On4jKh6lZY6x0D9q6f9OF/l6wnMZm4N4m9X/TmWMudkpIihbsPYKsVX6Y/FWgNYEUchTVPIvBQnGcfbZtjA1mxOxJY6p1peMuVY2YnDMM0IsxOLKEZRygXXqQFLKwRsgcmtGjIqjFot40o4FjYxs4qUZQf2grFMquPgLyUxxmxMcCSfFMvSVHFDQqYB/64f//EAC4RAAEEAAQEBgEEAwAAAAAAAAEAAgMEBRESITEyM0AQEyAiUWEVFCNBcEJTcf/aAAgBAwEBPwH+0rd10LtLQosReXAOHqdib8/aFTtmclrh2uI9ZUq0ZY2TLf1Xq0bGa2jdYZzntcR6yo9BvgyRr+U+jEOisM5z2uI9ZUeg1WGGSMtaqVaRkmp2yla5zcmnIrOy4aMsvtAZBYh0VhnOe1xHrKj0Gr9THr8vPf0NsxvdoB3WIdFYZzntcR6yo9AKz+3YJ+01wcMx4SPEbS4qg3VMFiPRWGc58XTRs5imva/lPY4mzlesMl2MavVvNGtvEKtcdB7TuF+SiVi0+wdP8KlW8lubuJWJy5kRhYZHk0v8Ltw5+WxR0JZBnwUkEtY6lTtecMncewniErCwoF8En2FBO2ZubVLUjl3IX4yP5UVaOLlCs2WwD7TQ+eT7KjYI2hoTzk0lU2+ZMNXg9ge3SVTOmcdjaqCcZjiiJIHfBUeJOHOM1+TZ8KTEnu5BkmMkndtuVWqiAffhxXuqzf8AFHYjkGYKs3Gxtyad1h0WqTX8dk+Nrxk4J+GxnlOSaM3AJmGxN47prAwZN9Fis2cb8VPAYHaSq1EzDUTso42xt0t7SZvlyEKN+tocPVdfrmKqM0QtHa4lDv5oVCzp/bd6blkQtyHFVofOky7ZzQ4ZFWaroT9KvfdH7X7hNuwu/lG3CP8AJTYkOEYTWvnf8lV4BA3IduQDsVLhzHbs2TsPmCFCf4UeGf7Co4mxDJo/vT//xAArEQABBAAEBAYDAQEAAAAAAAABAAIDBBESITEQIDNAExQiMkFhQlFScCP/2gAIAQIBAT8B/wBSgriQZin02huIPMKTcNSrFcRDEdrT6asTPzFnxzVpnudlKu+0drT6as9Q8HMc3cclTqK77R2tPpqz1ConBrwSrMzHNwCYQDiV/wAW+rf64VOorvtHa0+mrPUK8J+XNhpyGJ7RmIVTqK77R2tPpqz1CofVEAiMDgeDW5jgFaOEaqdRXfaOIjc7YJzS3fsaTt2q4zUOVabIcp2U1cSajdeTeooGxaqxN4h02VNm7lcdqG8K1cYZ3J9pjTgmSsm0ViHwziNuwjfkdmRDZW/SliMZwKZO9my8479J8z5N1DCZSiWxNT3FxzFAYnBWDkj04NJacQrHqjPYwTmPT4Xplan0x+JXknftMptHuRc2IaqaYyH646Txp8TmHUKGu55xOytvwbl7Jri3UJtx43ROAxTrbzsi4u1PJFMY9lFKJBiFNZEZyjdOcXnE9pGczAU5uU4c1ZuWMKd2aQntacn4FWocfWOWCHxDrsppPDb2wJBxChnEg+1LVDtW6I15B8IQSH4UdP8AtEtiapZTIce32TLjh7tULcZXmo/2n3P5Ce8vOLv90//EADUQAAEBBAYGCgIDAQAAAAAAAAECAAMQERIhIjFRcSAwQVBhchMjMjNCgYKRocFSYkCQsaL/2gAIAQEABj8C/stl0vw3efBbq1A6uTxYBbvPgtRS8r3TVtMjF3R2mWrKlXmLsqvluj1QVJQEmpqNJWzhqyp2qjPYwSTOqcHWW6PVB7mNank+4Ost0eqD3Ma1PJ9wdZbo9UHuYjNRkA0qR9mmLtQnk+4Ost0eqD3MRdp2E1wUDcDVBKkJSUHFphQHAsA5orVtZPSgBe2UU8n3B1luj1Qe5iNBXu3eCiwQi4QKVCYLWHhTmJtSNtXHQTyfcHWW6PVB7mNank+4Ost0eqD3MQCZUlFqCk0VbOOmUO0UpXlpgSIvEE8n3B1luj1Qe5iByDOubTmz08BBPJ9wdZboyMHoyMHbz0tNkrHnokeJVQgVfkYSwEHXLo21gN2j7N3gGbVbgeDhBP7VQUjG5ilQkQ2KDeGm7MxGazXg1JXkMGShO1glNwg8XiWAG1gMIzNQai4spx2nQsHy2NVUoXjcC0YGpphkrG2FN33n+tJQkWmkkHg3bnmG7csmmTNqKBMtis3mBl2lVCAOxNeh0Kbh2oUn1kYN3fy3VzQWorYLTeGStNx/nh6NlRhRV2FfEbYrxDdWoKHs3dKbuz5t1qpcA0naZQKlGQDUvDsEJntKriTgylG8lqarkaBHiFYgtGFf88g3FpeE3GHRvezsODTF2mVLMgGopqdj5hTV3afnQe8pgrm0VyxZfLuCgr3waivyOMLNafxLWrB4tNJmIzWQBxaTq2fhpvD5QwQLywSgSA0FJxEmkdjKdnxXaCl+0Hi/LcNFYmGmm0jGNkkZN3q/du9X7tWZxpPrKcNpaigSA0ulSLKr84UX9R/JqniPdu3SOCWmaki4MEpEyWSgbNxzlRViGsELa07V7Qqay6V7N1qgnKtrKbWJ1BSsTSWmm07x0KKBMtSVW8Pxuh1za1TxKJK4QX0gmA0naQkboeD9iyVYGbTGsSnFUHquMt00tihBOKbJ1lEXIqgjja3TSHaRXCvsKqOrKtuxpslGN7S3VLwnswDp6eU6kqWZANSN2wQ6RXaV/m66J8ixSuowoP6x+TTQQRw0rRmrANNV2wQprHVj53bXUoXFqKxKE3ailusQFZVNWlYbxezWHajm1RoD9Y03tTv/AFgAJAbuorEw03JpDBpKBB46VhNWLUnltW8pLSFZtUCnItZen2bvv+WtLUWqdjzr/tw//8QAKxAAAQIDBgcBAAMBAAAAAAAAAQARECExQVBRYXGRIDCBobHB8NFAkOHx/9oACAEBAAE/If7LCWRQQPkS+P5IgOi15eVCKlfH8kGBE6AgjzdLBs+hONtoJaHlSEyIY5nMZ3xTXRQ0e4Hmk5hSa/o5YTO0IOEBK8JLqWlo988HavK6lpaPfPB2ryupaWj3wgNBqomxDuE5kIFBKhHI7V5XUtLR74QDhXbIVWGIKeUFhqgE4tJMIiX1ABEAgDhYj2ryupaWj3wgIXRaMCFR0yG1mYwHyqgngIwQH3A0Njg7V5XUtLR754O1eV1LS0e+ABIFabBYq3NLjBIUCFgiiAR2ryupaWj3wA6wh7HAN5cRAHNEbuJyg6Ah2ryupXHJPB8kDMvKBIAdxNVSjRgeEYIyYT61haYx5MGDkPDXfwJnsg7XQ1IRsKA8QIyuBj5l5GtYNY0+fECBrUwKIQkiESy+AQmvfBEoyAalH+mhq9DgLSpYgmEMIJGlEWqibdDA0ACJSUAqSjAnzNES5JLvWL9O24nLcBOCGwKIKoGPVVSgmMDAcmABSkwEmKCceiVfBSQ7RkVFFiZoUeqsJAAAIGc75y4COdp+IAxiWzVAqDqSNPMhHI51BFDmEd3wEYFUsl/55K72sHcrVsyBeAvo5RXSrGZVk2k1StsEUXGPVTWma0wDymiUUTMllQacXZRAXoBKtwUUMoKXU8AmPyMHHts6/wA8JbgYhEOehNAfCkJFBK3jCRqiVPhYIJK+xwfOwh3/AMDhaLA8DdfFmLgJejaJEAICttgQ4A8undBABLRHMRRMnYC1MGywIWm/4EKzCIcACVDIc9RwPRZjfVwEZUS1I5rXBbcJl6QiBew1RqInXO5l7acqz02Iq5iznAZJqc+RkhMCiA4zCE4EO9UaB0RIoW5Q0qgmoLxZCrPElZNJnE3IhM6BbFUh5vI3gBIBxK7kSwd1SQwmKZPao8i0iAIqEdwNeB7vLXrCNFzym9ZhdjHNDYG05LYOcAhllqAXRlf5FiZ5CBYB5n2E0GsYGy6XLEtoT0eZmEButhmDzfC6XL8BbBgBt0DAocoNqZDiURITuZoR6FsQGEgLpIdPIY34hKmlCeOTaBEVSwMsKD6LDyuseyzXAKAUz84g2OURaGqEjXbNxHw3GVPoUbIf7+N3FJKtUsOkHKTKh1CCNmCPCQEcdYg3AykAdxuja7vAqAGsBagmwDAXcy3nLriVUc4NBxF5+ZIJu9KLyawsjr5jtVZxqJQx/fVA9ikpoYUD+2//2gAMAwEAAgADAAAAEPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPL3PPPPPLnfPPPPPPPPPPPPPPPPPPPFfvPPPPLfvPPPPPPPPPPPPPPPPPPPFfOdfPPPPvPPPPPPPPPPPPPPPPPPPFfOeOgPvPvPPPPPPPPPPPPPPPPPPPFfPvPPOvPvPPPPPPPPPPPPPPPPPPPFf8AvXz0/r/zzDLzzzzzzzzzzzzzzzxBZpzRcd27w1znvzzzzzzzzzzzzzzy/wD6+9/t59o68v088888888888888885ms2FxX8vCx9K888888888888888888NFvf8APPKv9ffPPPPPPPPPPPPPPPPPPFNfPPPPNFfPPPPPPPPPPPPPPPPPPPOjvPPPO2HPPPPPPPPPPPPPPPPPPPPPGTdPuHbPPPPPPPPPPPPPPPPPPPPPPPv99oTvPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP/xAAoEQEAAgACCQUBAQAAAAAAAAABABEhMUBBUWFxobHB0SCBkeHwEHD/2gAIAQMBAT8Q/wBSImcLb/EHlS1hev3fUlUVvvyQYAmOGi9AiFt9k9SGpY/VOT7mi9AnUdWKBbLeprZ6M7iTk+5ovQJ1HVj3UpK90D5ivYhjbBtB6EqC7mdxJyfc0XoE6jqxG70DW1GdxJyfc0Uch3jv3erAsb3eAGwf5llBEVst5fcVe4h+Puf1yhHiQyweDoL2HB6neAszMTvHAe4eSYM7jWftkarpvh9wECtQNcRd5bt3mBqJi9oi2tr4+/5drQZvaXTQdsMLDeS7yvM2+dA1jHXVLlkn72mv1rNZG81tMGF2fl4mP421xZYXFZH7VKUYp+9iZZZFI1DDzTN+Mev8QDgxjm80HLAee5mIbD97kHrijD66TCz8oRQb2b45Sl2TN8zbVZvY/Y/xAUzMpm+SVpcHONrDyrVxiXMup0KjJN8XtOaCy5tRi28hylUqN3o1MjJ/ZkuveuBa3NDQ0GiJsZ+yFlpL9QtMjD4+4iGdX846KoOA9ntDd+Dk9vfr6bSY8t2/xvjg8s3hDDRXZ2MuRi8nzBgOoefeHavHD6hto6ylmtr2JTjE/fEz7Lm7dHBQsjlttmZ5IrgDwfNRTjX3Oyxlv2Tz9TCuP90//8QAJxEAAgECBQMFAQEAAAAAAAAAAAERITFAQVGhsSBxwRBhgZHRcOH/2gAIAQIBAT8Q/qU7X8Dk1K1j86oqafaPwQG0euF5GKKPqIL5UM3XjC8jOHwKtCAmU9FnszdeMLyM4fBbSRO+XwJaZWgk6p9jljcuSz2ZuvGF5GcPgToegWaAs9mbrxheRix8XAy9ojwMdcXo5ecIetYQl3szdePWtMfwUJWsCmp3HpHOgvOWxV6ORVuhTNy9RUNgek3Oi8k6mXn0gEy3ZGZj9hNH6Zqy2wD1JkQG7GRjJimGpoynZv8ApeNNCCK2bJHZIa7jIjUPbbl6JF5Cpp98CxlVhOkqhtzBuVLNxpLTwZdI08ll6Jw5R9xfTIYCimAmNd4JvLDLBT2JjQUREhxLL6HeZaEDoGhE7B8fLwidYQ575dUMd3X7IC14wtF9xD6Wqv0ulsXFPavlhlplUQZ00fg/uNjXXYYQnGTLfC/SfOiRM1slh020q4phYbl8bXwNf+GJt934Swn+6f/EACsQAQABAQUIAgIDAQAAAAAAAAERABAhMUFRIFBhcYGRofAwscHRQJDx4f/aAAgBAQABPxD+ywAqwFRMjNJ3CuGrRc3EuDo/GIR9L2CuGrZpqR+zdKdYGXIvxart6j0PxNaJQuKSW+7rawdyLWGN0er0sTd2c1R5+iR8cXZU1o17nRxT8WeL3R7PSx7jR+eD4vdHs9LHuNH54Pi90ez0se40bSGZK4KJuTjqCdaUSJ8UHxe6PZ6WPcaNuMJHYiyGtWOFhl8IoToahp27Tp0mbsKfCag+FJBabMHxe6PZ6WPcaNumALF1cnEhz2q60WLFatg4Gh8GnXqoXlP3wHcptQfF7o9npY9xo/PB8Xuj2elj3GjY1I2LAKvKkuJbZIgdwylhRxsnYg+L3R7PSx7jRsYjzVDtUQpWAStcctCum2BB8XuiVmNhRa0sEMLj6KnhDwahQhjuJswZHD9ti5xvQusB7Dnt2SdkqMqTO1fSsHE0L3JoHzaKOeCpHcDBpAOC59WAgg3vw82LhUJ0HBpJwS8mkhVPAOfRnwJbIDZfe3XLXGA0rERVEBuBnwLAExf1vFFfJTzph+t6FoDTlEAU0AC8+Ciu0ry2xPs7v6NMYMRZbgf/AGwimlhwdKQ2EDvJYoHoYaNPcuRwlCD+dGhI5E0c9j1ZAcXk0hrKFesPwOFjCRN+RsYxIz57C6Hg4jlYFzUmJ5tRg42Z90iLJQvbqCHwg0QtmoH0g1qfzwVj9BbD/wBAYoAIiODZg3BkGkloIvwV74UJIvTjQaDdXuqLocXfzmwQgS8qdcOTLLiwjPRkW4mfelIFLue1JInHh2B4UVDgw62MmweV/PC3cdmNYvuYiyUpN4x4GhYPIGRNvkZsVXxWuzcTYpHVz7OxIh91Z17s0T8PltPC7gCFxvLqCvqTg1LE41fAVcI3E8MFYNCEkt4VPIoJCGNO6r1UYV3LLPdl9ygeDgMtjHSB1Iov4YejRiYIcjsI6RQavCkrNGH5H2O4Qb4g5rUaHS1KcQ3KVhvWKXEDm04zlcrArFGj0wJY0ZLaeAeN/wBUqsFgmJTnpYHuDKg2rh+6lM5Pea4cxYUUcmCVCK/kh3Gwyrwu5Sap6/W294A1rwCvbKs6GCPoRyJY/a/AOAMPWVMY2GAXsqT+CXvg3OKVSGSnHy6SBUQhfosMkGAxXCxruhAsKhDcF2yogklI/IGdl2DZ/kJt0pko67jYQAfAYeI+QVSZSwjDnr6eN0mAjcfbSwqyKFPxXE2uqwp5Lm1ruhpnaCCCgNDdIBEEcSlHZ9w/SwWHczcfH4QrAoSdilY0ZDd9Mcd1xT/tVSR0w2ATeLX5FYPVWg7Vwhrmnq0qcXJqywGluN2kBhgr6eFN3WWR1VnPGug5iWyruTSH3Q8zkqjjvC/eiXSC5QlUkrOEoIrgpEGAbucPuWTlTr01nrQBvi0dolNz3b1pRBx/Eby4HZGkkreman0ef8SkzxyvN3/3UI/tv//Z');	       

INSERT INTO patient (name, DNI_NIF, mobile_phone, landline, birthday, hist_numsergas, postal_address, location, email, allergies, diseases, interventions, family_background, smoker, menarche, menopause,
   last_menstruation_date, discharge_date, pregnancies, childbirths, cesarean_sections, misbirths, menstrual_type, user_id, enabled)
    VALUES ('Maria Carmen Silvestre','56487921L','679852456','981233256','1990-12-28','181212APAP1010','Cuesta Lorem ipsum dolor, 21B 5ºE','Rachaelbury','m.carmen@example.net',
    		'Alergia al tomate y los frutos secos','Diarrea crónica','Perforación de colon, apendicitis aguda','Diabetes por parte del padre',1,12,53,'2002-04-23','2002-02-05 22:11:50',1,1,0,0,'Dismenorrea primaria',2,1),
    	   ('Jacinta Barroso','78978955P','654321000','981885566','1997-02-07','121416ABCD1011','Alameda Lorem ipsum dolor sit, 258A','Cambre','jacinta.barroso@example.net',
    		'Alergia al polen','Asma','Colecistitis aguda','Diabetes por parte del padre, cáncer de mama por parte de abuela',0,10,48,'2008-11-23','2008-01-05 15:20:54',1,0,1,0,'Dismenorrea primaria',4,1),
    	   ('Elvira Alemany','14785236M','654889977','981777777','1996-12-24','222115EFGH2222','Urbanización Lorem ipsum, 97B 3ºA','Gijón','elviralemany@example.net',
    		'Alergia a la lactosa','Diabetes, hipertensión','Traumatismos abdominales','COVID-19 por parte del tío abuelo, escoliosis por parte del padre',0,12,50,'2011-11-11','2012-08-06 12:13:28',1,2,3,4,'Amenorrea secundaria',3,1),
    	   ('Maria Consolacion Pereira','48844884K','679723722','981555555','1998-04-16','112233MNBV1234','Pasadizo Lorem ipsum dolor, 12 7ºB','Torremolinos','mpereira@example.net',
    		'Alergia al pelo de gato','Escoliosis','Obstrucción intestinal por tumores','',1,11,55,'2020-12-11','2016-06-05 16:47:51',1,1,1,1,'Amenorrea primaria',3,1),
    	   ('Naima Abellan','77755522W','630103924','981012345','1984-01-04','123456MAMI9876','Plaza Lorem, 105A 5ºH','Despeñaperros','naima.abellan@example.net',
    		'Alergia a los frutos secos','Párkinson, esquizofrenia','Abdomen agudo no filiado','',1,13,46,'2021-01-01','2019-10-11 22:11:50',0,0,0,0,'Dismenorrea primaria',5,1);

INSERT INTO patient_contraceptive (patient_id, contraceptive_id)
VALUES (1, 1),
	   (1, 3),
	   (1, 4),
	   (2, 1),
	   (2, 2),
	   (2, 3),
	   (2, 4),
	   (4, 1),
	   (4, 7),
	   (5, 6);
	   
INSERT INTO meeting(activity, comments, meeting_date, patient_id, user_id)
	VALUES ('Primera cita', '<p>Primera cita para la paciente</p>', '2002-02-05 14:16:00', 1, 1),
		   ('Segunda cita', '<p>Segunda cita para la paciente</p>', '2003-02-05 14:16:00', 1, 2),
		   ('Tercera cita', '<p>Tercera cita para la paciente</p>', '2004-02-05 14:16:00', 1, 3),
		   ('Cuarta cita', '<p>Cuarta cita para la paciente</p>', '2005-02-05 14:16:00', 1, 4),
		   ('Primera cita', '<p>Primera cita para la paciente</p>', '2002-02-05 16:30:00', 2, 1),
		   ('Segunda cita', '<p>Segunda cita para la paciente</p>', '2003-02-05 14:16:00', 2, 2),
		   ('Tercera cita', '<p>Tercera cita para la paciente</p>', '2004-02-05 14:16:00', 2, 3);

INSERT INTO question(question)
	VALUES ('¿Es activa sexualmente?'),
		   ('¿Cuándo fue su última relación sexual?'),
		   ('¿Utiliza métodos anticonceptivos?'),
		   ('¿Realiza prácticas sexuales de riesgo?'),
		   ('¿Ha tenido algún tipo de ETS?'),
		   ('¿Cuándo se realizó la última citología?');
		
INSERT INTO answer(answer, question_id, meeting_id)
	VALUES ('Si', 1, 1),
		   ('Hace 3 días', 2, 1),
		   ('No', 3, 1),
		   ('Por suerte no', 5, 2),
		   ('Si', 3, 2),
		   ('Si', 1, 3),
		   ('Hace 3 días', 2, 3),
		   ('No', 3, 3),
		   ('Por suerte no', 5, 3),
		   ('No', 3, 4),
		   ('Hace 2 años', 6, 4),
		   ('No', 3, 5);

INSERT INTO complementary_test (id, file_name, file_type, diagnostic_test_id, meeting_id, data)
	VALUES (UUID(), 'prueba_complementaria_1', 'application/octet-stream', 1, 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sollicitudin risus non turpis luctus, at interdum tortor tristique. Duis consequat facilisis felis, sit amet aliquam enim suscipit vel. Quisque egestas pulvinar faucibus. Aliquam malesuada vel ligula at dictum. Donec non nisl vel odio pulvinar porta sed eget elit. Phasellus sem lacus, pretium ut fermentum sed, porttitor quis leo. Fusce efficitur eu ipsum ut fringilla. Cras eros enim, dapibus eget tempus eget, placerat quis justo. Aenean nec lobortis diam, accumsan accumsan ipsum.'),
		   (UUID(), 'prueba_complementaria_2.txt', 'text/plain', 5, 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sollicitudin risus non turpis luctus, at interdum tortor tristique. Duis consequat facilisis felis, sit amet aliquam enim suscipit vel. Quisque egestas pulvinar faucibus. Aliquam malesuada vel ligula at dictum. Donec non nisl vel odio pulvinar porta sed eget elit. Phasellus sem lacus, pretium ut fermentum sed, porttitor quis leo. Fusce efficitur eu ipsum ut fringilla. Cras eros enim, dapibus eget tempus eget, placerat quis justo. Aenean nec lobortis diam, accumsan accumsan ipsum.'),
		   (UUID(), 'prueba_complementaria_3.txt', 'text/plain', 7, 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sollicitudin risus non turpis luctus, at interdum tortor tristique. Duis consequat facilisis felis, sit amet aliquam enim suscipit vel. Quisque egestas pulvinar faucibus. Aliquam malesuada vel ligula at dictum. Donec non nisl vel odio pulvinar porta sed eget elit. Phasellus sem lacus, pretium ut fermentum sed, porttitor quis leo. Fusce efficitur eu ipsum ut fringilla. Cras eros enim, dapibus eget tempus eget, placerat quis justo. Aenean nec lobortis diam, accumsan accumsan ipsum.'),
		   (UUID(), 'prueba_complementaria_4.txt', 'text/plain', 2, 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sollicitudin risus non turpis luctus, at interdum tortor tristique. Duis consequat facilisis felis, sit amet aliquam enim suscipit vel. Quisque egestas pulvinar faucibus. Aliquam malesuada vel ligula at dictum. Donec non nisl vel odio pulvinar porta sed eget elit. Phasellus sem lacus, pretium ut fermentum sed, porttitor quis leo. Fusce efficitur eu ipsum ut fringilla. Cras eros enim, dapibus eget tempus eget, placerat quis justo. Aenean nec lobortis diam, accumsan accumsan ipsum.'),
		   (UUID(), 'prueba_complementaria_5', 'application/octet-stream', 4, 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sollicitudin risus non turpis luctus, at interdum tortor tristique. Duis consequat facilisis felis, sit amet aliquam enim suscipit vel. Quisque egestas pulvinar faucibus. Aliquam malesuada vel ligula at dictum. Donec non nisl vel odio pulvinar porta sed eget elit. Phasellus sem lacus, pretium ut fermentum sed, porttitor quis leo. Fusce efficitur eu ipsum ut fringilla. Cras eros enim, dapibus eget tempus eget, placerat quis justo. Aenean nec lobortis diam, accumsan accumsan ipsum.'),
		   (UUID(), 'prueba_complementaria_6', 'application/octet-stream', 8, 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sollicitudin risus non turpis luctus, at interdum tortor tristique. Duis consequat facilisis felis, sit amet aliquam enim suscipit vel. Quisque egestas pulvinar faucibus. Aliquam malesuada vel ligula at dictum. Donec non nisl vel odio pulvinar porta sed eget elit. Phasellus sem lacus, pretium ut fermentum sed, porttitor quis leo. Fusce efficitur eu ipsum ut fringilla. Cras eros enim, dapibus eget tempus eget, placerat quis justo. Aenean nec lobortis diam, accumsan accumsan ipsum.'),
		   (UUID(), 'prueba_complementaria_7', 'application/octet-stream', 8, 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sollicitudin risus non turpis luctus, at interdum tortor tristique. Duis consequat facilisis felis, sit amet aliquam enim suscipit vel. Quisque egestas pulvinar faucibus. Aliquam malesuada vel ligula at dictum. Donec non nisl vel odio pulvinar porta sed eget elit. Phasellus sem lacus, pretium ut fermentum sed, porttitor quis leo. Fusce efficitur eu ipsum ut fringilla. Cras eros enim, dapibus eget tempus eget, placerat quis justo. Aenean nec lobortis diam, accumsan accumsan ipsum.'),
		   (UUID(), 'prueba_complementaria_8.txt', 'text/plain', 7, 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sollicitudin risus non turpis luctus, at interdum tortor tristique. Duis consequat facilisis felis, sit amet aliquam enim suscipit vel. Quisque egestas pulvinar faucibus. Aliquam malesuada vel ligula at dictum. Donec non nisl vel odio pulvinar porta sed eget elit. Phasellus sem lacus, pretium ut fermentum sed, porttitor quis leo. Fusce efficitur eu ipsum ut fringilla. Cras eros enim, dapibus eget tempus eget, placerat quis justo. Aenean nec lobortis diam, accumsan accumsan ipsum.'),
		   (UUID(), 'prueba_complementaria_9', 'application/octet-stream', 6, 4, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sollicitudin risus non turpis luctus, at interdum tortor tristique. Duis consequat facilisis felis, sit amet aliquam enim suscipit vel. Quisque egestas pulvinar faucibus. Aliquam malesuada vel ligula at dictum. Donec non nisl vel odio pulvinar porta sed eget elit. Phasellus sem lacus, pretium ut fermentum sed, porttitor quis leo. Fusce efficitur eu ipsum ut fringilla. Cras eros enim, dapibus eget tempus eget, placerat quis justo. Aenean nec lobortis diam, accumsan accumsan ipsum.');

INSERT INTO patients_of_interest (user_id, patient_id)
	VALUES (1, 1),
		   (1, 2),
		   (1, 3),
		   (1, 5),
		   (3, 1),
		   (3, 4);
		   
INSERT INTO calendar_entry (entry_date, state, reason, user_id, patient_id) 
	VALUES ('2002-02-05 14:00:00', 'closed', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 1),
		   ('2003-02-05 15:30:00', 'closed', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 2),
		   ('2005-02-05 19:30:00', 'closed', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 3),
		   ('2006-02-05 16:00:00', 'cancelled', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 4),
		   ('2007-02-05 17:00:00', 'cancelled', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 5),
		   (DATE_ADD(DATE(NOW()), INTERVAL '0 09:00' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 3),
		   (DATE_ADD(DATE(NOW()), INTERVAL '0 12:00' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 4),
		   (DATE_ADD(DATE(NOW()), INTERVAL '0 17:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 5),
		   (DATE_ADD(DATE(NOW()), INTERVAL '1 20:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 3),
		   (DATE_ADD(DATE(NOW()), INTERVAL '1 15:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 4),
		   (DATE_ADD(DATE(NOW()), INTERVAL '1 18:00' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 2),
		   (DATE_ADD(DATE(NOW()), INTERVAL '1 12:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 5),
		   (DATE_ADD(DATE(NOW()), INTERVAL '2 20:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 3),
		   (DATE_ADD(DATE(NOW()), INTERVAL '3 15:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 4),
		   (DATE_ADD(DATE(NOW()), INTERVAL '4 18:00' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 2),
		   (DATE_ADD(DATE(NOW()), INTERVAL '5 12:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 1, 5),
		   ('2002-02-05 14:00:00', 'closed', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 2, 1),
		   ('2003-02-05 15:30:00', 'closed', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 3, 2),
		   ('2005-02-05 19:30:00', 'closed', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 4, 3),
		   ('2006-02-05 16:00:00', 'cancelled', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 5, 4),
		   ('2007-02-05 17:00:00', 'cancelled', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 2, 5),
		   (DATE_ADD(DATE(NOW()), INTERVAL '0 09:00' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 3, 3),
		   (DATE_ADD(DATE(NOW()), INTERVAL '0 12:00' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 4, 4),
		   (DATE_ADD(DATE(NOW()), INTERVAL '0 17:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 5, 5),
		   (DATE_ADD(DATE(NOW()), INTERVAL '1 20:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 2, 3),
		   (DATE_ADD(DATE(NOW()), INTERVAL '1 15:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 3, 4),
		   (DATE_ADD(DATE(NOW()), INTERVAL '1 18:00' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 4, 2),
		   (DATE_ADD(DATE(NOW()), INTERVAL '1 12:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 5, 5),
		   (DATE_ADD(DATE(NOW()), INTERVAL '2 20:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 2, 3),
		   (DATE_ADD(DATE(NOW()), INTERVAL '3 15:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 3, 4),
		   (DATE_ADD(DATE(NOW()), INTERVAL '4 18:00' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 4, 2),
		   (DATE_ADD(DATE(NOW()), INTERVAL '5 12:30' DAY_MINUTE), 'opened', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac lectus et mauris tempor consectetur. Sed vitae lacus volutpat mauris egestas fermentum. In hac habitasse platea dictumst. Quisque viverra ipsum ac velit ultrices, vel luctus enim viverra. ', 5, 5);
		   
INSERT INTO recipe (dispensing_date, clarifications, meeting_id)
	VALUES ('2002-02-08', '<p>Lorem ipsum dolor sit amet, consectetur tincidunt. </p>', 1),
		   ('2002-05-13', '<p>Lorem ipsum dolor sit amet, consectetur tincidunt. </p>', 1),
		   ('2002-07-26', '<p>Lorem ipsum dolor sit amet, consectetur tincidunt. </p>', 1),
		   ('2002-02-08', '<p>Lorem ipsum dolor sit amet, consectetur tincidunt. </p>', 5),
		   ('2004-02-08', '<p>Lorem ipsum dolor sit amet, consectetur tincidunt. </p>', 7);
		   

INSERT INTO recipe_medicine (recipe_id, medicine_id, denomination, dosification, form_of_administration, format, units, posology)
	VALUES (1, 1, 'Pfizer', '20 mg/dia', 'Vía digestiva o enteral', 3, 2, '2 cada 12 horas'),
		   (1, 3, 'Moderna', '3 mg/ciclo', 'Vía respiratoria', 2, 6, '1-0-1'),
		   (1, 8, 'Moderna', '16 cg', 'Vía transdérmica', 3, 3, '1 cada 8 horas'),
		   (2, 2, 'Johnson & Johnson', '10 mg/dia', 'Vía digestiva o enteral', 3, 2, '1 cada 4 horas'),
		   (3, 5, 'Sanofi', '1 mg/ciclo', 'Vía respiratoria', 2, 6, '2-0-1'),
		   (3, 6, 'AbbVie', '32 cg', 'Vía transdérmica', 3, 3, '3 cada 12 horas');
		   
INSERT INTO message (subject, message_body, message_date, message_read, sender_id, receiver_id, replied_message_id, interconsultation_meeting_id)
	VALUES ('Mensaje 1', '<p>Este es el mensaje 1.</p>', '2021-02-07 12:32:54', 1, 1, 2, null, null),
		   ('Mensaje 2', '<p>Este es el mensaje 2.</p>', '2021-02-07 15:48:00', 1, 2, 1, 1, null),
		   ('Mensaje 3', '<p>Este es el mensaje 3.</p>', '2021-02-08 12:32:54', 1, 1, 2, 2, null),
		   ('Mensaje 4', '<p>Este es el mensaje 4.</p>', '2021-02-08 16:03:11', 0, 2, 1, 3, null),
		   ('Mensaje 5', '<p>Este es el mensaje 5.</p>', '2021-03-01 15:00:25', 1, 2, 3, null, null),
		   ('Mensaje 6', '<p>Este es el mensaje 6.</p>', '2021-03-02 12:32:54', 0, 3, 2, 5, null),
		   ('Mensaje 7', '<p>Este es el mensaje 7.</p>', '2021-03-10 10:52:54', 1, 3, 4, null, null),
		   ('Mensaje 8', '<p>Este es el mensaje 8.</p>', '2021-03-10 18:45:01', 1, 4, 3, 7, null),
		   ('Mensaje 9', '<p>Este es el mensaje 9.</p>', '2021-03-11 09:05:16', 0, 3, 4, 8, null),
		   ('Mensaje de interconsulta 1', '<p>Esto es un mensaje de interconsulta de prueba numero 1.</p>', '2021-03-11 13:33:11', 1, 1, 2, null, 2),
		   ('Mensaje de interconsulta 2', '<p>Esto es un mensaje de interconsulta de prueba numero 2.</p>', '2021-03-11 15:20:03', 0, 2, 1, null, 1),
		   ('Mensaje de interconsulta 3', '<p>Esto es un mensaje de interconsulta de prueba numero 3.</p>', '2021-03-12 12:02:48', 1, 3, 1, null, 3);

INSERT INTO common_task (title, description)
	VALUES ('Tarea 1', '<p>Descripción de la tarea 1.</p>'),
		   ('Tarea 2', '<p>Descripción de la tarea 2.</p>'),
		   ('Tarea 3', '<p>Descripción de la tarea 3.</p>'),
		   ('Tarea 4', '<p>Descripción de la tarea 4.</p>'),
		   ('Tarea 5', '<p>Descripción de la tarea 5.</p>');

INSERT INTO common_task_user (user_id, common_task_id, last_time_read)
	VALUES (1, 1, '2021-03-20 09:32:14'),
		   (1, 2, '2021-03-20 10:00:00'),
		   (1, 3, '2021-03-20 10:02:00'),
		   (1, 4, '2021-03-20 10:50:00'),
		   (1, 5, '2021-03-20 12:15:00'),
		   (2, 2, '2021-03-20 10:00:00'),
		   (2, 3, '2021-03-20 10:02:00'),
		   (2, 4, '2021-03-20 10:50:00'),
		   (3, 3, '2021-03-20 10:02:00'),
		   (3, 4, '2021-03-20 10:50:00'),
		   (4, 1, '2021-03-20 10:50:00');

INSERT INTO grupal_message (message_body, datetime, common_task_id, user_id)
	VALUES ('<p>Mensaje 1 en tarea 1.</p>', '2021-03-21 09:32:14', 1, 4),
		   ('<p>Mensaje 2 en tarea 1.</p>', '2021-03-21 10:32:14', 1, 4),
		   ('<p>Mensaje 3 en tarea 1.</p>', '2021-03-21 11:32:14', 1, 4),
		   ('<p>Mensaje 4 en tarea 1.</p>', '2021-03-21 12:32:14', 1, 4),
		   ('<p>Mensaje 5 en tarea 1.</p>', '2021-03-21 13:32:14', 1, 4),
		   ('<p>Mensaje 6 en tarea 1.</p>', '2021-03-21 14:32:14', 1, 4),
		   ('<p>Mensaje 1 en tarea 2.</p>', '2021-03-21 09:32:14', 2, 4),
		   ('<p>Mensaje 2 en tarea 2.</p>', '2021-03-21 09:34:14', 2, 3),
		   ('<p>Mensaje 3 en tarea 2.</p>', '2021-03-21 09:36:14', 2, 2),
		   ('<p>Mensaje 4 en tarea 2.</p>', '2021-03-21 09:38:14', 2, 3),
		   ('<p>Mensaje 5 en tarea 2.</p>', '2021-03-21 09:40:14', 2, 4);

INSERT INTO notice (notice, datetime, user_id)
	VALUES ('Aviso 1.', DATE_ADD(DATE(NOW()), INTERVAL '0 09:00' DAY_MINUTE), 2),
		   ('Aviso 2.', DATE_ADD(DATE(NOW()), INTERVAL '0 09:00' DAY_MINUTE), 4),
		   ('Aviso 3.', DATE_ADD(DATE(NOW()), INTERVAL '0 09:00' DAY_MINUTE), 3),
		   ('Aviso 4.', DATE_ADD(DATE(NOW()), INTERVAL '0 09:00' DAY_MINUTE), 5),
		   ('Aviso 5.', DATE_ADD(DATE(NOW()), INTERVAL '0 09:00' DAY_MINUTE), 2),
		   ('Aviso 6.', DATE_ADD(DATE(NOW()), INTERVAL '-1 09:00' DAY_MINUTE), 1),
		   ('Aviso 7.', DATE_ADD(DATE(NOW()), INTERVAL '-1 12:30' DAY_MINUTE), 1),
		   ('Aviso 8.', DATE_ADD(DATE(NOW()), INTERVAL '-2 16:00' DAY_MINUTE), 3),
		   ('Aviso 9.', DATE_ADD(DATE(NOW()), INTERVAL '-3 18:24' DAY_MINUTE), 4),
		   ('Aviso 10.', DATE_ADD(DATE(NOW()), INTERVAL '-4 19:38' DAY_MINUTE), 5);





	       