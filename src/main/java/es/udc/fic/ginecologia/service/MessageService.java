package es.udc.fic.ginecologia.service;

import java.time.LocalDateTime;
import java.util.List;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.CommonTask;
import es.udc.fic.ginecologia.model.Message;
import es.udc.fic.ginecologia.model.Notice;

public interface MessageService {

	public Iterable<Message> findMessages(Integer userId) throws InstanceNotFoundException, PermissionException;

	public Message findMessage(Integer userId, Integer messageId) throws InstanceNotFoundException, PermissionException;

	public void addMessage(Integer senderId, Integer receiverId, String subject, String message_body)
			throws InstanceNotFoundException, PermissionException;

	public void replyMessage(Integer senderId, Integer messageId, String subject, String message_body)
			throws InstanceNotFoundException, PermissionException;

	public void addInterconsultation(Integer senderId, Integer receiverId, Integer meetingId, String subject,
			String message_body) throws InstanceNotFoundException, PermissionException;

	public Iterable<CommonTask> findCommonTasks(Integer userId) throws InstanceNotFoundException, PermissionException;

	public CommonTask findCommonTask(Integer userId, Integer commonTaskId)
			throws InstanceNotFoundException, PermissionException;

	public void addCommonTask(Integer userId, String title, String description, List<Integer> userIds)
			throws InstanceNotFoundException, PermissionException;

	public void addGrupalMessage(Integer userId, Integer commonTaskId, String message_body)
			throws InstanceNotFoundException, PermissionException;
	
	public long countNewNotices(LocalDateTime datetime);
	
	public Iterable<Notice> findNotices(Integer userId) throws PermissionException, InstanceNotFoundException;
	
	public void addNotice(Integer userId, String notice) throws PermissionException, InstanceNotFoundException;

}
