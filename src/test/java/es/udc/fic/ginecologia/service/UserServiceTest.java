package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.IncorrectPasswordException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.Schedule;
import es.udc.fic.ginecologia.model.SchedulePK;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.model.Weekday;
import es.udc.fic.ginecologia.repository.RoleDao;
import es.udc.fic.ginecologia.repository.ScheduleDao;
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

	@Autowired
	private ScheduleDao scheduleDao;

	@Autowired
	private BCryptPasswordEncoder encrypter;

	private User createUser(String name, String username, String email, String postalAddress, String location,
			String DNI, String phoneNumber, String collegiateNumber) {
		User user = new User(name, username, email, postalAddress, location, DNI, phoneNumber, collegiateNumber);
		user.setPassword(username);
		return user;
	}

	private Role createRole(String name) {
		return new Role(name);
	}

	private Schedule createSchedule(Integer userId, Weekday weekday, LocalTime initialHour, LocalTime finalHour) {
		Schedule result = new Schedule();
		SchedulePK resultPK = new SchedulePK();
		resultPK.setUser_id(userId);
		resultPK.setWeekday(weekday);
		result.setPk(resultPK);
		result.setInitial_hour(initialHour);
		result.setFinal_hour(finalHour);

		return result;
	}

	@Test
	public void testFindAllUsers() {
		User user1 = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "22222222B",
				"123456789", "235088754");
		User user3 = createUser("User 3", "user3", "user3@example.com", "postalAddress 3", "location 3", "33333333C",
				"987654321", "010266666");
		User user4 = createUser("User 4", "user4", "user4@example.com", "postalAddress 4", "location 4", "44444444D",
				"554488997", "303156565");

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

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		User result = userDao.findByUsername("user1").get();

		Set<Role> foundRoles = result.getRoles();
		assertTrue(foundRoles.contains(role1) && foundRoles.contains(role1));

		assertEquals(result.getName(), user.getName());
		assertEquals(result.getUsername(), user.getUsername());
		assertEquals(result.getEmail(), user.getEmail());
		assertEquals(result.getPostal_address(), user.getPostal_address());
		assertEquals(result.getLocation(), user.getLocation());
		assertEquals(result.getDni(), user.getDni());
		assertEquals(result.getPhone_number(), user.getPhone_number());
		assertEquals(result.getCollegiate_number(), user.getCollegiate_number());
		assertEquals(user.getPassword(), result.getPassword());
		assertTrue(result.isEnabled());
	}

	@Test
	public void testRegisterUserExpectDuplicateInstanceException() throws DuplicateInstanceException {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(DuplicateInstanceException.class, () -> userService.registerUser(user, roles));
	}

	@Test
	public void testUpdateProfile() throws DuplicateInstanceException, InstanceNotFoundException {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		roles = Arrays.asList(role1.getId());

		User result = userService.updateProfile(user.getId(), "New User 1", "newuser1@example.com",
				"newpostalAddress 1", "newlocation 1", "11111111B", "655789123", "122412345", roles);

		Set<Role> foundRoles = result.getRoles();
		assertTrue(foundRoles.contains(role1));

		assertEquals(result.getName(), "New User 1");
		assertEquals(result.getEmail(), "newuser1@example.com");
		assertEquals(result.getPostal_address(), "newpostalAddress 1");
		assertEquals(result.getLocation(), "newlocation 1");
		assertEquals(result.getDni(), "11111111B");
		assertEquals(result.getPhone_number(), "655789123");
		assertEquals(result.getCollegiate_number(), "122412345");
	}

	@Test
	public void testUpdateProfileExpectInstanceNotFoundException()
			throws DuplicateInstanceException, InstanceNotFoundException {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");

		roleDao.save(role1);
		roleDao.save(role2);

		final Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		final Iterable<Integer> updateRoles = Arrays.asList(role1.getId());

		assertThrows(InstanceNotFoundException.class,
				() -> userService.updateProfile(-1, "New User 1", "newuser1@example.com", "newpostalAddress 1",
						"newlocation 1", "11111111B", "655789123", "122412345", updateRoles));

	}

	@Test
	public void testUpdateProfileAsAdmin()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, roles);

		roles = Arrays.asList(role1.getId());

		User result = userService.updateProfile(admin.getId(), user.getId(), "New User 1", "newuser1@example.com",
				"newpostalAddress 1", "newlocation 1", "11111111B", "655789123", "122412345", roles);

		Set<Role> foundRoles = result.getRoles();
		assertTrue(foundRoles.contains(role1));

		assertEquals(result.getName(), "New User 1");
		assertEquals(result.getEmail(), "newuser1@example.com");
		assertEquals(result.getPostal_address(), "newpostalAddress 1");
		assertEquals(result.getLocation(), "newlocation 1");
		assertEquals(result.getDni(), "11111111B");
		assertEquals(result.getPhone_number(), "655789123");
		assertEquals(result.getCollegiate_number(), "122412345");
	}

	@Test
	public void testUpdateProfileAsAdminExpectedAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, roles);

		final Iterable<Integer> updateRoles = Arrays.asList(role1.getId());

		assertThrows(InstanceNotFoundException.class,
				() -> userService.updateProfile(-1, user.getId(), "New User 1", "newuser1@example.com",
						"newpostalAddress 1", "newlocation 1", "11111111B", "655789123", "122412345", updateRoles));
	}

	@Test
	public void testUpdateProfileAsAdminExpectedUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, roles);

		final Iterable<Integer> updateRoles = Arrays.asList(role1.getId());

		assertThrows(InstanceNotFoundException.class,
				() -> userService.updateProfile(admin.getId(), -1, "New User 1", "newuser1@example.com",
						"newpostalAddress 1", "newlocation 1", "11111111B", "655789123", "122412345", updateRoles));
	}

	@Test
	public void testUpdateProfileAsAdminExpectedPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());
		final Iterable<Integer> updateRoles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, updateRoles);

		assertThrows(PermissionException.class,
				() -> userService.updateProfile(admin.getId(), user.getId(), "New User 1", "newuser1@example.com",
						"newpostalAddress 1", "newlocation 1", "11111111B", "655789123", "122412345", updateRoles));
	}

	@Test
	public void testChangePassword()
			throws DuplicateInstanceException, InstanceNotFoundException, IncorrectPasswordException {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		userService.changePassword(user.getId(), "password1", "newpassword1");

		User result = userService.findUserById(user.getId());

		assertTrue(encrypter.matches("newpassword1", result.getPassword()));
	}

	@Test
	public void testChangePasswordExpectedUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, IncorrectPasswordException {

		assertThrows(InstanceNotFoundException.class,
				() -> userService.changePassword(-1, "password1", "newpassword1"));
	}

	@Test
	public void testChangePasswordExpectedWrongPassword()
			throws DuplicateInstanceException, InstanceNotFoundException, IncorrectPasswordException {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(IncorrectPasswordException.class,
				() -> userService.changePassword(user.getId(), "wrongpassword1", "newpassword1"));

	}

	@Test
	public void testChangePasswordAsAdmin() throws DuplicateInstanceException, InstanceNotFoundException,
			IncorrectPasswordException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, roles);

		userService.changePassword(admin.getId(), user.getId(), "password1", "newpassword1");

		User result = userService.findUserById(user.getId());

		assertTrue(encrypter.matches("newpassword1", result.getPassword()));
	}

	@Test
	public void testChangePasswordAsAdminExpectedAdminNotFound() throws DuplicateInstanceException,
			InstanceNotFoundException, IncorrectPasswordException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> userService.changePassword(-1, user.getId(), "password1", "newpassword1"));
	}

	@Test
	public void testChangePasswordAsAdminExpectedUserNotFound() throws DuplicateInstanceException,
			InstanceNotFoundException, IncorrectPasswordException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> userService.changePassword(admin.getId(), -1, "password1", "newpassword1"));
	}

	@Test
	public void testChangePasswordAsAdminExpectedIncorrectPassword() throws DuplicateInstanceException,
			InstanceNotFoundException, IncorrectPasswordException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, roles);

		assertThrows(IncorrectPasswordException.class,
				() -> userService.changePassword(admin.getId(), user.getId(), "wrongpassword1", "newpassword1"));
	}

	@Test
	public void testChangePasswordAsAdminExpectedPermissionException() throws DuplicateInstanceException,
			InstanceNotFoundException, IncorrectPasswordException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());
		Iterable<Integer> rolesWithoutAdmin = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		admin.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, rolesWithoutAdmin);

		assertThrows(PermissionException.class,
				() -> userService.changePassword(admin.getId(), user.getId(), "password1", "newpassword1"));
	}

	@Test
	public void testFindUserById() throws DuplicateInstanceException, InstanceNotFoundException {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		User result = userService.findUserById(user.getId());

		assertEquals(user, result);
	}

	@Test
	public void testFindUserByIdExpectUserNotFound() throws DuplicateInstanceException, InstanceNotFoundException {
		assertThrows(InstanceNotFoundException.class, () -> userService.findUserById(-1));
	}

	@Test
	public void testExistsUserById() throws DuplicateInstanceException, InstanceNotFoundException {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertTrue(userService.existsUserById(user.getId()));
	}

	@Test
	public void testExistsUserByIdUnknownUser() throws DuplicateInstanceException, InstanceNotFoundException {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertFalse(userService.existsUserById(-1));
	}

	@Test
	public void testChangeUserStateDisableUser()
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		admin.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, roles);

		userService.changeUserState(admin.getId(), user.getId());

		assertFalse(user.isEnabled());
	}

	@Test
	public void testChangeUserStateEnableUser()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setEnabled(false);
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		admin.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, roles);

		userService.changeUserState(admin.getId(), user.getId());

		assertTrue(user.isEnabled());
	}

	@Test
	public void testChangeUserStateAdminNotFoundExpected() throws DuplicateInstanceException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class, () -> userService.changeUserState(-1, user.getId()));
	}

	@Test
	public void testChangeUserStateUserNotFoundExpected() throws DuplicateInstanceException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User admin = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		admin.setPassword("password1");

		userService.registerUser(admin, roles);

		assertThrows(InstanceNotFoundException.class, () -> userService.changeUserState(admin.getId(), -1));
	}

	@Test
	public void testChangeUserStatePermissionExceptionExpected() throws DuplicateInstanceException {
		Role role1 = createRole("role1");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());
		Iterable<Integer> rolesWithoutAdmin = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		User admin = createUser("Admin", "admin", "admin@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		admin.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(admin, rolesWithoutAdmin);

		assertThrows(PermissionException.class, () -> userService.changeUserState(admin.getId(), user.getId()));
	}

	@Test
	public void testFindUsers() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roleFacultative = Arrays.asList(role1.getId());
		Iterable<Integer> roleAdmin = Arrays.asList(role2.getId());

		User user1 = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user1.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789123", "122112345");
		user2.setPassword("password2");
		User user3 = createUser("User 3", "user3", "user3@example.com", "postalAddress 3", "location 3", "11111111C",
				"654789123", "122112345");
		user3.setPassword("password3");
		User user4 = createUser("User 4", "user4", "user4@example.com", "postalAddress 4", "location 4", "11111111D",
				"654789123", "122112345");
		user4.setPassword("password4");

		userService.registerUser(user1, roleFacultative);
		userService.registerUser(user2, roleAdmin);
		userService.registerUser(user3, roleFacultative);
		userService.registerUser(user4, roleAdmin);

		LocalDateTime dateFrom = LocalDateTime.now().minusDays(2);
		LocalDateTime dateTo = LocalDateTime.now().plusDays(2);

		Iterable<User> result = userService.findUsers(user2.getId(), "user", "user", "user example", dateFrom, dateTo,
				true, role2.getId());

		Iterable<User> expectedResult = Arrays.asList(user2, user4);

		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindUsersExpectAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role2);

		LocalDateTime dateFrom = LocalDateTime.now().minusDays(2);
		LocalDateTime dateTo = LocalDateTime.now().plusDays(2);

		assertThrows(InstanceNotFoundException.class,
				() -> userService.findUsers(-1, "user", "user", "user example", dateFrom, dateTo, true, role2.getId()));

	}

	@Test
	public void testFindUsersExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roleFacultative = Arrays.asList(role1.getId());

		User user1 = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user1.setPassword("password1");

		userService.registerUser(user1, roleFacultative);

		LocalDateTime dateFrom = LocalDateTime.now().minusDays(2);
		LocalDateTime dateTo = LocalDateTime.now().plusDays(2);

		assertThrows(PermissionException.class, () -> userService.findUsers(user1.getId(), "user", "user",
				"user example", dateFrom, dateTo, true, role1.getId()));
	}

	@Test
	public void testChangeSchedule() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roleFacultative = Arrays.asList(role1.getId());
		Iterable<Integer> roleAdmin = Arrays.asList(role2.getId());

		User user1 = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user1.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789123", "122112345");
		user2.setPassword("password2");

		userService.registerUser(user1, roleFacultative);
		userService.registerUser(user2, roleAdmin);

		Schedule monday = createSchedule(user1.getId(), Weekday.monday, LocalTime.of(10, 00), LocalTime.of(14, 00));
		Schedule tuesday = createSchedule(user1.getId(), Weekday.tuesday, LocalTime.of(16, 00), LocalTime.of(20, 00));
		Schedule wednesday = createSchedule(user1.getId(), Weekday.wednesday, LocalTime.of(10, 00),
				LocalTime.of(18, 00));
		Schedule thursday = createSchedule(user1.getId(), Weekday.thursday, LocalTime.of(8, 00), LocalTime.of(16, 00));
		Schedule friday = createSchedule(user1.getId(), Weekday.friday, LocalTime.of(10, 00), LocalTime.of(14, 00));

		List<Schedule> schedulesExpected = Arrays.asList(monday, tuesday, wednesday, thursday, friday);

		Set<Schedule> schedules = new HashSet<>();
		schedules.add(monday);
		schedules.add(tuesday);
		schedules.add(wednesday);
		schedules.add(thursday);
		schedules.add(friday);

		userService.changeSchedule(user2.getId(), user1.getId(), schedules);

		Iterable<Schedule> result = scheduleDao.findAll();

		for (Schedule schedule : result) {
			Schedule scheduleFound = schedulesExpected.stream().filter(s -> s.getPk().equals(schedule.getPk()))
					.findFirst().orElse(null);

			assertTrue(scheduleFound != null);
			assertEquals(schedule.getInitial_hour(), scheduleFound.getInitial_hour());
			assertEquals(schedule.getFinal_hour(), scheduleFound.getFinal_hour());

		}
	}

	@Test
	public void testChangeScheduleThrowsAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roleFacultative = Arrays.asList(role1.getId());
		Iterable<Integer> roleAdmin = Arrays.asList(role2.getId());

		User user1 = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user1.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789123", "122112345");
		user2.setPassword("password2");

		userService.registerUser(user1, roleFacultative);
		userService.registerUser(user2, roleAdmin);

		assertThrows(InstanceNotFoundException.class, () -> userService.changeSchedule(-1, user1.getId(), null));

	}
	
	@Test
	public void testChangeScheduleThrowsUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roleFacultative = Arrays.asList(role1.getId());
		Iterable<Integer> roleAdmin = Arrays.asList(role2.getId());

		User user1 = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user1.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789123", "122112345");
		user2.setPassword("password2");

		userService.registerUser(user1, roleFacultative);
		userService.registerUser(user2, roleAdmin);

		assertThrows(InstanceNotFoundException.class, () -> userService.changeSchedule(user2.getId(), -1, null));

	}
	
	@Test
	public void testChangeScheduleThrowsPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roleFacultative = Arrays.asList(role1.getId());

		User user1 = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user1.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789123", "122112345");
		user2.setPassword("password2");

		userService.registerUser(user1, roleFacultative);
		userService.registerUser(user2, roleFacultative);

		assertThrows(PermissionException.class, () -> userService.changeSchedule(user2.getId(), user1.getId(), null));

	}
}
