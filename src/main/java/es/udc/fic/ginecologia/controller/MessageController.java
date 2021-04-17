package es.udc.fic.ginecologia.controller;

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

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.MessageForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.Message;
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

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		Iterable<Message> messages;

		try {
			messages = messageService.findMessages(userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			return "/error/403";
		}

		model.addAttribute("messages", messages);

		return "messages/messages-list";
	}

	// Mailbox
	@GetMapping("/messages/read-message/{id}")
	public String readMessage(@PathVariable Integer id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		Message message;

		try {
			message = messageService.findMessage(userId, id);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		Iterable<User> users = userService.findAllUsers();
		Iterable<User> facultatives = userService.findAllFacultatives();

		model.addAttribute("users", users);
		model.addAttribute("facultatives", facultatives);
		model.addAttribute("messageForm", new MessageForm());
		model.addAttribute("message", message);

		return "messages/read-message";
	}

	// Add message view
	@GetMapping("/messages/add-message")
	public String goToAddMessageView(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		Iterable<User> users = userService.findAllUsers();
		model.addAttribute("users", users);

		model.addAttribute("messageForm", new MessageForm());

		return "messages/add-message";
	}

	// Add message
	@PostMapping("/messages/add-message")
	public String addMessage(@ModelAttribute MessageForm messageForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		try {
			messageService.addMessage(userId, messageForm.getReceiverId(), messageForm.getSubject(),
					messageForm.getMessage_body());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/messages/messages-list";
	}

	// Reply message
	@PostMapping("/messages/reply-message/{id}")
	public String addMessage(@PathVariable Integer id, @ModelAttribute MessageForm messageForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		try {
			messageService.replyMessage(userId, id, messageForm.getSubject(), messageForm.getMessage_body());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/messages/messages-list";
	}

	// Open interconsultation
	@PostMapping("/messages/open-interconsultation/{id}")
	public String openInterconsultation(@PathVariable Integer id, @ModelAttribute MessageForm messageForm,
			Model model, HttpServletRequest request) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}
		
		try {
			messageService.addInterconsultation(userId, messageForm.getReceiverId(), id, messageForm.getSubject(), messageForm.getMessage_body());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/messages/messages-list/";
	}

}
