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

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		Iterable<Contraceptive> contraceptives = contraceptiveService.findAllContraceptives();

		prepareModel(model, contraceptives);

		return "contraceptive/contraceptive-list";
	}

	// Contraceptives list
	@GetMapping("/contraceptive/contraceptive-list-error")
	public String goToContraceptivesManagementError(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		Iterable<Contraceptive> contraceptives = contraceptiveService.findAllContraceptives();

		prepareModel(model, contraceptives);
		model.addAttribute("duplicateContraceptive", true);

		return "contraceptive/contraceptive-list";
	}

	// Add contraceptive
	@PostMapping("/contraceptive/add-contraceptive")
	public String addContraceptive(@ModelAttribute ContraceptiveForm addContraceptiveForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		try {
			contraceptiveService.addContraceptive(userId, addContraceptiveForm.getName());
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		} catch (DuplicateInstanceException e) {
			return "redirect:/contraceptive/contraceptive-list-error";
		} catch (PermissionException e) {
			return "/error/401";
		}

		return "redirect:/contraceptive/contraceptive-list";
	}

	// Update contraceptive
	@PostMapping("/contraceptive/update/{id}")
	public String updateContraceptive(@PathVariable Integer id,
			@ModelAttribute ContraceptiveForm updateContraceptiveForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		try {
			contraceptiveService.updateContraceptive(userId, id, updateContraceptiveForm.getName());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (DuplicateInstanceException e) {
			return "redirect:/contraceptive/contraceptive-list-error";
		} catch (PermissionException e) {
			return "/error/401";
		}

		return "redirect:/contraceptive/contraceptive-list";
	}

	// Change contraceptive state
	@PostMapping("/contraceptive/change-contraceptive-state/{id}")
	public String changeContraceptiveState(@PathVariable Integer id, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		try {
			contraceptiveService.changeEnablingContraceptive(userId, id);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/401";
		}

		return "redirect:/contraceptive/contraceptive-list";
	}

	// Update contraceptive
	@PostMapping("/contraceptive/search-contraceptives")
	public String searchContraceptives(@ModelAttribute ContraceptiveForm searchContraceptivesForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		Iterable<Contraceptive> contraceptives;

		try {
			contraceptives = contraceptiveService.findContraceptives(userId, searchContraceptivesForm.getName(),
					searchContraceptivesForm.isEnabled());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/401";
		}

		prepareModel(model, contraceptives);

		return "contraceptive/contraceptive-list";
	}

	private void prepareModel(Model model, Iterable<Contraceptive> contraceptives) {
		model.addAttribute("contraceptives", contraceptives);
		model.addAttribute("addContraceptiveForm", new ContraceptiveForm());
		model.addAttribute("searchContraceptivesForm", new ContraceptiveForm());
		model.addAttribute("updateContraceptiveForm", new ContraceptiveForm());
	}
}
