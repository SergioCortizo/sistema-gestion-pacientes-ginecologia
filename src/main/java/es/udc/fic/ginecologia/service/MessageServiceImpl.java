package es.udc.fic.ginecologia.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.CommonTask;
import es.udc.fic.ginecologia.model.CommonTaskUser;
import es.udc.fic.ginecologia.model.CommonTaskUserPK;
import es.udc.fic.ginecologia.model.GrupalMessage;
import es.udc.fic.ginecologia.model.Meeting;
import es.udc.fic.ginecologia.model.Message;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.CommonTaskDao;
import es.udc.fic.ginecologia.repository.CommonTaskUserDao;
import es.udc.fic.ginecologia.repository.GrupalMessageDao;
import es.udc.fic.ginecologia.repository.MeetingDao;
import es.udc.fic.ginecologia.repository.MessageDao;
import es.udc.fic.ginecologia.repository.UserDao;

@Transactional
@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private CommonTaskDao commonTaskDao;
	
	@Autowired
	private CommonTaskUserDao commonTaskUserDao;
	
	@Autowired
	private GrupalMessageDao grupalMessageDao;
	
	@Autowired
	private UserDao userDao;

	@Override
	public Iterable<Message> findMessages(Integer userId) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		return messageDao.findByUserId(userId);
	}

	@Override
	public Message findMessage(Integer userId, Integer messageId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		Optional<Message> messageFound = messageDao.findById(messageId);

		if (!messageFound.isPresent()) {
			throw new InstanceNotFoundException("entities.message", messageId);
		}

		Message message = messageFound.get();

		if (message.getSender().getId() != userId && message.getReceiver().getId() != userId) {
			throw new PermissionException();
		}

		if (message.getReceiver().getId() == userId) {
			message.setMessage_read(true);
		}

		return message;
	}

	@Override
	public void addMessage(Integer senderId, Integer receiverId, String subject, String message_body)
			throws InstanceNotFoundException, PermissionException {

		if ((!permissionChecker.checkIsFacultative(senderId) && !permissionChecker.checkIsAdmin(senderId))
				|| (!permissionChecker.checkIsFacultative(receiverId) && !permissionChecker.checkIsAdmin(receiverId))) {
			throw new PermissionException();
		}

		User sender = permissionChecker.checkUser(senderId);
		User receiver = permissionChecker.checkUser(receiverId);

		Message message = new Message(subject, message_body);
		message.setSender(sender);
		message.setReceiver(receiver);

		messageDao.save(message);

	}

	@Override
	public void replyMessage(Integer senderId, Integer messageId, String subject, String message_body)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(senderId) && !permissionChecker.checkIsAdmin(senderId)) {
			throw new PermissionException();
		}

		User sender = permissionChecker.checkUser(senderId);

		Optional<Message> messageFound = messageDao.findById(messageId);

		if (!messageFound.isPresent()) {
			throw new InstanceNotFoundException("entities.message", messageId);
		}

		Message messageReplied = messageFound.get();

		if (!messageReplied.getReceiver().equals(sender)) {
			throw new PermissionException();
		}

		Message message = new Message(subject, message_body);
		message.setSender(sender);
		message.setReceiver(messageReplied.getSender());
		message.setMessageReplied(messageReplied);

		messageDao.save(message);

	}

	@Override
	public void addInterconsultation(Integer senderId, Integer receiverId, Integer meetingId, String subject,
			String message_body) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(senderId) || !permissionChecker.checkIsFacultative(receiverId)) {
			throw new PermissionException();
		}

		User sender = permissionChecker.checkUser(senderId);
		User receiver = permissionChecker.checkUser(receiverId);

		Optional<Meeting> meetingFound = meetingDao.findById(meetingId);

		if (!meetingFound.isPresent()) {
			throw new InstanceNotFoundException("entities.meeting", meetingId);
		}

		Meeting meeting = meetingFound.get();

		Message message = new Message(subject, message_body);
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setInterconsultationMeeting(meeting);

		messageDao.save(message);

	}

	@Override
	public Iterable<CommonTask> findCommonTasks(Integer userId) throws InstanceNotFoundException, PermissionException {

		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		return commonTaskDao.findByUserId(userId);
	}

	@Override
	public CommonTask findCommonTask(Integer userId, Integer commonTaskId)
			throws InstanceNotFoundException, PermissionException {

		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		Optional<CommonTask> commonTaskFound = commonTaskDao.findById(commonTaskId);

		if (!commonTaskFound.isPresent()) {
			throw new InstanceNotFoundException("entities.commonTask", commonTaskId);
		}

		CommonTask commonTask = commonTaskFound.get();
		
		Optional<CommonTaskUser> commonTaskUserFound = StreamSupport.stream(commonTask.getCommonTaskUsers().spliterator(), false)
				.filter(ctu -> ctu.getPk().getUser_id() == userId).findFirst();
		
		if (!commonTaskUserFound.isPresent()) {
			throw new PermissionException();
		}
		
		CommonTaskUser commonTaskUser = commonTaskUserFound.get();
		commonTaskUser.setLast_time_read(LocalDateTime.now());

		return commonTask;
	}

	@Override
	public void addCommonTask(Integer userId, String title, String description, List<Integer> userIds)
			throws InstanceNotFoundException, PermissionException {
		
		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}
				
		CommonTask commonTask = new CommonTask(title, description);
		commonTaskDao.save(commonTask);
		
		userIds.add(userId);
		Iterable<User> users = userDao.findAllById(userIds);
		
		Set<CommonTaskUser> commonTaskUsers = new HashSet<>();
		
		users.forEach(u -> {
			CommonTaskUser commonTaskUser = new CommonTaskUser();
			
			CommonTaskUserPK pk = new CommonTaskUserPK(u.getId(), commonTask.getId());
			
			commonTaskUser.setPk(pk);
			commonTaskUser.setLast_time_read(LocalDateTime.now());
			commonTaskUser.setUser(u);
			commonTaskUser.setCommonTask(commonTask);
			
			commonTaskUserDao.save(commonTaskUser);
			
			commonTaskUsers.add(commonTaskUser);
		});
		
		commonTask.setCommonTaskUsers(commonTaskUsers);
		
	}

	@Override
	public void addGrupalMessage(Integer userId, Integer commonTaskId, String message_body)
			throws InstanceNotFoundException, PermissionException {
		
		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}
		
		Optional<CommonTask> commonTaskFound = commonTaskDao.findById(commonTaskId);

		if (!commonTaskFound.isPresent()) {
			throw new InstanceNotFoundException("entities.commonTask", commonTaskId);
		}

		CommonTask commonTask = commonTaskFound.get();

		boolean isAllowed = StreamSupport.stream(commonTask.getCommonTaskUsers().spliterator(), false)
				.anyMatch(ctu -> ctu.getPk().getUser_id() == userId);
		
		if (!isAllowed) {
			throw new PermissionException();
		}
		
		User user = permissionChecker.checkUser(userId);
		
		GrupalMessage grupalMessage = new GrupalMessage(message_body);
		grupalMessage.setCommonTask(commonTask);
		grupalMessage.setUser(user);
		
		grupalMessageDao.save(grupalMessage);
		
	}

}
