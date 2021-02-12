package es.udc.fic.ginecologia.service;

import java.util.List;

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
	
	private User createUser (String name) {
		return new User(name);
	}
	
	@Test
	public void testFindAllUsers() {
		User user1 = createUser("user1");
		User user2 = createUser("user2");
		User user3 = createUser("user3");
		User user4 = createUser("user4");
		
		userDao.save(user1);
		userDao.save(user2);
		userDao.save(user3);
		userDao.save(user4);
		
		List<User> expectedUsers = Arrays.asList(user1, user2, user3, user4);
		
		List<User> foundUsers = userService.findAllUsers();
		
		assertEquals(expectedUsers, foundUsers);
		
	}

}
