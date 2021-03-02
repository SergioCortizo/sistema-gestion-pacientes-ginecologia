package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.UserDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	private User createUser (String name, String username, String email, String postalAddress, String location, String DNI,
			String phoneNumber, String collegiateNumber) {
		User user = new User(name, username, email, postalAddress, location, DNI, phoneNumber, collegiateNumber);
		user.setPassword(username);
		return user;
	}
	
	@Test
	public void testFindAllUsers() {
		User user1 = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A", "654789123", "122112345");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "22222222B", "123456789", "235088754");
		User user3 = createUser("User 3", "user3", "user3@example.com", "postalAddress 3", "location 3", "33333333C", "987654321", "010266666");
		User user4 = createUser("User 4", "user4", "user4@example.com", "postalAddress 4", "location 4", "44444444D", "554488997", "303156565");
		
		userDao.save(user1);
		userDao.save(user2);
		userDao.save(user3);
		userDao.save(user4);
		
		Iterable<User> expectedUsers = Arrays.asList(user1, user2, user3, user4);
		
		Iterable<User> foundUsers = userService.findAllUsers();
		
		assertEquals(expectedUsers, foundUsers);
		
	}

}
