package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.RoleDao;
import es.udc.fic.ginecologia.repository.UserDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	private User createUser (String name, String username, String email, String postalAddress, String location, String DNI,
			String phoneNumber, String collegiateNumber) {
		User user = new User(name, username, email, postalAddress, location, DNI, phoneNumber, collegiateNumber);
		user.setPassword(username);
		return user;
	}
	
	private Role createRole(String name) {
		return new Role(name);
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
	
	@Test
	public void testFindAllRoles() {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");
		Role role3 = createRole("role3");
		Role role4 = createRole("role4");
		
		roleDao.save(role1);
		roleDao.save(role2);
		roleDao.save(role3);
		roleDao.save(role4);
		
		Iterable<Role> expectedRoles = Arrays.asList(role1, role2, role3, role4);
		
		Iterable<Role> foundRoles = userService.findAllRoles();
		
		assertEquals(expectedRoles, foundRoles);
	}
	
	@Test
	public void testRegisterUser() throws DuplicateInstanceException {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");
		
		roleDao.save(role1);
		roleDao.save(role2);
		
		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A", "654789123", "122112345");
		user.setPassword("password1");
		
		userService.registerUser(user, roles);
		
		User result = userDao.findByUsername("user1").get();
		
		assertEquals(result.getName(), user.getName());
		assertEquals(result.getUsername(), user.getUsername());
		assertEquals(result.getEmail(), user.getEmail());
		assertEquals(result.getPostalAddress(), user.getPostalAddress());
		assertEquals(result.getLocation(), user.getLocation());
		assertEquals(result.getDNI(), user.getDNI());
		assertEquals(result.getPhoneNumber(), user.getPhoneNumber());
		assertEquals(result.getCollegiateNumber(), user.getCollegiateNumber());
		assertEquals(user.getPassword(), result.getPassword());
		assertTrue(result.isEnabled());
	}

}
