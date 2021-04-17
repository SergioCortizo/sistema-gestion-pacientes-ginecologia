package es.udc.fic.ginecologia.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Meeting;
import es.udc.fic.ginecologia.model.Message;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.MeetingDao;
import es.udc.fic.ginecologia.repository.MessageDao;

@Transactional
@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private MeetingDao meetingDao;

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

}
