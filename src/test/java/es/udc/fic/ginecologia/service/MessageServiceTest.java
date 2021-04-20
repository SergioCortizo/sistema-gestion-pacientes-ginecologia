package es.udc.fic.ginecologia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.CommonTask;
import es.udc.fic.ginecologia.model.GrupalMessage;
import es.udc.fic.ginecologia.model.Meeting;
import es.udc.fic.ginecologia.model.Message;
import es.udc.fic.ginecologia.model.Notice;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.MessageDao;
import es.udc.fic.ginecologia.repository.NoticeDao;
import es.udc.fic.ginecologia.repository.PatientDao;
import es.udc.fic.ginecologia.repository.RoleDao;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MessageServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private PatientDao patientDao;

	@Autowired
	private NoticeDao noticeDao;

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
	public void testFindMessages() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);
		Message message2 = new Message("Subject 2", "Message 2");
		message2.setSender(user);
		message2.setReceiver(user2);
		Message message3 = new Message("Subject 3", "Message 3");
		message3.setSender(user);
		message3.setReceiver(user2);
		Message message4 = new Message("Subject 4", "Message 4");
		message4.setSender(user);
		message4.setReceiver(user2);

		messageDao.save(message1);
		messageDao.save(message2);
		messageDao.save(message3);
		messageDao.save(message4);

		Iterable<Message> expectedMessages = Arrays.asList(message1, message2, message3, message4);
		Iterable<Message> foundMessages = messageService.findMessages(user.getId());

		assertEquals(expectedMessages, foundMessages);

	}

	@Test
	public void testFindMessagesExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> messageService.findMessages(-1));

	}

	@Test
	public void testFindMessagesExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		assertThrows(PermissionException.class, () -> messageService.findMessages(user.getId()));

	}

	@Test
	public void testFindMessage() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);

		messageDao.save(message1);

		Message result = messageService.findMessage(user.getId(), message1.getId());

		assertEquals(message1, result);

	}

	@Test
	public void testFindMessageExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);

		messageDao.save(message1);

		assertThrows(InstanceNotFoundException.class, () -> messageService.findMessage(-1, message1.getId()));

	}

	@Test
	public void testFindMessageExpectMessageNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class, () -> messageService.findMessage(user.getId(), -1));

	}

	@Test
	public void testFindMessageExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> messageService.findMessage(user.getId(), 1));

	}

	@Test
	public void testFindMessageExpectPermissionExceptionForUserNotAllowedInMessage()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");
		User user3 = createUser("User 3", "user3", "user3@example.com", "postalAddress 3", "location 3", "11111111C",
				"654789233", "133112345");
		user3.setPassword("password3");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);
		userService.registerUser(user3, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);

		messageDao.save(message1);

		assertThrows(PermissionException.class, () -> messageService.findMessage(user3.getId(), message1.getId()));

	}

	@Test
	public void testFindMessageMessageReadWhenUserIsReceiver()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);

		messageDao.save(message1);

		Message result = messageService.findMessage(user2.getId(), message1.getId());

		assertTrue(result.isMessage_read());

	}

	@Test
	public void testFindMessageMessageNotReadWhenUserIsSender()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);

		messageDao.save(message1);

		Message result = messageService.findMessage(user.getId(), message1.getId());

		assertFalse(result.isMessage_read());

	}

	@Test
	public void testAddMessage() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		messageService.addMessage(user.getId(), user2.getId(), "Subject 1", "Message 1");

		Iterable<Message> result = messageDao.findByUserId(user.getId());

		result.forEach(m -> {
			assertEquals("Subject 1", m.getSubject());
			assertEquals("Message 1", m.getMessage_body());
			assertEquals(user, m.getSender());
			assertEquals(user2, m.getReceiver());

		});

	}

	@Test
	public void testAddMessageSenderNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user2, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> messageService.addMessage(-1, user2.getId(), "Subject 1", "Message 1"));

	}

	@Test
	public void testAddMessageReceiverNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> messageService.addMessage(user.getId(), -1, "Subject 1", "Message 1"));

	}

	@Test
	public void testAddMessageSenderPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId());
		Iterable<Integer> roles2 = Arrays.asList(role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles2);
		userService.registerUser(user2, roles);

		assertThrows(PermissionException.class,
				() -> messageService.addMessage(user.getId(), user2.getId(), "Subject 1", "Message 1"));

	}

	@Test
	public void testAddMessageReceiverPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId());
		Iterable<Integer> roles2 = Arrays.asList(role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles2);

		assertThrows(PermissionException.class,
				() -> messageService.addMessage(user.getId(), user2.getId(), "Subject 1", "Message 1"));

	}

	@Test
	public void testReplyMessage() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);

		messageDao.save(message1);

		messageService.replyMessage(user2.getId(), message1.getId(), "Subject reply 1", "Message reply 1");

		Iterable<Message> result = messageDao.findByUserId(user.getId());

		result.forEach(m -> {

			if (m.getMessageReplied() != null) {
				assertEquals("Subject reply 1", m.getSubject());
				assertEquals("Message reply 1", m.getMessage_body());
				assertEquals(user2, m.getSender());
				assertEquals(user, m.getReceiver());
				assertEquals(message1, m.getMessageReplied());
			}

		});

	}

	@Test
	public void testReplyMessageSenderNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);

		messageDao.save(message1);

		assertThrows(InstanceNotFoundException.class,
				() -> messageService.replyMessage(-1, message1.getId(), "Subject reply 1", "Message reply 1"));

	}

	@Test
	public void testReplyMessageMessageNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId(), role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class,
				() -> messageService.replyMessage(user.getId(), -1, "Subject reply 1", "Message reply 1"));

	}

	@Test
	public void testReplyMessagePermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);

		messageDao.save(message1);

		assertThrows(PermissionException.class, () -> messageService.replyMessage(user2.getId(), message1.getId(),
				"Subject reply 1", "Message reply 1"));

	}

	@Test
	public void testReplyMessagePermissionExceptionNotTheReceiver()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Message message1 = new Message("Subject 1", "Message 1");
		message1.setSender(user);
		message1.setReceiver(user2);

		messageDao.save(message1);

		assertThrows(PermissionException.class, () -> messageService.replyMessage(user.getId(), message1.getId(),
				"Subject reply 1", "Message reply 1"));

	}

	@Test
	public void testAddInterconsultation()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		patientDao.save(patient);

		meetingService.addMeeting(user.getId(), patient.getId(), "Activity", "Comments", null, null, null, null);

		Meeting meeting = new Meeting();

		for (Meeting m : patient.getMeetings()) {
			meeting = m;

			messageService.addInterconsultation(user.getId(), user2.getId(), m.getId(), "Interconsultation 1",
					"Interconsultation 1");
		}

		Iterable<Message> result = messageDao.findByUserId(user.getId());

		for (Message m : result) {
			if (m.getMessageReplied() != null) {
				assertEquals("Interconsultation 1", m.getSubject());
				assertEquals("Interconsultation 1", m.getMessage_body());
				assertEquals(user, m.getSender());
				assertEquals(user2, m.getReceiver());
				assertEquals(meeting, m.getInterconsultationMeeting());
			}
		}
	}

	@Test
	public void testAddInterconsultationSenderNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		patientDao.save(patient);

		meetingService.addMeeting(user.getId(), patient.getId(), "Activity", "Comments", null, null, null, null);

		for (Meeting m : patient.getMeetings()) {

			assertThrows(InstanceNotFoundException.class, () -> messageService.addInterconsultation(-1, user2.getId(),
					m.getId(), "Interconsultation 1", "Interconsultation 1"));
		}
	}

	@Test
	public void testAddInterconsultationReceiverNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Patient patient = new Patient("Patient 1", "11111111A", "630882421", "981088021", LocalDateTime.now(),
				"121416ABCD1011", "Postal address 1", "Location 1", "email1@example.com", "", "", "", "", false, 10, 50,
				LocalDateTime.now(), 0, 0, 0, 0, "", user);

		patientDao.save(patient);

		meetingService.addMeeting(user.getId(), patient.getId(), "Activity", "Comments", null, null, null, null);

		for (Meeting m : patient.getMeetings()) {

			assertThrows(InstanceNotFoundException.class, () -> messageService.addInterconsultation(user.getId(), -1,
					m.getId(), "Interconsultation 1", "Interconsultation 1"));
		}
	}

	@Test
	public void testAddInterconsultationMeetingNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		assertThrows(InstanceNotFoundException.class, () -> messageService.addInterconsultation(user.getId(),
				user2.getId(), -1, "Interconsultation 1", "Interconsultation 1"));
	}

	@Test
	public void testAddInterconsultationPermissionExceptionSender()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId());
		Iterable<Integer> roles2 = Arrays.asList(role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles2);
		userService.registerUser(user2, roles);

		assertThrows(PermissionException.class, () -> messageService.addInterconsultation(user.getId(), user2.getId(),
				1, "Interconsultation 1", "Interconsultation 1"));
	}

	@Test
	public void testAddInterconsultationPermissionExceptionReceiver()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		Role role1 = createRole("ROLE_FACULTATIVE");
		Role role2 = createRole("ROLE_ADMIN");

		roleDao.save(role1);
		roleDao.save(role2);

		Iterable<Integer> roles = Arrays.asList(role1.getId());
		Iterable<Integer> roles2 = Arrays.asList(role2.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654789133", "132112345");
		user2.setPassword("password2");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles2);

		assertThrows(PermissionException.class, () -> messageService.addInterconsultation(user.getId(), user2.getId(),
				1, "Interconsultation 1", "Interconsultation 1"));
	}

	@Test
	public void testFindCommonTasks()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		List<Integer> users = new ArrayList<>();

		messageService.addCommonTask(user.getId(), "Title 1", "Description 1", users);
		messageService.addCommonTask(user.getId(), "Title 2", "Description 2", users);
		messageService.addCommonTask(user.getId(), "Title 3", "Description 3", users);
		messageService.addCommonTask(user.getId(), "Title 4", "Description 4", users);

		Iterable<CommonTask> result = messageService.findCommonTasks(user.getId());

		for (CommonTask commonTask : result) {
			User userResult = StreamSupport.stream(commonTask.getCommonTaskUsers().spliterator(), false)
					.filter(ctu -> ctu.getUser().getId() == user.getId()).findFirst().get().getUser();

			assertEquals(user, userResult);
		}

	}

	@Test
	public void testFindCommonTasksExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> messageService.findCommonTasks(-1));

	}

	@Test
	public void testFindCommonTasksExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> messageService.findCommonTasks(user.getId()));

	}

	@Test
	public void testFindCommonTask() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		List<Integer> users = new ArrayList<>();

		messageService.addCommonTask(user.getId(), "Title 1", "Description 1", users);

		Iterable<CommonTask> result = messageService.findCommonTasks(user.getId());

		for (CommonTask commonTask : result) {
			CommonTask resultCommonTask = messageService.findCommonTask(user.getId(), commonTask.getId());

			assertEquals(commonTask, resultCommonTask);
		}

	}

	@Test
	public void testFindCommonTaskExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> messageService.findCommonTask(-1, 1));

	}

	@Test
	public void testFindCommonTaskExpectCommonTaskNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(InstanceNotFoundException.class, () -> messageService.findCommonTask(user.getId(), -1));

	}

	@Test
	public void testFindCommonTaskPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> messageService.findCommonTask(user.getId(), 1));

	}

	@Test
	public void testFindCommonTaskExpectPermisionExceptionAccessNotAllowed()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654785123", "123112345");
		user2.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		List<Integer> users = new ArrayList<>();

		messageService.addCommonTask(user.getId(), "Title 1", "Description 1", users);

		Iterable<CommonTask> result = messageService.findCommonTasks(user.getId());

		for (CommonTask commonTask : result) {
			assertThrows(PermissionException.class,
					() -> messageService.findCommonTask(user2.getId(), commonTask.getId()));
		}

	}

	@Test
	public void testAddCommonTask() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		List<Integer> users = new ArrayList<>();

		messageService.addCommonTask(user.getId(), "Title 1", "Description 1", users);

		Iterable<CommonTask> result = messageService.findCommonTasks(user.getId());

		for (CommonTask commonTask : result) {
			assertEquals("Title 1", commonTask.getTitle());
			assertEquals("Description 1", commonTask.getDescription());

			User userResult = StreamSupport.stream(commonTask.getCommonTaskUsers().spliterator(), false)
					.filter(ctu -> ctu.getUser().getId() == user.getId()).findFirst().get().getUser();

			assertEquals(user, userResult);
		}

	}

	@Test
	public void testAddCommonTaskExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		List<Integer> users = new ArrayList<>();

		assertThrows(InstanceNotFoundException.class,
				() -> messageService.addCommonTask(-1, "Title 1", "Description 1", users));

	}

	@Test
	public void testAddCommonTaskExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		List<Integer> users = new ArrayList<>();

		assertThrows(PermissionException.class,
				() -> messageService.addCommonTask(user.getId(), "Title 1", "Description 1", users));

	}

	@Test
	public void testAddGrupalMessage()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		List<Integer> users = new ArrayList<>();

		messageService.addCommonTask(user.getId(), "Title 1", "Description 1", users);

		Iterable<CommonTask> result = messageService.findCommonTasks(user.getId());

		for (CommonTask commonTask : result) {
			messageService.addGrupalMessage(user.getId(), commonTask.getId(), "Message 1.");

			for (GrupalMessage message : commonTask.getMessages()) {
				assertEquals("Message 1.", message.getMessage_body());
				assertEquals(user, message.getUser());
				assertEquals(commonTask, message.getCommonTask());
			}
		}

	}

	@Test
	public void testAddGrupalMessageExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> messageService.addGrupalMessage(-1, 1, "Message 1."));

	}

	@Test
	public void testAddGrupalMessageExpectCommonTaskNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		List<Integer> users = new ArrayList<>();

		messageService.addCommonTask(user.getId(), "Title 1", "Description 1", users);

		assertThrows(InstanceNotFoundException.class,
				() -> messageService.addGrupalMessage(user.getId(), -1, "Message 1."));

	}

	@Test
	public void testAddGrupalMessageExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> messageService.addGrupalMessage(user.getId(), 1, "Message 1."));

	}

	@Test
	public void testAddGrupalMessageExpectPermissionExceptionAccesNotAllowed()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");
		User user2 = createUser("User 2", "user2", "user2@example.com", "postalAddress 2", "location 2", "11111111B",
				"654785123", "123112345");
		user2.setPassword("password1");

		userService.registerUser(user, roles);
		userService.registerUser(user2, roles);

		List<Integer> users = new ArrayList<>();

		messageService.addCommonTask(user.getId(), "Title 1", "Description 1", users);

		Iterable<CommonTask> result = messageService.findCommonTasks(user.getId());

		for (CommonTask commonTask : result) {
			assertThrows(PermissionException.class,
					() -> messageService.addGrupalMessage(user2.getId(), commonTask.getId(), "Message 1."));
		}

	}

	@Test
	public void testCountNewNotices()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		messageService.addNotice(user.getId(), "Notice 1");
		messageService.addNotice(user.getId(), "Notice 2");
		messageService.addNotice(user.getId(), "Notice 3");
		messageService.addNotice(user.getId(), "Notice 4");

		LocalDateTime date = LocalDateTime.now().minusDays(1);

		assertTrue(messageService.countNewNotices(date) == 4);
	}

	@Test
	public void testCountNewNoticesDatetimeIsNull()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		messageService.addNotice(user.getId(), "Notice 1");
		messageService.addNotice(user.getId(), "Notice 2");
		messageService.addNotice(user.getId(), "Notice 3");
		messageService.addNotice(user.getId(), "Notice 4");

		long result = messageService.countNewNotices(null);

		assertTrue(result >= 0);
	}

	@Test
	public void testFindNotices() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		Notice notice1 = new Notice("Notice 1");
		notice1.setUser(user);
		Notice notice2 = new Notice("Notice 2");
		notice2.setUser(user);
		Notice notice3 = new Notice("Notice 3");
		notice3.setUser(user);
		Notice notice4 = new Notice("Notice 4");
		notice4.setUser(user);

		noticeDao.save(notice1);
		noticeDao.save(notice2);
		noticeDao.save(notice3);
		noticeDao.save(notice4);

		Iterable<Notice> expectedNotices = Arrays.asList(notice1, notice2, notice3, notice4);
		Iterable<Notice> result = messageService.findNotices(user.getId());

		assertEquals(expectedNotices, result);

	}

	@Test
	public void testFindNoticesExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> messageService.findNotices(-1));

	}

	@Test
	public void testFindNoticesExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> messageService.findNotices(user.getId()));

	}

	@Test
	public void testAddNotice() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE_FACULTATIVE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		messageService.addNotice(user.getId(), "Notice");

		Notice notice = noticeDao.findByNotice("Notice").get();

		assertEquals("Notice", notice.getNotice());
		assertEquals(user, notice.getUser());
		assertTrue(notice.getDatetime().isBefore(LocalDateTime.now()));
	}

	@Test
	public void testAddNoticeExpectUserNotFound()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		assertThrows(InstanceNotFoundException.class, () -> messageService.addNotice(-1, "Notice"));

	}
	
	@Test
	public void testAddNoticeExpectPermissionException()
			throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

		Role role1 = createRole("ROLE");

		roleDao.save(role1);

		Iterable<Integer> roles = Arrays.asList(role1.getId());

		User user = createUser("User 1", "user1", "user1@example.com", "postalAddress 1", "location 1", "11111111A",
				"654789123", "122112345");
		user.setPassword("password1");

		userService.registerUser(user, roles);

		assertThrows(PermissionException.class, () -> messageService.addNotice(user.getId(), "Notice"));

	}

}
