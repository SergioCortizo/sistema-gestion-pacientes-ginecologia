package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Message;

public interface MessageService {

	public Iterable<Message> findMessages(Integer userId) throws InstanceNotFoundException, PermissionException;

	public Message findMessage(Integer userId, Integer messageId) throws InstanceNotFoundException, PermissionException;

	public void addMessage(Integer senderId, Integer receiverId, String subject, String message_body)
			throws InstanceNotFoundException, PermissionException;

	public void replyMessage(Integer senderId, Integer messageId, String subject, String message_body)
			throws InstanceNotFoundException, PermissionException;

	public void addInterconsultation(Integer senderId, Integer receiverId, Integer meetingId, String subject,
			String message_body) throws InstanceNotFoundException, PermissionException;
}
