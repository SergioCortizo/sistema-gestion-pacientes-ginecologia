package es.udc.fic.ginecologia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.ContraceptiveForm;
import es.udc.fic.ginecologia.model.Contraceptive;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.service.ContraceptiveService;

@Controller
public class ContraceptiveController {

	@Autowired
	ContraceptiveService contraceptiveService;

	@Autowired
	PermissionChecker permissionChecker;

	// Contraceptives list
	@GetMapping("/contraceptive/contraceptive-list")
	public String goToContraceptivesManagement(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/contraceptive/contraceptive-list");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/contraceptive/contraceptive-list");
			return "/error/403";
		}

		Iterable<Contraceptive> contraceptives = contraceptiveService.findAllContraceptives();

		prepareModel(model, contraceptives);

		LoggingUtility.logGetResource(username, "GET", "/contraceptive/contraceptive-list");

		return "contraceptive/contraceptive-list";
	}

	// Contraceptives list
	@GetMapping("/contraceptive/contraceptive-list-error")
	public String goToContraceptivesManagementError(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/contraceptive/contraceptive-list-error");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/contraceptive/contraceptive-list-error");
			return "/error/403";
		}

		Iterable<Contraceptive> contraceptives = contraceptiveService.findAllContraceptives();

		prepareModel(model, contraceptives);
		model.addAttribute("duplicateContraceptive", true);

		LoggingUtility.logGetResource(username, "GET", "/contraceptive/contraceptive-list");

		return "contraceptive/contraceptive-list";
	}

	// Add contraceptive
	@PostMapping("/contraceptive/add-contraceptive")
	public String addContraceptive(@ModelAttribute ContraceptiveForm addContraceptiveForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/add-contraceptive");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/add-contraceptive");
			return "/error/403";
		}

		try {
			contraceptiveService.addContraceptive(userId, addContraceptiveForm.getName());
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/add-contraceptive");
			return "/error/403";
		} catch (DuplicateInstanceException e) {
			LoggingUtility.logDuplicateContraceptive(username, addContraceptiveForm.getName());
			return "redirect:/contraceptive/contraceptive-list-error";
		}
		
		LoggingUtility.logAddContraceptive(username, addContraceptiveForm);

		return "redirect:/contraceptive/contraceptive-list";
	}

	// Update contraceptive
	@PostMapping("/contraceptive/update/{id}")
	public String updateContraceptive(@PathVariable Integer id,
			@ModelAttribute ContraceptiveForm updateContraceptiveForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/update/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/update/" + id);
			return "/error/403";
		}

		try {
			contraceptiveService.updateContraceptive(userId, id, updateContraceptiveForm.getName());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, Contraceptive.class.getName(), id, "POST",
					"/contraceptive/update/" + id);
			return "/error/404";
		} catch (DuplicateInstanceException e) {
			LoggingUtility.logDuplicateContraceptive(username, updateContraceptiveForm.getName());
			return "redirect:/contraceptive/contraceptive-list-error";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/update/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logUpdateContraceptive(username, id, updateContraceptiveForm);

		return "redirect:/contraceptive/contraceptive-list";
	}

	// Change contraceptive state
	@PostMapping("/contraceptive/change-contraceptive-state/{id}")
	public String changeContraceptiveState(@PathVariable Integer id, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/change-contraceptive-state/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/change-contraceptive-state/" + id);
			return "/error/403";
		}

		try {
			contraceptiveService.changeEnablingContraceptive(userId, id);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, Contraceptive.class.getName(), id, "POST",
					"/contraceptive/change-contraceptive-state/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/change-contraceptive-state/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logChangeContraceptiveState(username, id);

		return "redirect:/contraceptive/contraceptive-list";
	}

	// Update contraceptive
	@PostMapping("/contraceptive/search-contraceptives")
	public String searchContraceptives(@ModelAttribute ContraceptiveForm searchContraceptivesForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/search-contraceptives");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/search-contraceptives");
			return "/error/403";
		}

		Iterable<Contraceptive> contraceptives;

		try {
			contraceptives = contraceptiveService.findContraceptives(userId, searchContraceptivesForm.getName(),
					searchContraceptivesForm.isEnabled());
		} catch (PermissionException | InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/contraceptive/search-contraceptives");
			return "/error/403";
		}

		prepareModel(model, contraceptives);
		
		LoggingUtility.logSearchContraceptives(username, searchContraceptivesForm, contraceptives);

		return "contraceptive/contraceptive-list";
	}

	private void prepareModel(Model model, Iterable<Contraceptive> contraceptives) {
		model.addAttribute("contraceptives", contraceptives);
		model.addAttribute("addContraceptiveForm", new ContraceptiveForm());
		model.addAttribute("searchContraceptivesForm", new ContraceptiveForm());
		model.addAttribute("updateContraceptiveForm", new ContraceptiveForm());
	}
}
