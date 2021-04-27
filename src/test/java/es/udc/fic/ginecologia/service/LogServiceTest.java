package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.LogLevel;
import es.udc.fic.ginecologia.model.LogLine;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.LogLineDao;
import es.udc.fic.ginecologia.repository.RoleDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LogServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private LogLineDao logLineDao;

	@Autowired
	private LogService logService;

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
	public void findLogsTest() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		LogLine log1 = new LogLine(LogLevel.WARN, LocalDateTime.now(), "Thread 1", "Logger 1", "Message 1");
		LogLine log2 = new LogLine(LogLevel.WARN, LocalDateTime.now(), "Thread 2", "Logger 2", "Message 2");
		LogLine log3 = new LogLine(LogLevel.ERROR, LocalDateTime.now(), "Thread 3", "Logger 3", "Message 3");
		LogLine log4 = new LogLine(LogLevel.INFO, LocalDateTime.now(), "Thread 4", "Logger 4", "Message 4");

		logLineDao.save(log1);
		logLineDao.save(log2);
		logLineDao.save(log3);
		logLineDao.save(log4);

		List<LogLine> result = StreamSupport.stream(logService.findLogs(user.getId()).spliterator(), false)
				.collect(Collectors.toList());
		Iterable<LogLine> expectedResult = Arrays.asList(log1, log2, log3, log4);

		for (LogLine logLine : expectedResult) {
			result.contains(logLine);
		}

	}

	@Test
	public void findLogsTestExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> logService.findLogs(-1));

	}

	@Test
	public void findLogsTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> logService.findLogs(user.getId()));

	}

	@Test
	public void searchLogsTest() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		LogLine log1 = new LogLine(LogLevel.WARN, LocalDateTime.now(), "Thread 1", "Logger 1", "Message 1");
		LogLine log2 = new LogLine(LogLevel.WARN, LocalDateTime.now(), "Thread 2", "Logger 2", "Message 2");
		LogLine log3 = new LogLine(LogLevel.ERROR, LocalDateTime.now(), "Thread 3", "Logger 3", "Message 3");
		LogLine log4 = new LogLine(LogLevel.INFO, LocalDateTime.now(), "Thread 4", "Logger 4", "Message 4");

		logLineDao.save(log1);
		logLineDao.save(log2);
		logLineDao.save(log3);
		logLineDao.save(log4);

		List<LogLine> result = StreamSupport.stream(logService
				.searchLogs(user.getId(), null, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1))
				.spliterator(), false).collect(Collectors.toList());
		Iterable<LogLine> expectedResult = Arrays.asList(log1, log2, log3, log4);

		for (LogLine logLine : expectedResult) {
			result.contains(logLine);
		}

	}

	@Test
	public void searchLogsTestWithLogLevel()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_ADMIN");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		LogLine log1 = new LogLine(LogLevel.WARN, LocalDateTime.now(), "Thread 1", "Logger 1", "Message 1");
		LogLine log2 = new LogLine(LogLevel.WARN, LocalDateTime.now(), "Thread 2", "Logger 2", "Message 2");
		LogLine log3 = new LogLine(LogLevel.ERROR, LocalDateTime.now(), "Thread 3", "Logger 3", "Message 3");
		LogLine log4 = new LogLine(LogLevel.INFO, LocalDateTime.now(), "Thread 4", "Logger 4", "Message 4");

		logLineDao.save(log1);
		logLineDao.save(log2);
		logLineDao.save(log3);
		logLineDao.save(log4);

		List<LogLine> result = StreamSupport.stream(logService.searchLogs(user.getId(), LogLevel.WARN,
				LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)).spliterator(), false)
				.collect(Collectors.toList());
		Iterable<LogLine> expectedResult = Arrays.asList(log1, log2);

		for (LogLine logLine : expectedResult) {
			result.contains(logLine);
		}

	}

	@Test
	public void searchLogsTestExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> logService.searchLogs(-1, LogLevel.WARN,
				LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));

	}

	@Test
	public void searchLogsTestExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role = createRole("ROLE_FACULTATIVE");

		roleDao.save(role);

		Iterable<Integer> roles = Arrays.asList(role.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> logService.searchLogs(user.getId(), LogLevel.WARN,
				LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));

	}

}
