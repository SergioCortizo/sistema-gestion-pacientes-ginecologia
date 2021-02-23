package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

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
	
	private User createUser (String name, String username, String email) {
		User user = new User(name, username, email);
		user.setPassword(username);
		return user;
	}
	
	@Test
	public void testFindAllUsers() {
		User user1 = createUser("User 1", "user1", "user1@example.com");
		User user2 = createUser("User 2", "user2", "user2@example.com");
		User user3 = createUser("User 3", "user3", "user3@example.com");
		User user4 = createUser("User 4", "user4", "user4@example.com");
		
		userDao.save(user1);
		userDao.save(user2);
		userDao.save(user3);
		userDao.save(user4);
		
		Iterable<User> expectedUsers = Arrays.asList(user1, user2, user3, user4);
		
		Iterable<User> foundUsers = userService.findAllUsers();
		
		assertEquals(expectedUsers, foundUsers);
		
	}

}
