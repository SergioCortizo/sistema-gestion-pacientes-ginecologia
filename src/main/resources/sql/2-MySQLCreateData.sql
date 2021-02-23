--Datos para user
INSERT INTO user (name, username, password, email, enabled) 
	VALUES ('User 1', 'user1', '$2y$10$UgB6dXrhUVwubG8O7lKFz.dzAh12obZGMzKOg1gR.YdWJW9pXmchW', 'user1@example.com', 1),
		   ('User 2', 'user2', '$2y$10$/ZMcrTQzB9RWYxLR52zDFOFqOJulWKr/w3WbowKn0vjfdvuiGT5l2', 'user2@example.com', 1),
		   ('User 3', 'user3', '$2y$10$oitT6WNJErvZDxDHNNwMbu7WPn5IfklOuaZdaDA7OVQvwKXg1BZmK', 'user3@example.com', 1),
		   ('User 4', 'user4', '$2y$10$aya2t4UFLfac8R/RS37LRupBaLCsL5DmUA4vTj/Ctf5a54pJ0WDb.', 'user4@example.com', 1),
		   ('User 5', 'user5', '$2y$10$BsgELu9MSqAZG65NE20nj.O7iz6weIUgXA8nwwE9tr4m/8aFg9Yc.', 'user5@example.com', 1);
		   
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