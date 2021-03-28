package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Contraceptive;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.ContraceptiveDao;
import es.udc.fic.ginecologia.repository.RoleDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ContraceptiveServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private ContraceptiveService contraceptiveService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private ContraceptiveDao contraceptiveDao;

	private User createUser(String name, String username, String email, String postalAddress, String location,
			String DNI, String phoneNumber, String collegiateNumber) {
		User user = new User(name, username, email, postalAddress, location, DNI, phoneNumber, collegiateNumber);
		user.setPassword(username);
		return user;
	}

	private Role createRole(String name) {
		return new Role(name);
	}
	
	@Test
	public void findAllContraceptivesTest() {

		Contraceptive contraceptive1 = new Contraceptive("contraceptive1");
		Contraceptive contraceptive2 = new Contraceptive("contraceptive2");
		Contraceptive contraceptive3 = new Contraceptive("contraceptive3");
		Contraceptive contraceptive4 = new Contraceptive("contraceptive4");

		contraceptiveDao.save(contraceptive1);
		contraceptiveDao.save(contraceptive2);
		contraceptiveDao.save(contraceptive3);
		contraceptiveDao.save(contraceptive4);

		Iterable<Contraceptive> result = contraceptiveService.findAllContraceptives();
		Iterable<Contraceptive> expectedResult = Arrays.asList(contraceptive1, contraceptive2, contraceptive3,
				contraceptive4);

		assertEquals(expectedResult, result);
	}
	
	@Test
	public void findAllActiveContraceptivesTest() {

		Contraceptive contraceptive1 = new Contraceptive("contraceptive1");
		Contraceptive contraceptive2 = new Contraceptive("contraceptive2");
		Contraceptive contraceptive3 = new Contraceptive("contraceptive3");
		contraceptive3.setEnabled(false);
		Contraceptive contraceptive4 = new Contraceptive("contraceptive4");

		contraceptiveDao.save(contraceptive1);
		contraceptiveDao.save(contraceptive2);
		contraceptiveDao.save(contraceptive3);
		contraceptiveDao.save(contraceptive4);

		Iterable<Contraceptive> result = contraceptiveService.findAllActiveContraceptives();
		Iterable<Contraceptive> expectedResult = Arrays.asList(contraceptive1, contraceptive2, contraceptive4);

		assertEquals(expectedResult, result);
	}

	@Test
	public void addContraceptiveTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		contraceptiveService.addContraceptive(user.getId(), "newcontraceptive");

		Contraceptive contraceptive = contraceptiveDao.findByName("newcontraceptive").get();

		assertEquals("newcontraceptive", contraceptive.getName());
		assertTrue(contraceptive.isEnabled());
	}

	@Test
	public void addDiangosticTestTestExpectAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class,
				() -> contraceptiveService.addContraceptive(-1, "newcontraceptive"));
	}

	@Test
	public void addDiangosticTestTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class,
				() -> contraceptiveService.addContraceptive(user.getId(), "newcontraceptive"));
	}

	@Test
	public void addContraceptiveTestExpectDuplicateInstanceException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		contraceptiveService.addContraceptive(user.getId(), "newcontraceptive");

		assertThrows(DuplicateInstanceException.class,
				() -> contraceptiveService.addContraceptive(user.getId(), "newcontraceptive"));
	}
	
	@Test
	public void updateContraceptiveTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		contraceptiveService.addContraceptive(user.getId(), "newcontraceptive");

		Contraceptive contraceptive = contraceptiveDao.findByName("newcontraceptive").get();

		contraceptiveService.updateContraceptive(user.getId(), contraceptive.getId(), "updatedcontraceptive");

		contraceptive = contraceptiveDao.findByName("updatedcontraceptive").get();

		assertEquals("updatedcontraceptive", contraceptive.getName());
	}

	@Test
	public void updateContraceptiveTestExpectAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		contraceptiveService.addContraceptive(user.getId(), "newcontraceptive");

		Contraceptive contraceptive = contraceptiveDao.findByName("newcontraceptive").get();

		assertThrows(InstanceNotFoundException.class,
				() -> contraceptiveService.updateContraceptive(-1, contraceptive.getId(), "updatedcontraceptive"));
	}

	@Test
	public void updateContraceptiveTestExpectContraceptiveNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> contraceptiveService.updateContraceptive(user.getId(), -1, "updatedcontraceptive"));
	}

	@Test
	public void updateContraceptiveTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class,
				() -> contraceptiveService.updateContraceptive(user.getId(), 1, "updatedcontraceptive"));
	}

	@Test
	public void updateContraceptiveTestExpectDuplicateInstanceException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		contraceptiveService.addContraceptive(user.getId(), "newcontraceptive");

		Contraceptive contraceptive = contraceptiveDao.findByName("newcontraceptive").get();

		assertThrows(DuplicateInstanceException.class, () -> contraceptiveService.updateContraceptive(user.getId(),
				contraceptive.getId(), "newcontraceptive"));
	}

	@Test
	public void changeEnablingContraceptiveTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		contraceptiveService.addContraceptive(user.getId(), "newcontraceptive");

		Contraceptive contraceptive = contraceptiveDao.findByName("newcontraceptive").get();

		contraceptiveService.changeEnablingContraceptive(user.getId(), contraceptive.getId());

		assertFalse(contraceptive.isEnabled());

		contraceptiveService.changeEnablingContraceptive(user.getId(), contraceptive.getId());

		assertTrue(contraceptive.isEnabled());
	}

	@Test
	public void changeEnablingContraceptiveTestAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		contraceptiveService.addContraceptive(user.getId(), "newcontraceptive");

		Contraceptive contraceptive = contraceptiveDao.findByName("newcontraceptive").get();

		assertThrows(InstanceNotFoundException.class,
				() -> contraceptiveService.changeEnablingContraceptive(-1, contraceptive.getId()));
	}

	@Test
	public void changeEnablingContraceptiveTestContraceptiveNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> contraceptiveService.changeEnablingContraceptive(user.getId(), -1));
	}

	@Test
	public void changeEnablingContraceptiveTestPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class,
				() -> contraceptiveService.changeEnablingContraceptive(user.getId(), 1));
	}
	
	@Test
	public void findContraceptivesTest() throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		Contraceptive contraceptive1 = new Contraceptive("contraceptive1");
		Contraceptive contraceptive2 = new Contraceptive("contraceptive2");
		Contraceptive contraceptive3 = new Contraceptive("contraceptive3");
		Contraceptive contraceptive4 = new Contraceptive("contraceptive4");

		contraceptiveDao.save(contraceptive1);
		contraceptiveDao.save(contraceptive2);
		contraceptiveDao.save(contraceptive3);
		contraceptiveDao.save(contraceptive4);

		Iterable<Contraceptive> expectedResult = Arrays.asList(contraceptive1, contraceptive2, contraceptive3, contraceptive4);

		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Iterable<Contraceptive> result = contraceptiveService.findContraceptives(user.getId(), "contraceptive", true);

		assertEquals(expectedResult, result);
	}

	@Test
	public void findContraceptivesTestAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> contraceptiveService.findContraceptives(-1, "contraceptive", true));
	}

	@Test
	public void findContraceptivesTestPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class,
				() -> contraceptiveService.findContraceptives(user.getId(), "contraceptive", true));
	}
}
