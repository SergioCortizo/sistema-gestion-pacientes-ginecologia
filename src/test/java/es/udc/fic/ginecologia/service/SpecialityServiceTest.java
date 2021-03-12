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
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.Speciality;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.RoleDao;
import es.udc.fic.ginecologia.repository.SpecialityDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SpecialityServiceTest {
	@Autowired
	private UserService userService;

	@Autowired
	private SpecialityService specialityService;

	@Autowired
	private SpecialityDao specialityDao;

	@Autowired
	private RoleDao roleDao;

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
	public void findAllSpecialitiesTest() {
		Speciality speciality1 = new Speciality("speciality1");
		Speciality speciality2 = new Speciality("speciality2");
		Speciality speciality3 = new Speciality("speciality3");
		Speciality speciality4 = new Speciality("speciality4");

		specialityDao.save(speciality1);
		specialityDao.save(speciality2);
		specialityDao.save(speciality3);
		specialityDao.save(speciality4);

		Iterable<Speciality> result = specialityService.findAllSpecialities();
		Iterable<Speciality> expectedResult = Arrays.asList(speciality1, speciality2, speciality3, speciality4);

		assertEquals(expectedResult, result);
	}

	@Test
	public void addSpecialityTest() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		specialityService.addSpeciality(user.getId(), "newspeciality");

		Speciality speciality = specialityDao.findByName("newspeciality").get();

		assertEquals("newspeciality", speciality.getName());
	}

	@Test
	public void addSpecialityTestExpectAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class, () -> specialityService.addSpeciality(-1, "newspeciality"));

	}

	@Test
	public void addSpecialityTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> specialityService.addSpeciality(user.getId(), "newspeciality"));

	}

	@Test
	public void addSpecialityTestExpectDuplicateInstance()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		specialityService.addSpeciality(user.getId(), "newspeciality");

		assertThrows(DuplicateInstanceException.class,
				() -> specialityService.addSpeciality(user.getId(), "newspeciality"));
	}

	@Test
	public void updateSpecialityTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		specialityService.addSpeciality(user.getId(), "newspeciality");

		Speciality speciality = specialityDao.findByName("newspeciality").get();

		specialityService.updateSpeciality(user.getId(), speciality.getId(), "updatespeciality");

		speciality = specialityDao.findByName("updatespeciality").get();

		assertEquals("updatespeciality", speciality.getName());
	}

	@Test
	public void updateSpecialityTestAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		specialityService.addSpeciality(user.getId(), "newspeciality");

		Speciality speciality = specialityDao.findByName("newspeciality").get();

		assertThrows(InstanceNotFoundException.class,
				() -> specialityService.updateSpeciality(-1, speciality.getId(), "updatespeciality"));
	}

	@Test
	public void updateSpecialityTestSpecialityNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> specialityService.updateSpeciality(user.getId(), -1, "updatespeciality"));
	}

	@Test
	public void updateSpecialityTestPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class,
				() -> specialityService.updateSpeciality(user.getId(), -1, "updatespeciality"));
	}

	@Test
	public void updateSpecialityTestDuplicateInstance()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		specialityService.addSpeciality(user.getId(), "newspeciality");

		Speciality speciality = specialityDao.findByName("newspeciality").get();

		assertThrows(DuplicateInstanceException.class,
				() -> specialityService.updateSpeciality(user.getId(), speciality.getId(), "newspeciality"));
	}

	@Test
	public void changeEnablingSpecialityTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		specialityService.addSpeciality(user.getId(), "newspeciality");

		Speciality speciality = specialityDao.findByName("newspeciality").get();

		specialityService.changeEnablingSpeciality(user.getId(), speciality.getId());

		speciality = specialityDao.findByName("newspeciality").get();

		assertFalse(speciality.isEnabled());

		specialityService.changeEnablingSpeciality(user.getId(), speciality.getId());

		speciality = specialityDao.findByName("newspeciality").get();

		assertTrue(speciality.isEnabled());
	}

	@Test
	public void changeEnablingSpecialityTestAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		specialityService.addSpeciality(user.getId(), "newspeciality");

		Speciality speciality = specialityDao.findByName("newspeciality").get();

		assertThrows(InstanceNotFoundException.class,
				() -> specialityService.changeEnablingSpeciality(-1, speciality.getId()));
	}

	@Test
	public void changeEnablingSpecialityTestSpecialityNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> specialityService.changeEnablingSpeciality(user.getId(), -1));
	}

	@Test
	public void changeEnablingSpecialityTestPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> specialityService.changeEnablingSpeciality(user.getId(), -1));
	}

	@Test
	public void findSpecialitiesTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Speciality speciality1 = new Speciality("speciality1");
		Speciality speciality2 = new Speciality("speciality2");
		Speciality speciality3 = new Speciality("speciality3");
		Speciality speciality4 = new Speciality("speciality4");

		specialityDao.save(speciality1);
		specialityDao.save(speciality2);
		specialityDao.save(speciality3);
		specialityDao.save(speciality4);

		Iterable<Speciality> expectedResult = Arrays.asList(speciality1, speciality2, speciality3, speciality4);

		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Iterable<Speciality> result = specialityService.findSpecialities(user.getId(), "speciality", true);

		assertEquals(expectedResult, result);
	}

	@Test
	public void findSpecialitiesTestAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> specialityService.findSpecialities(-1, "speciality", true));
	}

	@Test
	public void findSpecialitiesTestPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class,
				() -> specialityService.findSpecialities(user.getId(), "speciality", true));
	}
}
