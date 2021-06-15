package es.udc.fic.ginecologia.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fic.ginecologia.common.GrupalMessageByDateDescendingComparator;
import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.CommonTaskConversor;
import es.udc.fic.ginecologia.form.CommonTaskElemList;
import es.udc.fic.ginecologia.form.CommonTaskForm;
import es.udc.fic.ginecologia.form.MessageForm;
import es.udc.fic.ginecologia.form.NoticeForm;
import es.udc.fic.ginecologia.model.CommonTask;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.GrupalMessage;
import es.udc.fic.ginecologia.model.Message;
import es.udc.fic.ginecologia.model.Notice;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.service.MessageService;
import es.udc.fic.ginecologia.service.UserService;

@Controller
public class MessageController {

	@Autowired
	PermissionChecker permissionChecker;

	@Autowired
	MessageService messageService;

	@Autowired
	UserService userService;

	// Mailbox
	@GetMapping("/messages/messages-list")
	public String goToMailbox(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/messages/messages-list");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/messages-list");
			return "/error/403";
		}

		Iterable<Message> messages;

		try {
			messages = messageService.findMessages(userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/messages-list");
			return "/error/403";
		}

		model.addAttribute("messages", messages);

		LoggingUtility.logGetResource(username, "GET", "/messages/messages-list");

		return "messages/messages-list";
	}

	// Mailbox
	@GetMapping("/messages/read-message/{id}")
	public String readMessage(@PathVariable Integer id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/messages/read-message/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/read-message/" + id);
			return "/error/403";
		}

		Message message;

		try {
			message = messageService.findMessage(userId, id);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, Message.class.getSimpleName(), id, "GET",
					"/messages/read-message/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/read-message/" + id);
			return "/error/403";
		}

		Iterable<User> users = userService.findAllUsers();
		Iterable<User> facultatives = userService.findAllFacultatives();

		model.addAttribute("users", users);
		model.addAttribute("facultatives", facultatives);
		model.addAttribute("messageForm", new MessageForm());
		model.addAttribute("message", message);

		LoggingUtility.logGetResource(username, "GET", "/messages/read-message/" + id);

		return "messages/read-message";
	}

	// Add message view
	@GetMapping("/messages/add-message")
	public String goToAddMessageView(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/messages/add-message");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/add-message");
			return "/error/403";
		}

		Iterable<User> users = userService.findAllUsers();
		model.addAttribute("users", users);

		model.addAttribute("messageForm", new MessageForm());

		LoggingUtility.logGetResource(username, "GET", "/messages/add-message");

		return "messages/add-message";
	}

	// Add message
	@PostMapping("/messages/add-message")
	public String addMessage(@ModelAttribute MessageForm messageForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/messages/add-message");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/add-message");
			return "/error/403";
		}

		try {
			messageService.addMessage(userId, messageForm.getReceiverId(), messageForm.getSubject(),
					messageForm.getMessage_body());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, User.class.getSimpleName(), messageForm.getReceiverId(),
					"POST", "/messages/add-message");
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/add-message");
			return "/error/403";
		}

		LoggingUtility.logAddMessage(username, messageForm);

		return "redirect:/messages/messages-list";
	}

	// Reply message
	@PostMapping("/messages/reply-message/{id}")
	public String addMessage(@PathVariable Integer id, @ModelAttribute MessageForm messageForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/messages/reply-message/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/reply-message/" + id);
			return "/error/403";
		}

		try {
			messageService.replyMessage(userId, id, messageForm.getSubject(), messageForm.getMessage_body());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, Message.class.getSimpleName(), messageForm.getReceiverId(),
					"POST", "/messages/reply-message/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/reply-message/" + id);
			return "/error/403";
		}

		LoggingUtility.logReplyMessage(username, id, messageForm);

		return "redirect:/messages/messages-list";
	}

	// Open interconsultation
	@PostMapping("/messages/open-interconsultation/{id}")
	public String openInterconsultation(@PathVariable Integer id, @ModelAttribute MessageForm messageForm, Model model,
			HttpServletRequest request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/messages/open-interconsultation/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/open-interconsultation/" + id);
			return "/error/403";
		}

		try {
			messageService.addInterconsultation(userId, messageForm.getReceiverId(), id, messageForm.getSubject(),
					messageForm.getMessage_body());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, "POST", "/messages/open-interconsultation/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/open-interconsultation/" + id);
			return "/error/403";
		}

		LoggingUtility.logOpenInterconsultation(username, id, messageForm);

		return "redirect:/messages/messages-list/";
	}

	// Get common tasks list
	@GetMapping("/messages/common-tasks-list")
	public String goToCommonTaskList(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/messages/common-tasks-list");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/common-tasks-list");
			return "/error/403";
		}

		List<CommonTaskElemList> commonTasks = new ArrayList<>();

		try {
			commonTasks = CommonTaskConversor.convertoToCommonTaskElemList(messageService.findCommonTasks(userId),
					userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/common-tasks-list");
			return "/error/403";
		}

		model.addAttribute("commonTasks", commonTasks);
		model.addAttribute("users", userService.findAllUsers());
		model.addAttribute("userId", userId);
		model.addAttribute("commonTaskForm", new CommonTaskForm());

		LoggingUtility.logGetResource(username, "GET", "/messages/common-tasks-list");

		return "messages/common-tasks-list";
	}

	// Common task details
	@GetMapping("/messages/common-task/{id}")
	public String getCommonTaskDetails(@PathVariable Integer id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/messages/common-task/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/common-task/" + id);
			return "/error/403";
		}

		CommonTask commonTask = null;

		try {
			commonTask = messageService.findCommonTask(userId, id);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/common-task/" + id);
			return "/error/403";
		}

		List<GrupalMessage> messages = new ArrayList<>(commonTask.getMessages());
		Collections.sort(messages, new GrupalMessageByDateDescendingComparator());

		model.addAttribute("commonTask", commonTask);
		model.addAttribute("addMessageForm", new MessageForm());
		model.addAttribute("messages", messages);

		LoggingUtility.logGetResource(username, "GET", "/messages/common-task/" + id);

		return "messages/common-task-details";
	}

	// Open common task
	@PostMapping("/messages/open-common-task/")
	public String openCommonTask(@ModelAttribute CommonTaskForm commonTaskForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/messages/open-common-task/");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/open-common-task/");
			return "/error/403";
		}

		try {
			messageService.addCommonTask(userId, commonTaskForm.getTitle(), commonTaskForm.getDescription(),
					commonTaskForm.getUserIds());
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/open-common-task/");
			return "/error/403";
		}

		LoggingUtility.logAddCommonTask(username, commonTaskForm);

		return "redirect:/messages/common-tasks-list";
	}

	// Add grupal message
	@PostMapping("/messages/add-grupal-message/{id}")
	public String addGrupalMessage(@PathVariable Integer id, @ModelAttribute MessageForm addMessageForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/messages/add-grupal-message/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/add-grupal-message/" + id);
			return "/error/403";
		}

		if (!addMessageForm.getMessage_body().isEmpty()) {
			try {
				messageService.addGrupalMessage(userId, id, addMessageForm.getMessage_body());
			} catch (InstanceNotFoundException | PermissionException e) {
				LoggingUtility.logDeniedAccess(username, "POST", "/messages/add-grupal-message/" + id);
				return "/error/403";
			}
		}

		LoggingUtility.logAddGrupalMessage(username, id, addMessageForm);

		return "redirect:/messages/common-task/" + id;
	}

	// Notices list
	@GetMapping("/messages/notices-list/")
	public String findNotices(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/messages/notices-list/");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/notices-list/");
			return "/error/403";
		}

		try {
			Iterable<Notice> notices = messageService.findNotices(userId);
			Collections.reverse((List<?>) notices);
			model.addAttribute("notices", notices);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/messages/notices-list/");
			return "/error/403";
		}

		model.addAttribute("noticeForm", new NoticeForm());

		LoggingUtility.logGetResource(username, "GET", "/messages/notices-list/");

		return "messages/notices-list";
	}

	// Add notice
	@PostMapping("/messages/add-notice/")
	public String addNotice(@ModelAttribute NoticeForm noticeForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)
					&& !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/messages/add-notice/");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/add-notice/");
			return "/error/403";
		}

		try {
			messageService.addNotice(userId, noticeForm.getNotice());
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/messages/add-notice/");
			return "/error/403";
		}

		LoggingUtility.logAddNotice(username, noticeForm);

		return "redirect:/messages/notices-list/";
	}

}
