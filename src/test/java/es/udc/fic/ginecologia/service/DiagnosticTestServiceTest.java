package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import es.udc.fic.ginecologia.model.DiagnosticTest;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.DiagnosticTestDao;
import es.udc.fic.ginecologia.repository.RoleDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DiagnosticTestServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private DiagnosticTestService diagnosticTestService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private DiagnosticTestDao diagnosticTestDao;

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
	public void findAllDiagnosticTestsTest() {

		DiagnosticTest diagnosticTest1 = new DiagnosticTest("diagnosticTest1");
		DiagnosticTest diagnosticTest2 = new DiagnosticTest("diagnosticTest2");
		DiagnosticTest diagnosticTest3 = new DiagnosticTest("diagnosticTest3");
		DiagnosticTest diagnosticTest4 = new DiagnosticTest("diagnosticTest4");

		diagnosticTestDao.save(diagnosticTest1);
		diagnosticTestDao.save(diagnosticTest2);
		diagnosticTestDao.save(diagnosticTest3);
		diagnosticTestDao.save(diagnosticTest4);

		Iterable<DiagnosticTest> result = diagnosticTestService.findAllDiagnosticTests();
		Iterable<DiagnosticTest> expectedResult = Arrays.asList(diagnosticTest1, diagnosticTest2, diagnosticTest3,
				diagnosticTest4);

		assertEquals(expectedResult, result);
	}

	@Test
	public void addDiagnosticTestTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		diagnosticTestService.addDiagnosticTest(user.getId(), "newdiagnosticTest");

		DiagnosticTest diagnosticTest = diagnosticTestDao.findByName("newdiagnosticTest").get();

		assertEquals("newdiagnosticTest", diagnosticTest.getName());
		assertTrue(diagnosticTest.isEnabled());
	}

	@Test
	public void addDiangosticTestTestExpectAdminNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class,
				() -> diagnosticTestService.addDiagnosticTest(-1, "newdiagnosticTest"));
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
				() -> diagnosticTestService.addDiagnosticTest(user.getId(), "newdiagnosticTest"));
	}

	@Test
	public void addMedicineTestExpectDuplicateInstanceException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		diagnosticTestService.addDiagnosticTest(user.getId(), "newdiagnosticTest");

		assertThrows(DuplicateInstanceException.class,
				() -> diagnosticTestService.addDiagnosticTest(user.getId(), "newdiagnosticTest"));
	}
}
