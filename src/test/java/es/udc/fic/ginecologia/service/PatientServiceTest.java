package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.PatientDao;
import es.udc.fic.ginecologia.repository.RoleDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PatientServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PatientDao patientDao;

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
	public void findAllPatientsTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient1 = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 2", "11111111B", "630123456", "981987654", LocalDateTime.now(),
				"121516ABCD1011", "Postal address 2", "Location 2", "email2@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient3 = new Patient("Patient 3", "11111111C", "679723722", "981654789", LocalDateTime.now(),
				"121416ABCD9875", "Postal address 3", "Location 3", "email3@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient4 = new Patient("Patient 4", "11111111D", "630103924", "981088021", LocalDateTime.now(),
				"121416DEFG1011", "Postal address 4", "Location 4", "email4@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		patientDao.save(patient1);
		patientDao.save(patient2);
		patientDao.save(patient3);
		patientDao.save(patient4);

		Iterable<Patient> result = patientService.findAllPatients(user.getId());
		Iterable<Patient> expectedResult = Arrays.asList(patient4, patient3, patient2, patient1);

		assertEquals(expectedResult, result);
	}

	@Test
	public void findAllPatientsTestExpectFacultativeNotFound() throws InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> patientService.findAllPatients(-1));
	}

	@Test
	public void findAllPatientsTestExpectPermissionException()
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> patientService.findAllPatients(user.getId()));
	}

	@Test
	public void findPatientTest() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		patientDao.save(patient);

		Patient expectedResult = patientDao.findById(patient.getId()).get();
		Patient result = patientService.findPatient(user.getId(), patient.getId());

		assertEquals(expectedResult, result);
	}

	@Test
	public void findPatientTestExpectedFacultativeNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		patientDao.save(patient);

		assertThrows(InstanceNotFoundException.class, () -> patientService.findPatient(-1, patient.getId()));

	}

	@Test
	public void findPatientTestExpectedPatientNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class, () -> patientService.findPatient(user.getId(), -1L));

	}

	@Test
	public void findPatientTestExpectedPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		patientDao.save(patient);

		assertThrows(PermissionException.class, () -> patientService.findPatient(user.getId(), patient.getId()));

	}

	@Test
	public void findPatientsTest() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient1 = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 2", "11111111B", "630123456", "981987654", LocalDateTime.now(),
				"121516ABCD1011", "Postal address 2", "Location 2", "email2@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient3 = new Patient("Patient 3", "11111111C", "679723722", "981654789", LocalDateTime.now(),
				"121416ABCD9875", "Postal address 3", "Location 3", "email3@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient4 = new Patient("Patient 4", "11111111D", "630103924", "981088021", LocalDateTime.now(),
				"121416DEFG1011", "Postal address 4", "Location 4", "email4@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		patientDao.save(patient1);
		patientDao.save(patient2);
		patientDao.save(patient3);
		patientDao.save(patient4);

		Iterable<Patient> result = patientService.findPatients(user.getId(), "Patient", "11111111", "121", true);
		Iterable<Patient> expectedResult = Arrays.asList(patient4, patient3, patient2, patient1);

		assertEquals(expectedResult, result);
	}

	@Test
	public void findPatientsTestExpectedFacultativeNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> patientService.findPatients(-1, "Patient", "11111111", "121", true));
	}

	@Test
	public void findPatientsTestExpectedPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class,
				() -> patientService.findPatients(user.getId(), "Patient", "11111111", "121", true));
	}

	@Test
	public void addPatientTest() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		Patient expectedPatient = patientDao.findByDNI_NIF(patient.getDNI_NIF()).get();

		assertEquals(expectedPatient, patient);
	}

	@Test
	public void addPatientExpectDuplicateInstanceException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		assertThrows(DuplicateInstanceException.class, () -> patientService.addPatient(user.getId(), patient, ids));
	}

	@Test
	public void addPatientExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		assertThrows(InstanceNotFoundException.class, () -> patientService.addPatient(-1, patient, ids));
	}

	@Test
	public void addPatientExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		assertThrows(PermissionException.class, () -> patientService.addPatient(user.getId(), patient, ids));
	}

	@Test
	public void updatePatientTest() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		Patient updatedPatient = new Patient("Patient updated 1", "11111111B", "630882422", "981089021",
				LocalDateTime.now(), "121417ABCD1011", "Postal address updated 1", "Location updated 1",
				"emailupdated1@example.com", "A", "B", "C", "D", true, 12, 60, LocalDateTime.now(), 1, 2, 3, 4, "E",
				user);

		patientService.updatePatient(user.getId(), patient.getId(), updatedPatient, ids);

		updatedPatient = patientDao.findByDNI_NIF("11111111B").get();

		assertEquals(updatedPatient, patient);
	}

	@Test
	public void updatePatientTestExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		Patient updatedPatient = new Patient("Patient updated 1", "11111111B", "630882422", "981089021",
				LocalDateTime.now(), "121417ABCD1011", "Postal address updated 1", "Location updated 1",
				"emailupdated1@example.com", "A", "B", "C", "D", true, 12, 60, LocalDateTime.now(), 1, 2, 3, 4, "E",
				user);

		assertThrows(InstanceNotFoundException.class,
				() -> patientService.updatePatient(-1, patient.getId(), updatedPatient, ids));

	}

	@Test
	public void updatePatientTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		Patient updatedPatient = new Patient("Patient updated 1", "11111111B", "630882422", "981089021",
				LocalDateTime.now(), "121417ABCD1011", "Postal address updated 1", "Location updated 1",
				"emailupdated1@example.com", "A", "B", "C", "D", true, 12, 60, LocalDateTime.now(), 1, 2, 3, 4, "E",
				user);

		assertThrows(PermissionException.class,
				() -> patientService.updatePatient(user.getId(), patient.getId(), updatedPatient, ids));

	}

	@Test
	public void updatePatientTestExpectPatientNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		Patient updatedPatient = new Patient("Patient updated 1", "11111111B", "630882422", "981089021",
				LocalDateTime.now(), "121417ABCD1011", "Postal address updated 1", "Location updated 1",
				"emailupdated1@example.com", "A", "B", "C", "D", true, 12, 60, LocalDateTime.now(), 1, 2, 3, 4, "E",
				user);

		assertThrows(InstanceNotFoundException.class,
				() -> patientService.updatePatient(user.getId(), -1L, updatedPatient, ids));

	}

	@Test
	public void updatePatientTestExpectDuplicateDNI_NIF()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 1", "11111111B", "630882421", "981088021", LocalDateTime.now(),
				"121418ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);
		patientService.addPatient(user.getId(), patient2, ids);

		Patient updatedPatient = new Patient("Patient updated 1", "11111111B", "630882422", "981089021",
				LocalDateTime.now(), "121417ABCD1011", "Postal address updated 1", "Location updated 1",
				"emailupdated1@example.com", "A", "B", "C", "D", true, 12, 60, LocalDateTime.now(), 1, 2, 3, 4, "E",
				user);

		assertThrows(DuplicateInstanceException.class,
				() -> patientService.updatePatient(user.getId(), patient.getId(), updatedPatient, ids));

	}

	@Test
	public void updatePatientTestExpectDuplicateHist_numsergas()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 1", "11111111B", "630882421", "981088021", LocalDateTime.now(),
				"121418ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);
		patientService.addPatient(user.getId(), patient2, ids);

		Patient updatedPatient = new Patient("Patient updated 1", "11111111C", "630882422", "981089021",
				LocalDateTime.now(), "121418ABCD1011", "Postal address updated 1", "Location updated 1",
				"emailupdated1@example.com", "A", "B", "C", "D", true, 12, 60, LocalDateTime.now(), 1, 2, 3, 4, "E",
				user);

		assertThrows(DuplicateInstanceException.class,
				() -> patientService.updatePatient(user.getId(), patient.getId(), updatedPatient, ids));

	}

	@Test
	public void changePatientEnablingStateTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		patientService.changePatientEnablingState(user.getId(), patient.getId());

		assertFalse(patient.isEnabled());

		patientService.changePatientEnablingState(user.getId(), patient.getId());

		assertTrue(patient.isEnabled());
	}

	@Test
	public void changePatientEnablingStateTestExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		assertThrows(InstanceNotFoundException.class,
				() -> patientService.changePatientEnablingState(-1, patient.getId()));

	}

	@Test
	public void changePatientEnablingStateTestExpectPatientNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		assertThrows(InstanceNotFoundException.class,
				() -> patientService.changePatientEnablingState(user.getId(), -1L));

	}

	@Test
	public void changePatientEnablingStateTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		assertThrows(PermissionException.class,
				() -> patientService.changePatientEnablingState(user.getId(), patient.getId()));

	}

	@Test
	public void changeAsPatientOfInterestTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		patient = patientDao.findByDNI_NIF("11111111A").get();

		patientService.changeAsPatientOfInterest(user.getId(), patient.getId());

		user = userService.findUserById(user.getId());

		Set<Patient> patientsOfInterest = user.getPatientsOfInterest();

		assertTrue(patientsOfInterest.contains(patient));

		patientService.changeAsPatientOfInterest(user.getId(), patient.getId());

		user = userService.findUserById(user.getId());

		patientsOfInterest = user.getPatientsOfInterest();

		assertFalse(patientsOfInterest.contains(patient));
	}

	@Test
	public void changeAsPatientOfInterestTestExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		assertThrows(InstanceNotFoundException.class,
				() -> patientService.changeAsPatientOfInterest(-1, patient.getId()));
	}
	
	@Test
	public void changeAsPatientOfInterestTestExpectPatientNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		assertThrows(InstanceNotFoundException.class,
				() -> patientService.changeAsPatientOfInterest(user.getId(), -1L));
	}
	
	@Test
	public void changeAsPatientOfInterestTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class,
				() -> patientService.changeAsPatientOfInterest(user.getId(), 1L));
	}
}
