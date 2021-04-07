package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.CalendarEntry;
import es.udc.fic.ginecologia.model.MeetingState;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.CalendarEntryDao;
import es.udc.fic.ginecologia.repository.RoleDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CalendarEntryTest {

	@Autowired
	private CalendarEntryService calendarEntryService;

	@Autowired
	private UserService userService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private CalendarEntryDao calendarEntryDao;

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
	public void findCalendarEntriesTest()
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

		CalendarEntry calendarEntry1 = new CalendarEntry(LocalDateTime.now(), "Reason 1", user, patient);
		CalendarEntry calendarEntry2 = new CalendarEntry(LocalDateTime.now(), "Reason 2", user, patient);
		CalendarEntry calendarEntry3 = new CalendarEntry(LocalDateTime.now(), "Reason 3", user, patient);

		calendarEntryDao.save(calendarEntry1);
		calendarEntryDao.save(calendarEntry2);
		calendarEntryDao.save(calendarEntry3);

		Iterable<CalendarEntry> result = calendarEntryService.findCalendarEntries(user.getId());
		Iterable<CalendarEntry> expectedCalendarEntries = Arrays.asList(calendarEntry1, calendarEntry2, calendarEntry3);

		assertEquals(expectedCalendarEntries, result);

	}

	@Test
	public void findCalendarEntriesTestExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.findCalendarEntries(-1));

	}

	@Test
	public void findCalendarEntriesTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> calendarEntryService.findCalendarEntries(user.getId()));

	}

	@Test
	public void findAllCalendarEntriesTest()
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

		CalendarEntry calendarEntry1 = new CalendarEntry(LocalDateTime.now(), "Reason 1", user, patient);
		CalendarEntry calendarEntry2 = new CalendarEntry(LocalDateTime.now(), "Reason 2", user, patient);
		CalendarEntry calendarEntry3 = new CalendarEntry(LocalDateTime.now(), "Reason 3", user, patient);

		calendarEntryDao.save(calendarEntry1);
		calendarEntryDao.save(calendarEntry2);
		calendarEntryDao.save(calendarEntry3);

		Iterable<CalendarEntry> result = calendarEntryService.findAllCalendarEntries(user.getId());
		Iterable<CalendarEntry> expectedCalendarEntries = Arrays.asList(calendarEntry1, calendarEntry2, calendarEntry3);

		assertEquals(expectedCalendarEntries, result);

	}

	@Test
	public void findAllCalendarEntriesTestExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.findAllCalendarEntries(-1));

	}

	@Test
	public void findAllCalendarEntriesTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> calendarEntryService.findAllCalendarEntries(user.getId()));

	}

	@Test
	public void addCalendarEntryTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId()).forEach(ce -> {
			assertEquals(user, ce.getUser());
			assertEquals(patient, ce.getPatient());
			assertEquals(MeetingState.opened, ce.getState());
			assertEquals(date, ce.getEntryDate());
			assertEquals("Reason", ce.getReason());
		});

	}

	@Test
	public void addCalendarEntryTestExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		assertThrows(InstanceNotFoundException.class,
				() -> calendarEntryService.addCalendarEntry(-1, user.getId(), patient.getId(), date, "Reason"));

	}

	@Test
	public void addCalendarEntryTestExpectFacultativeNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		assertThrows(InstanceNotFoundException.class,
				() -> calendarEntryService.addCalendarEntry(user.getId(), -1, patient.getId(), date, "Reason"));

	}

	@Test
	public void addCalendarEntryTestExpectPatientNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		assertThrows(InstanceNotFoundException.class,
				() -> calendarEntryService.addCalendarEntry(user.getId(), user.getId(), -1L, date, "Reason"));

	}

	@Test
	public void addCalendarEntryTestExpectPermissionAdminException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		assertThrows(PermissionException.class, () -> calendarEntryService.addCalendarEntry(user.getId(), user.getId(),
				patient.getId(), date, "Reason"));

	}

	@Test
	public void addCalendarEntryTestExpectPermissionFacultativeException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		assertThrows(PermissionException.class, () -> calendarEntryService.addCalendarEntry(user.getId(), user.getId(),
				patient.getId(), date, "Reason"));

	}

	@Test
	public void updateCalendarEntryTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789124", "122112355");
		user.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 2", "11111111B", "630882431", "981088121", LocalDateTime.now(),
				"121416ABCD1111", "Postal address 2", "Location 2", "email2@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);
		patientService.addPatient(user.getId(), patient2, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			calendarEntryService.updateCalendarEntry(user.getId(), ce.getId(), user2.getId(), patient2.getId(), date,
					"Updated reason");

			assertEquals(user2, ce.getUser());
			assertEquals(patient2, ce.getPatient());
			assertEquals(MeetingState.opened, ce.getState());
			assertEquals(date, ce.getEntryDate());
			assertEquals("Updated reason", ce.getReason());
		}

	}

	@Test
	public void updateCalendarEntryTestUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789124", "122112355");
		user.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 2", "11111111B", "630882431", "981088121", LocalDateTime.now(),
				"121416ABCD1111", "Postal address 2", "Location 2", "email2@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);
		patientService.addPatient(user.getId(), patient2, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.updateCalendarEntry(-1, ce.getId(),
					user2.getId(), patient2.getId(), date, "Updated reason"));
		}

	}

	@Test
	public void updateCalendarEntryTestCalendarEntryNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789124", "122112355");
		user.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 2", "11111111B", "630882431", "981088121", LocalDateTime.now(),
				"121416ABCD1111", "Postal address 2", "Location 2", "email2@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);
		patientService.addPatient(user.getId(), patient2, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.updateCalendarEntry(user.getId(), -1,
				user2.getId(), patient2.getId(), date, "Updated reason"));

	}

	@Test
	public void updateCalendarEntryTestFacultativeNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789124", "122112355");
		user.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 2", "11111111B", "630882431", "981088121", LocalDateTime.now(),
				"121416ABCD1111", "Postal address 2", "Location 2", "email2@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);
		patientService.addPatient(user.getId(), patient2, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.updateCalendarEntry(user.getId(),
					ce.getId(), -1, patient2.getId(), date, "Updated reason"));
		}

	}

	@Test
	public void updateCalendarEntryTestPatientNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789124", "122112355");
		user.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 2", "11111111B", "630882431", "981088121", LocalDateTime.now(),
				"121416ABCD1111", "Postal address 2", "Location 2", "email2@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);
		patientService.addPatient(user.getId(), patient2, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.updateCalendarEntry(user.getId(),
					ce.getId(), user2.getId(), -1L, date, "Updated reason"));
		}

	}

	@Test
	public void updateCalendarEntryTestAdminPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789124", "122112355");
		user.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 2", "11111111B", "630882431", "981088121", LocalDateTime.now(),
				"121416ABCD1111", "Postal address 2", "Location 2", "email2@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);
		patientService.addPatient(user.getId(), patient2, ids);

		assertThrows(PermissionException.class, () -> calendarEntryService.updateCalendarEntry(user.getId(), 1,
				user2.getId(), patient2.getId(), LocalDateTime.now(), "Updated reason"));

	}

	@Test
	public void updateCalendarEntryTestFacultativePermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789124", "122112355");
		user.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);
		Patient patient2 = new Patient("Patient 2", "11111111B", "630882431", "981088121", LocalDateTime.now(),
				"121416ABCD1111", "Postal address 2", "Location 2", "email2@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);
		patientService.addPatient(user.getId(), patient2, ids);

		assertThrows(PermissionException.class, () -> calendarEntryService.updateCalendarEntry(user.getId(), 1,
				user2.getId(), patient2.getId(), LocalDateTime.now(), "Updated reason"));

	}

	@Test
	public void cancelEntryTest() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			calendarEntryService.cancelEntry(user.getId(), ce.getId());

			ce.setState(MeetingState.cancelled);
		}

	}

	@Test
	public void cancelEntryTestUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.cancelEntry(-1, ce.getId()));
		}

	}

	@Test
	public void cancelEntryTestCalendarEntryNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.cancelEntry(user.getId(), -1));

	}

	@Test
	public void cancelEntryTestPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		assertThrows(PermissionException.class, () -> calendarEntryService.cancelEntry(user.getId(), 1));

	}

	@Test
	public void setEntryAsClosedTest()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			calendarEntryService.setEntryAsClosed(user.getId(), ce.getId());

			assertEquals(MeetingState.closed, ce.getState());
		}
	}

	@Test
	public void setEntryAsClosedTestUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.setEntryAsClosed(-1, ce.getId()));
		}
	}

	@Test
	public void setEntryAsClosedTestCalendarEntryNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		assertThrows(InstanceNotFoundException.class, () -> calendarEntryService.setEntryAsClosed(user.getId(), -1));
	}
	
	@Test
	public void setEntryAsClosedTestPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");

		user.getRoles().remove(role2);
		
		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			assertThrows(PermissionException.class, () -> calendarEntryService.setEntryAsClosed(user.getId(), ce.getId()));
		}
	}
	
	@Test
	public void setEntryAsClosedTestPermissionExceptionWithAnotherUser()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");
		Role role2 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789124", "122112355");
		user.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		List<Integer> ids = new ArrayList<>();

		patientService.addPatient(user.getId(), patient, ids);

		final LocalDateTime date = LocalDateTime.now();

		calendarEntryService.addCalendarEntry(user.getId(), user.getId(), patient.getId(), date, "Reason");
		
		Iterable<CalendarEntry> list = calendarEntryDao.findByUserIdOrderByEntryDateAsc(user.getId());

		for (CalendarEntry ce : list) {
			assertThrows(PermissionException.class, () -> calendarEntryService.setEntryAsClosed(user2.getId(), ce.getId()));
		}
	}

}
