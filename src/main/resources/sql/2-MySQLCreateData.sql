--Datos para user
INSERT INTO user (name, username, password, email, enabled, postal_address, location, DNI, phone_number, discharge_date, collegiate_number) 
	VALUES ('User 1', 'user1', '$2y$10$UgB6dXrhUVwubG8O7lKFz.dzAh12obZGMzKOg1gR.YdWJW9pXmchW', 'user1@example.com', 1, "Alameda Lorem ipsum dolor, 210A 5ºG", "A Coruña", "61861134N", "649383350", '2019-01-09 15:48:23', "253011225"),                                        
		   ('User 2', 'user2', '$2y$10$/ZMcrTQzB9RWYxLR52zDFOFqOJulWKr/w3WbowKn0vjfdvuiGT5l2', 'user2@example.com', 1, "Pasaje Lorem ipsum dolor sit, 275B", "Madrid", "88878494P", "880721492", '2020-10-09 16:30:12', NULL),
		   ('User 3', 'user3', '$2y$10$oitT6WNJErvZDxDHNNwMbu7WPn5IfklOuaZdaDA7OVQvwKXg1BZmK', 'user3@example.com', 1, "C. Comercial Lorem ipsum, 7A", "Granada", "83722842J", "779519883", '2020-11-21 12:00:48', "282998654"),
		   ('User 4', 'user4', '$2y$10$aya2t4UFLfac8R/RS37LRupBaLCsL5DmUA4vTj/Ctf5a54pJ0WDb.', 'user4@example.com', 0, "Pasadizo Lorem ipsum, 265A 5ºE", "Gijón", "54638037L", "626504814", '2021-01-20 17:54:47', NULL),
		   ('User 5', 'user5', '$2y$10$BsgELu9MSqAZG65NE20nj.O7iz6weIUgXA8nwwE9tr4m/8aFg9Yc.', 'user5@example.com', 1, "Vía Lorem, 209B", "Ourense", "88440299D", "714877694", '2021-01-30 15:48:23', "152755566");
		   
--Datos para role
INSERT INTO role (name) 
	VALUES ('ROLE_FACULTATIVE'), 
		   ('ROLE_ADMIN');

--Datos para userRole
INSERT INTO user_role (user_id, role_id)
	VALUES (1, 1),
		   (1, 2),
		   (2, 2),
		   (3, 1),
		   (4, 2),
		   (5, 1);